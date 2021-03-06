package ystr.controller;

import java.util.*;

import javax.validation.Valid;

import org.apache.commons.collections4.IteratorUtils;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ystr.domain.*;
import ystr.repositories.*;
import ystr.searchForm.SearchFormSession;
import ystr.searchForm.SearchForm;

@Controller
public class YstrController {
	
	@Autowired
    private PersonRepository personRep;

	@Autowired
    private Session template;

	@Autowired
    private DYS710Repository dys710Repository;

    @Autowired
    private DYS518Repository dys518Repository;

    @Autowired
    private DYS385aRepository dys385aRepository;

    @Autowired
    private DYS385bRepository dys385bRepository;

    @Autowired
    private DYS644Repository dys644Repository;

    @Autowired
    private DYS612Repository dys612Repository;

    @Autowired
    private DYS626Repository dys626Repository;

    @Autowired
    private DYS504Repository dys504Repository;

    @Autowired
    private DYS481Repository dys481Repository;

    @Autowired
    private DYS447Repository dys447Repository;

    @Autowired
    private DYS449Repository dys449Repository;

    @Autowired
	private StatsRepository statsRepository;

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
	public String search(SearchForm searchForm, Model model) {
        Sort sort = new Sort(Sort.Direction.ASC, "leftLimit");
        Collection<DYS710> dys710s = dys710Repository.findAll(sort, 1);
        Collection<DYS518> dys518s = dys518Repository.findAll(sort, 1);
        Collection<DYS385a> dys385as = dys385aRepository.findAll(sort, 1);
        Collection<DYS385b> dys385bs = dys385bRepository.findAll(sort, 1);
        Collection<DYS644> dys644s = dys644Repository.findAll(sort, 1);
        Collection<DYS612> dys612s = dys612Repository.findAll(sort, 1);
        Collection<DYS626> dys626s = dys626Repository.findAll(sort, 1);
        Collection<DYS504> dys504s = dys504Repository.findAll(sort, 1);
        Collection<DYS481> dys481s = dys481Repository.findAll(sort, 1);
        Collection<DYS447> dys447s = dys447Repository.findAll(sort, 1);
        Collection<DYS449> dys449s = dys449Repository.findAll(sort, 1);

        model.addAttribute("dys710s", dys710s);
        model.addAttribute("dys518s", dys518s);
        model.addAttribute("dys385as", dys385as);
        model.addAttribute("dys385bs", dys385bs);
        model.addAttribute("dys644s", dys644s);
        model.addAttribute("dys612s", dys612s);
        model.addAttribute("dys626s", dys626s);
        model.addAttribute("dys504s", dys504s);
        model.addAttribute("dys481s", dys481s);
        model.addAttribute("dys447s", dys447s);
        model.addAttribute("dys449s", dys449s);

		return "searchPage";
	}

    @RequestMapping(value = "/moreinfo", method = RequestMethod.POST)
    public String moreInfoSearch(SearchForm searchForm, Model model) {
        final Map<String, String> parameterValues = queryParameters(searchForm);
        String queryEthnicity = constructQuery(parameterValues, "ethnicity");
        String queryCountry = constructQuery(parameterValues, "country");
        final Map<String, String> paramsQuery = removeNullParams(parameterValues);
        Map<String, Integer> hapByEthnicity = runNeoQuery(queryEthnicity, paramsQuery);
        Map<String, Integer> hapByCountry = runNeoQuery(queryCountry, paramsQuery);

        model.addAttribute("hapByEthnicity", hapByEthnicity);
        model.addAttribute("hapByCountry", hapByCountry);

        return "moreInfoPage";
    }

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchHaplotypes(@Valid SearchForm searchForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
		if(bindingResult.hasErrors()){
			return "searchPage";
		}
		
		searchFormSession.saveForm(searchForm);
		
		final Map<String, String> parameterValues = queryParameters(searchForm);
        String query = constructQuery(parameterValues, "count");
        final Map<String, String> paramsQuery = removeNullParams(parameterValues);
		int hapCount = runNeoQueryCount(query, paramsQuery);

		int totalCount = personRep.countPersons();
		int matchPerObserved = hapCount == 0 ? totalCount : (int)totalCount/hapCount;
		int matchPerExpected = (int)(totalCount + 1)/(hapCount + 1);
		List<Integer> ciObserved = confidenceInterval(hapCount, totalCount, true);
		List<Integer> ciExpected = confidenceInterval(hapCount, totalCount, false);

		Stats stats = statsRepository.findAll().get(0);

		int singletons = stats.getSingletons();
		float alpha = (float)(singletons+1)/(totalCount+1);
		int kappa = Math.round(1/(alpha/((totalCount + 1)/(hapCount+1))));

		redirectAttributes.addFlashAttribute("hc", hapCount);
		redirectAttributes.addFlashAttribute("tc", totalCount);
		redirectAttributes.addFlashAttribute("mpo", matchPerObserved);
		redirectAttributes.addFlashAttribute("mpe", matchPerExpected);
		redirectAttributes.addFlashAttribute("kappa", kappa);
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

    private Map<String, String> queryParameters(SearchForm searchForm) {
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

        return parameterValues;
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
	private String constructQuery(Map<String, String> parameterValues, String ret) {
		String query = "";
		String queryFirstPart = "MATCH (p:Person) ";
		String querySecondPart = "";
		int i = 1;
		for (Map.Entry<String, String> entry : parameterValues.entrySet()) {
			if(! entry.getValue().equals("")) {
				queryFirstPart += "MATCH (y" + i + ":" + entry.getKey() + " " +
						          "{val: {" + entry.getKey().substring(0, 3).toLowerCase() + 
						          entry.getKey().substring(3) +"Val}}) ";
				querySecondPart += (i == 1) ? "WHERE" : "AND";
				querySecondPart += " (p)-[:HAS_LOCUS_" + entry.getKey() + "]->(y"+ i + ") ";
				i++;
			}
		}

		query += queryFirstPart + querySecondPart;

		switch (ret) {
            case "count":
                query += "RETURN count(p)";
                break;
            case "ethnicity":
                query += "RETURN labels(p), p.ethnicity, count(*)";
                break;
            case "country":
                query += "MATCH(c:Country) WHERE (c)-[]->(p) RETURN labels(c), c.name, count(*)";
            default:
                break;
        }

		return query;
	}

	/**
	 * Run dynamic Neo4j query. If the interface "PersonRepository.java" is used
	 * the query should be static and final.  
	 */
	private Map<String, Integer> runNeoQuery(String query, Map<String, String> paramsQuery) {
		Result result = template.query(query, paramsQuery);

		List<Map<String, Object>> mapHaplotypes = IteratorUtils.toList(result.iterator());

		Map<String, Integer> haplotypes = new HashMap<>();
		for (Map<String, Object> i : mapHaplotypes) {
			String ethnicity = "";
			String country = "";
			for (Map.Entry<String, Object> entry : i.entrySet()) {
		        if (entry.getKey().equals("p.ethnicity")) {
		            ethnicity = entry.getValue().toString();
                } else if (entry.getKey().equals("c.name"))
                    country = entry.getValue().toString();
                else if (entry.getKey().equals("count(*)")) {
                    if (!ethnicity.equals(""))
                        haplotypes.put(ethnicity, ((Long)entry.getValue()).intValue());
                    else
                        haplotypes.put(country, ((Long)entry.getValue()).intValue());
                }
            }
        }

		return haplotypes;
	}

    private int runNeoQueryCount(String query, Map<String, String> paramsQuery) {
        Result result = template.query(query, paramsQuery);
		int countPersons = ((Long)result.iterator().next().get("count(p)")).intValue();

        return countPersons;
    }
}
