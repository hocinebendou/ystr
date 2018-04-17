package ystr.controller;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ystr.domain.*;
import ystr.repositories.*;

import java.util.*;


@Controller
public class AdminController {

    @Autowired
    private Session template;

    @Autowired
    private PersonRepository personRepository;

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

    @RequestMapping(value = "/administrate")
    public String adminPage() {

        return "adminPage";
    }

    @RequestMapping(value = "/administrate", method = RequestMethod.POST)
    public String leftLimit() {
        Collection<DYS710> dys710s = dys710Repository.findAll();
        Collection<DYS518> dys518s = dys518Repository.findAll();
        Collection<DYS385a> dys385as = dys385aRepository.findAll();
        Collection<DYS385b> dys385bs = dys385bRepository.findAll();
        Collection<DYS644> dys644s = dys644Repository.findAll();
        Collection<DYS612> dys612s = dys612Repository.findAll();
        Collection<DYS626> dys626s = dys626Repository.findAll();
        Collection<DYS504> dys504s = dys504Repository.findAll();
        Collection<DYS481> dys481s = dys481Repository.findAll();
        Collection<DYS447> dys447s = dys447Repository.findAll();
        Collection<DYS449> dys449s = dys449Repository.findAll();

        for (DYS710 obj: dys710s) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys710Repository.save(obj);
        }
        for (DYS518 obj: dys518s) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys518Repository.save(obj);
        }
        for (DYS385a obj: dys385as) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys385aRepository.save(obj);
        }
        for (DYS385b obj: dys385bs) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys385bRepository.save(obj);
        }
        for (DYS644 obj: dys644s) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys644Repository.save(obj);
        }
        for (DYS612 obj: dys612s) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys612Repository.save(obj);
        }
        for (DYS626 obj: dys626s) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys626Repository.save(obj);
        }
        for (DYS504 obj: dys504s) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys504Repository.save(obj);
        }
        for (DYS481 obj: dys481s) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys481Repository.save(obj);
        }
        for (DYS447 obj: dys447s) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys447Repository.save(obj);
        }
        for (DYS449 obj: dys449s) {
            obj.setLeftLimit(getLimit(obj.getVal()));
            dys449Repository.save(obj);
        }

        return "adminPage";
    }

    private float getLimit(String val) {
        if (val.indexOf(',') != -1)
            return Float.parseFloat(val.split(",")[0]);

        return Float.parseFloat(val);
    }

    @RequestMapping(value = "/kappa", method = RequestMethod.POST)
    public String countSingletons() {

        Collection<Person> persons = personRepository.findAll();

        int singletons = 0;
        for (Person person : persons) {
            List<String> haplo = new ArrayList(personRepository.haplotypeValues(person.getName()));
            int count = personRepository.countYPerson(
                    haplo.get(10), haplo.get(9), haplo.get(8), haplo.get(7), haplo.get(6),
                    haplo.get(5), haplo.get(4), haplo.get(3), haplo.get(2), haplo.get(1), haplo.get(0));
            if (count == 1)
                singletons += 1;
        }

        Map<String, Integer> params = new HashMap<>();
        params.put("singletons", singletons);
        Result result = template.query("MERGE (s:Stats {singletons: {singletons}})", params);
        System.out.println(result);

        return "adminPage";
    }
}
