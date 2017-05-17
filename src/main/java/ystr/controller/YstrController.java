package ystr.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ystr.repositories.PersonRepository;
import ystr.searchForm.SearchFormSession;
import ystr.searchForm.SearchForm;

@Controller
public class YstrController {
	
	@Autowired PersonRepository personRep;
	@Autowired Session template;
	
	private SearchFormSession searchFormSession;
	@Autowired
	public YstrController(SearchFormSession searchFormSession) {
		this.searchFormSession = searchFormSession;
	}
	
	@ModelAttribute
	public SearchForm getSearchForm() {
		return searchFormSession.toForm();
	}
	
	@RequestMapping(value = "/")
	public String home(RedirectAttributes redirectAttributes) {
		return "redirect:/search";
	}
	
	@RequestMapping(value = "/search")
	public String search(SearchForm searchForm) {
		return "searchPage";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchHaplotypes(@Valid SearchForm searchForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
		if(bindingResult.hasErrors()){
			return "searchPage";
		}
		
		searchFormSession.saveForm(searchForm);
		
		final Map<String, String> parameterValues = new LinkedHashMap<>();
		
		parameterValues.put("DYS710", searchForm.getDys710());
		parameterValues.put("DYS518", searchForm.getDys518());
		parameterValues.put("DYS385a", searchForm.getDys385a());
		parameterValues.put("DYS385b", searchForm.getDys385b());
		parameterValues.put("DYS644", searchForm.getDys644());
		parameterValues.put("DYS612", searchForm.getDys612());
		parameterValues.put("DYS626", searchForm.getDys626());
		parameterValues.put("DYS504", searchForm.getDys504());
		parameterValues.put("DYS481", searchForm.getDys481());
		parameterValues.put("DYS447", searchForm.getDys447());
		parameterValues.put("DYS449", searchForm.getDys449());
		
		String query = constructQuery(parameterValues);
		final Map<String, String> paramsQuery = removeNullParams(parameterValues);
		int hapCount = runNeoQuery(query, paramsQuery);

		int totalCount = personRep.countPersons();
		int matchPerObserved = hapCount == 0 ? totalCount : (int)totalCount/hapCount;
		int matchPerExpected = (int)(totalCount)/(hapCount + 1);
		List<Integer> ciObserved = confidenceInterval(hapCount, totalCount, true);
		List<Integer> ciExpected = confidenceInterval(hapCount, totalCount, false);
		
		redirectAttributes.addFlashAttribute("hc", hapCount);
		redirectAttributes.addFlashAttribute("tc", totalCount);
		redirectAttributes.addFlashAttribute("mpo", matchPerObserved);
		redirectAttributes.addFlashAttribute("mpe", matchPerExpected);
		if(!ciObserved.isEmpty()){
			redirectAttributes.addFlashAttribute("cio1", ciObserved.get(0));
			redirectAttributes.addFlashAttribute("cio2", ciObserved.get(1));
		}
		if(!ciExpected.isEmpty()){
			redirectAttributes.addFlashAttribute("cie1", ciExpected.get(0));
			redirectAttributes.addFlashAttribute("cie2", ciExpected.get(1));
		}
		return "redirect:/search";
	}
	
	/**
	 * Compute confidence interval for Haplotype proportion
	 */
	private List<Integer> confidenceInterval(int hapCount, int totalCount, boolean observed) {
		List<Integer> ci = new ArrayList<>();
		String bool = observed == true ? "TRUE" : "FALSE";
		try {
			RConnection rconn = new RConnection();
			rconn.eval("source('/home/hocine/Rserve/scripts/ciproportion.R')");
			String rcommand = "ciproportion(" + hapCount + ", " + totalCount + ", " + bool + ")";
			double [] ciLimits = (double[]) rconn.eval(rcommand).asDoubles();
			if(ciLimits.length == 2) {
				ci.add((int)(ciLimits[1]));
				ci.add((int)(ciLimits[0]));
			}
		} catch (RserveException e) {
			e.printStackTrace();
		} catch(REXPMismatchException re) {
			re.printStackTrace();
		}
		return ci;
	}
	
	/**
	 * Remove YSTR loci with empty string and return a new map collection of only 
	 * YSTR with non empty value.
	 */
	private LinkedHashMap<String, String> removeNullParams(Map<String, String> params) {
		LinkedHashMap<String, String> paramsQuery = new LinkedHashMap<>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if(entry.getValue() != "") {
				String key = entry.getKey().substring(0, 3).toLowerCase() + 
							 entry.getKey().substring(3) + "Val";
				paramsQuery.put(key, entry.getValue());
			}
		}
		return paramsQuery;
	}
	
	/**
	 * Construct query from @parameterValues and use it as entry
	 * to query Neo4j.
	 */
	private String constructQuery(Map<String, String> parameterValues) {
		String query = "";
		String queryFirstPart = "MATCH (p:Person) ";
		String querySecondPart = "";
		int i = 1;
		for (Map.Entry<String, String> entry : parameterValues.entrySet()) {
			if(entry.getValue() != "") {
				queryFirstPart += "MATCH (y" + i + ":" + entry.getKey() + " " +
						          "{val: {" + entry.getKey().substring(0, 3).toLowerCase() + 
						          entry.getKey().substring(3) +"Val}}) ";
				querySecondPart += (i == 1) ? "WHERE" : "AND";
				querySecondPart += " (p)-[:HAS_LOCUS_" + entry.getKey() + "]->(y"+ i + ") ";
				i++;
			}
		}
		query += queryFirstPart + querySecondPart + "RETURN count(p)";
		return query;
	}
	
	/**
	 * Run dynamic Neo4j query. If the interface "PersonRepository.java" is used
	 * the query should be static and final.  
	 */
	private int runNeoQuery(String query, Map<String, String> paramsQuery) {
		Result result = template.query(query, paramsQuery);
		int countPersons = ((Long)result.iterator().next().get("count(p)")).intValue();
		
		return countPersons;
	}
}
