package com.bogolyandras.twentyquestions.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bogolyandras.twentyquestions.dto.QuestionDTO;
import com.bogolyandras.twentyquestions.dto.RelationDTO;
import com.bogolyandras.twentyquestions.dto.ThingDTO;
import com.bogolyandras.twentyquestions.service.KnowledgeService;

@Controller
public class KnowledgeController {
	
	@Autowired
	private KnowledgeService knowledgeService;
	
	@RequestMapping(value = "/questions", method = RequestMethod.GET)
	public String questions(ModelMap model) {
		model.addAttribute("question", new QuestionDTO());
		model.addAttribute("questions", knowledgeService.getQuestions());
		return "questions";
	}
	
	@RequestMapping(value = "/things")
	public String things(ModelMap model) {
		model.addAttribute("thing", new ThingDTO());
		model.addAttribute("things", knowledgeService.getThings());
		return "things";
	}
	
	@RequestMapping(value = "/addQuestion", method = RequestMethod.POST)
	public String addQuestion(@Valid @ModelAttribute("question") QuestionDTO questionDTO,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			model.addAttribute("message", "Failed to add question!");
			model.addAttribute("messageBackground", "bg-danger");
			model.addAttribute("showQuestionForm", true);
		} else {
			knowledgeService.addQuestion(questionDTO.getText());
			model.addAttribute("message", "Question added: " + questionDTO.getText());
			model.addAttribute("messageBackground", "bg-success");
		}
		model.addAttribute("questions", knowledgeService.getQuestions());
		return "questions";
	}
	
	@RequestMapping(value = "/addThing", method = RequestMethod.POST)
	public String addThing(@Valid @ModelAttribute("thing") ThingDTO thingDTO,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			model.addAttribute("message", "Failed to add thing!");
			model.addAttribute("messageBackground", "bg-danger");
			model.addAttribute("showThingForm", true);
		} else {
			knowledgeService.addThing(thingDTO.getName());
			model.addAttribute("message", "Thing added: " + thingDTO.getName());
			model.addAttribute("messageBackground", "bg-success");
		}
		model.addAttribute("things", knowledgeService.getThings());
		return "things";
	}
	
	@RequestMapping(value = "/thing")
	public String openThing(ModelMap model, @RequestParam("id") Long id) {
		model.addAttribute("thing", knowledgeService.getThing(id));
		model.addAttribute("definedQuestions", knowledgeService.getDefinedQuestions(id));
		model.addAttribute("undefinedQuestions", knowledgeService.getUndefinedQuestions(id));
		return "openthing";
	}
	
	@RequestMapping(value = "/teach")
	public String teach(ModelMap model) {
		model.addAttribute("thingsToTeach", knowledgeService.getThingsWithUndefinedQuestions());
		return "teach";
	}
	
	@RequestMapping(value = "/editQuestion", method = RequestMethod.POST)
	public String editQuestion(@Valid @ModelAttribute("question") QuestionDTO questionDTO,
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("message", "Failed to edit question!");
			model.addAttribute("messageBackground", "bg-danger");
		} else {
			knowledgeService.editQuestion(questionDTO);
			model.addAttribute("message", "Question edited successfully!");
			model.addAttribute("messageBackground", "bg-success");
		}
		return questions(model);
	}
	
	@RequestMapping(value = "/editThing", method = RequestMethod.POST)
	public String editThing(@Valid @ModelAttribute("thing") ThingDTO thingDTO,
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("message", "Failed to edit thing!");
			model.addAttribute("messageBackground", "bg-danger");
		} else {
			knowledgeService.editThing(thingDTO);
			model.addAttribute("message", "Thing edited successfully!");
			model.addAttribute("messageBackground", "bg-success");
		}
		return openThing(model, thingDTO.getId());
	}
	
	@RequestMapping(value = "/deleteQuestion", method = RequestMethod.POST)
	public String deleteQuestion(ModelMap model, @RequestParam("id") Long id) {
		knowledgeService.deleteQuestion(id);
		model.addAttribute("message", "Question deleted!");
		model.addAttribute("messageBackground", "bg-success");
		return questions(model);
	}
	
	@RequestMapping(value = "/deleteThing", method = RequestMethod.POST)
	public String deleteThing(ModelMap model, @RequestParam("id") Long id) {
		knowledgeService.deleteThing(id);
		model.addAttribute("message", "Thing deleted!");
		model.addAttribute("messageBackground", "bg-success");
		return things(model);
	}
	
	@RequestMapping(value = "/addRelation", method = RequestMethod.POST)
	public String addRelation(ModelMap model, @ModelAttribute("relation") RelationDTO relationDTO,
			@RequestParam(value = "teaching", required = false) String teaching ) {
		knowledgeService.addRelation(relationDTO);
		if (teaching != null && teaching.equals("yes")) {
			return teach(model);
		}
		return openThing(model, relationDTO.getThing_id());
	}
	
	@RequestMapping(value = "/deleteRelation", method = RequestMethod.POST)
	public String deleteRelation(ModelMap model, @ModelAttribute("relation") RelationDTO relationDTO) {
		knowledgeService.deleteRelation(relationDTO);
		return openThing(model, relationDTO.getThing_id());
	}
	
}
