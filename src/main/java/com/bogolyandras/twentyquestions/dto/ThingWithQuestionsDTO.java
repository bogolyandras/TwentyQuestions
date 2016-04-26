package com.bogolyandras.twentyquestions.dto;

import java.util.List;

public class ThingWithQuestionsDTO {

	private ThingDTO thing;
	
	private List<QuestionDTO> questions;

	public ThingWithQuestionsDTO(ThingDTO thing, List<QuestionDTO> questions) {
		this.thing = thing;
		this.questions = questions;
	}

	public ThingDTO getThing() {
		return thing;
	}

	public void setThing(ThingDTO thing) {
		this.thing = thing;
	}

	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}
	
}
