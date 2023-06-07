package app.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import app.Errors.MethodNotAllowed;
import app.Errors.NoSuchElement;
import app.model.Entrant;
import app.service.MyService;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class MyController {
    @Autowired
    private MyService serv;

    @ModelAttribute
    public void setup(Model m) {
        m.addAttribute("allsections", serv.getAllSections());
        m.addAttribute("allyears", serv.getAllYears());
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }
    @PostMapping("/search")
    public ModelAndView postSearch(@RequestParam(name="startdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @RequestParam(name="enddate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, @RequestParam(name="country") String country) {
        if (start.after(end)) {
            return new ModelAndView("index", "start_end_date", "Start date must be before end date");
        }
        else {

            try {
                ModelAndView mv = new ModelAndView("results");
                List<Entrant> list = serv.search(start, end, country);

                if (list.isEmpty())
                    mv.addObject("no_results", "No results found");

                else
                    mv.addObject("entrants", list);

                return mv;
            } catch (HttpClientErrorException.MethodNotAllowed e) {
                //needed because changing language when this has been performed will cause an error
                throw new MethodNotAllowed();
            }
        }
    }
    @GetMapping ("/drilldown/{id}")
    public ModelAndView drilldown(@PathVariable String id) {
        try {
            //need data from both entrant and venue table
            Entrant e = serv.findEntrantById(Long.valueOf(id));
            ModelAndView mv = new ModelAndView("drilldown", "entrant", e);
            mv.addObject("venue", serv.findVenueById(Long.valueOf(e.getVenueID())));
            return mv;
        }
        catch (NoSuchElement e) {
            throw new NoSuchElement();
        }
    }

    @GetMapping("/edit/{id}")
    public ModelAndView getEditPage(@PathVariable String id) {
        try {
            Entrant e = serv.findEntrantById(Long.valueOf(id));

            return new ModelAndView("edit", "editentrant", e);
        }
        catch (NoSuchElement e)
        {
            throw new NoSuchElement();
        }
    }

    @PostMapping("/update")
    public ModelAndView updateEntrant(@Valid @ModelAttribute("editentrant") Entrant e, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return new ModelAndView("edit");
            }
            serv.updateEntrant(e);
            return new ModelAndView("redirect:/drilldown/" + e.getId());
        }
        catch(HttpClientErrorException.MethodNotAllowed ex) {
            throw new MethodNotAllowed();
        }
    }
    @PostMapping("/othersearch")
    public ModelAndView otherSearch(@RequestParam(name="year") String year, @RequestParam(name="section") String section) {
        try {
            List<Entrant> list = serv.getBySectionAndYear(section, Integer.valueOf(year));

            ModelAndView mv = new ModelAndView("othersearch");

            if (list.isEmpty())
                mv.addObject("no_results", "No results found");
            else
                mv.addObject("entrants", list);

            return mv;
        }
        catch (HttpClientErrorException.MethodNotAllowed e) {
            throw new MethodNotAllowed();
        }
    }
}
