package com.bogolyandras.twentyquestions.controller;

import com.bogolyandras.twentyquestions.dto.ThingDTO;
import com.bogolyandras.twentyquestions.service.TeachNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@Scope("session")
public class TeachNewController {

    @Autowired
    TeachNewService teachNewService;

    public String createModel(ModelMap model) {
        model.addAttribute("thingName", teachNewService.getThingName());
        model.addAttribute("questionText", teachNewService.getQuestionText());
        return "teachme";
    }

    @RequestMapping(value = "/teachme", method = RequestMethod.POST)
    public String teachMe(ModelMap model) {
        teachNewService.copyGameResults();
        return createModel(model);
    }

    @RequestMapping(value = "/teachThing", method = RequestMethod.POST)
    public String teachThing(ModelMap model, @RequestParam("name") String name) {
        if (name.length()==0)
            throw new IllegalStateException();
        teachNewService.setThingName(name);
        return createModel(model);
    }

}
