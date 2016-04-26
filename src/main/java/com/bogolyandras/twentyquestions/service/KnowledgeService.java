package com.bogolyandras.twentyquestions.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bogolyandras.twentyquestions.dto.QuestionDTO;
import com.bogolyandras.twentyquestions.dto.RelationDTO;
import com.bogolyandras.twentyquestions.dto.ThingDTO;
import com.bogolyandras.twentyquestions.dto.ThingWithQuestionsDTO;
import com.bogolyandras.twentyquestions.persistence.entity.Question;
import com.bogolyandras.twentyquestions.persistence.entity.Relation;
import com.bogolyandras.twentyquestions.persistence.entity.Thing;
import com.bogolyandras.twentyquestions.persistence.dao.QuestionDAO;
import com.bogolyandras.twentyquestions.persistence.dao.RelationDAO;
import com.bogolyandras.twentyquestions.persistence.dao.ThingDAO;

@Transactional
@Secured("ROLE_ADMIN")
@Service
public class KnowledgeService {

	@Autowired
	private QuestionDAO questionDAO;
	
	@Autowired
	private ThingDAO thingDAO;
	
	@Autowired
	private RelationDAO relationDAO;
	
	public List<QuestionDTO> getQuestions() {
		Iterable<Question> questions = questionDAO.findAll();
		List<QuestionDTO> questionsBack = new ArrayList<>();
		for(Question q : questions) {
			questionsBack.add(new QuestionDTO(q.getId(), q.getText()));
		}
		return questionsBack;
	}
	
	public List<ThingDTO> getThings() {
		Iterable<Thing> things = thingDAO.findAll();
		List<ThingDTO> thingsBack = new ArrayList<>();
		for(Thing t : things) {
			thingsBack.add(new ThingDTO(t.getId(), t.getName()));
		}
		return thingsBack;
	}
	
	public ThingDTO getThing(Long id) {
		Thing thing = thingDAO.findOne(id);
		return new ThingDTO(thing.getId(), thing.getName());
	}
	
	public List<QuestionDTO> getDefinedQuestions(Long id) {
		List<Object[]> questions = questionDAO.getDefinedQuestions(thingDAO.findOne(id));
		List<QuestionDTO> questionsBack = new ArrayList<>();
		for(Object[] o : questions) {	
			Question q = (Question) o[0];
			Relation r = (Relation) o[1];
			questionsBack.add(new QuestionDTO(q.getId(), q.getText(), r.getType()));
		}
		return questionsBack;
	}
	
	public List<QuestionDTO> getUndefinedQuestions(Long id) {
		List<Question> questions = questionDAO.getUndefinedQuestions(thingDAO.findOne(id));
		List<QuestionDTO> questionsBack = new ArrayList<>();
		for(Question q : questions) {
			questionsBack.add(new QuestionDTO(q.getId(), q.getText()));
		}
		return questionsBack;
	}
	
	public List<ThingWithQuestionsDTO> getThingsWithUndefinedQuestions() {
		Iterable<Thing> things = thingDAO.findAll();
		List<ThingWithQuestionsDTO> thingsBack = new ArrayList<>();
		for (Thing t : things) {
			List<Question> questions = questionDAO.getUndefinedQuestions(t);
			if (questions.size() > 0) {
				List<QuestionDTO> questionsBack = new ArrayList<>();
				for(Question q : questions) {
					questionsBack.add(new QuestionDTO(q.getId(), q.getText()));
				}
				thingsBack.add(new ThingWithQuestionsDTO(
						new ThingDTO(t.getId(), t.getName()), questionsBack));
			}
		}
		return thingsBack;
	}
	
	
	public void addQuestion(String question) {
		questionDAO.save(new Question(question));
	}
	
	public void addThing(String thing) {
		thingDAO.save(new Thing(thing));
	}
	
	public void editQuestion(QuestionDTO questionDTO) {
		Question q = questionDAO.findOne(questionDTO.getId());
		q.setText(questionDTO.getText());
		questionDAO.save(q);
	}

	public void editThing(ThingDTO thingDTO) {
		Thing t = thingDAO.findOne(thingDTO.getId());
		t.setName(thingDTO.getName());
		thingDAO.save(t);
	}
	
	public void deleteQuestion(Long id) {
		questionDAO.delete(id);
	}
	
	public void deleteThing(Long id) {
		thingDAO.delete(id);
	}
	
	public void addRelation(RelationDTO relationDTO) {
		Relation r = new Relation(relationDTO.getRelationEnum(),
				questionDAO.findOne(relationDTO.getQuestion_id()),
				thingDAO.findOne(relationDTO.getThing_id()));
		relationDAO.save(r);
	}
	
	public void deleteRelation(RelationDTO relationDTO) {
		Relation r = relationDAO.getSpecificRelation(
				questionDAO.findOne(relationDTO.getQuestion_id()),
				thingDAO.findOne(relationDTO.getThing_id()));
		relationDAO.delete(r);
	}
	
}
