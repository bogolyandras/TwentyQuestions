package com.bogolyandras.twentyquestions.controller;

import com.bogolyandras.twentyquestions.dto.QuestionDTO;
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
        if (!model.containsAttribute("thing"))
            model.addAttribute("thing", teachNewService.getThing());
        if (!model.containsAttribute("question"))
            model.addAttribute("question", teachNewService.getQuestion());
        model.addAttribute("remainigQuestions", teachNewService.getRemainigQuestions());
        model.addAttribute("remainingThings", teachNewService.getRemainingThings());
        model.addAttribute("currentQuestion", teachNewService.getCurrentQuestion());
        model.addAttribute("currentThing", teachNewService.getCurrentThing());
        model.addAttribute("answeredQuestions", teachNewService.getAnsweredQuestions());
        model.addAttribute("answeredThings", teachNewService.getAnsweredThings());
        return "teachme";
    }

    @RequestMapping(value = "/teachme", method = RequestMethod.POST)
    public String teachMe(ModelMap model) {
        teachNewService.copyGameResults();
        return createModel(model);
    }

    @RequestMapping(value = "/teachP1", method = RequestMethod.POST)
    public String teachPhaseOne(ModelMap model, @Valid @ModelAttribute("thing") ThingDTO thingDTO,
                                BindingResult result) {
        if (!result.hasErrors()) {
            teachNewService.defineThingName(thingDTO.getName());
        }
        return createModel(model);
    }

    @RequestMapping(value = "/teachP2", method = RequestMethod.POST)
    public String teachPhaseTwo(ModelMap model, @RequestParam("answer") String answer) {
        switch(answer) {
            case "yes":
                teachNewService.p2AnswerYes();
                break;
            case "irrelevant":
                teachNewService.p2AnswerIrrelevant();
                break;
            case "no":
                teachNewService.p2AnswerNo();
                break;
        }
        return createModel(model);
    }

    @RequestMapping(value = "/teachP3", method = RequestMethod.POST)
    public String teachPhaseThree(ModelMap model, @Valid @ModelAttribute("question")QuestionDTO questionDTO,
                                BindingResult result) {
        if (!result.hasErrors()) {
            teachNewService.defineQuestionText(questionDTO.getText());
        }
        return createModel(model);
    }

    @RequestMapping(value = "/teachP4", method = RequestMethod.POST)
    public String teachPhaseFour(ModelMap model, @RequestParam("answer") String answer) {
        switch(answer) {
            case "yes":
                teachNewService.p4AnswerYes();
                break;
            case "irrelevant":
                teachNewService.p4AnswerIrrelevant();
                break;
            case "no":
                teachNewService.p4AnswerNo();
                break;
        }
        return createModel(model);
    }

    @RequestMapping(value = "/teachP5", method = RequestMethod.POST)
    public String teachFinish() {
        teachNewService.persistKnowledge();
        return "redirect:newgame";
    }

}
