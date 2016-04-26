package com.bogolyandras.twentyquestions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bogolyandras.twentyquestions.service.GameService;

@Controller
@Scope("session")
public class GameController {
	
	@Autowired
	private GameService gameService;
	
	@RequestMapping(value = {"/", "/game"}, method = RequestMethod.GET)
	public String game(ModelMap model) {
		model.addAttribute("question", gameService.getCurrentQuestion());
		model.addAttribute("answeredQuestions", gameService.getAnsweredQuestions());
		model.addAttribute("possibleThings", gameService.getPossibleThings());
		model.addAttribute("message", gameService.getMessage());
		model.addAttribute("displayMessage", gameService.getDisplayMessage());
		model.addAttribute("enableDebug", gameService.getDisplayDebug());
		return "game";
	}
	
	@RequestMapping(value = "/newgame", method = RequestMethod.GET)
	public String newGame(ModelMap model) {
		gameService.reset();
		return game(model);
	}
	
	@RequestMapping(value = "/answer", method = RequestMethod.POST)
	public String answer(ModelMap model, @RequestParam("answer") String answer) {
		switch(answer) {
		case "yes":
			gameService.answerYes();
			break;
		case "dunno":
			gameService.answerDunno();
			break;
		case "no":
			gameService.answerNo();
			break;
		}
		return game(model);
	}

	@RequestMapping(value = "/enabledebug", method = RequestMethod.GET)
	public String enableDebug(ModelMap model) {
		gameService.enableDebug();
		return game(model);
	}

	@RequestMapping(value = "/disabledebug", method = RequestMethod.GET)
	public String disableDebug(ModelMap model) {
		gameService.disableDebug();
		return game(model);
	}

}
