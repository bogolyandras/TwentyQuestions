package com.bogolyandras.twentyquestions.service;

import com.bogolyandras.twentyquestions.dto.QuestionDTO;
import com.bogolyandras.twentyquestions.dto.ThingDTO;
import com.bogolyandras.twentyquestions.persistence.entity.Question;
import com.bogolyandras.twentyquestions.persistence.entity.RelationType;
import com.bogolyandras.twentyquestions.persistence.entity.Thing;
import com.bogolyandras.twentyquestions.persistence.dao.QuestionDAO;
import com.bogolyandras.twentyquestions.persistence.dao.ThingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class GameService {

	@Autowired
	private QuestionDAO questionDAO;

    @Autowired
    private ThingDAO thingDAO;
	
	private List<QuestionDTO> answeredQuestions;
    private List<ThingDTO> possibleThings;
	
	private QuestionDTO currentQuestion;
	private String message;
	private Boolean displayMessage;
    private Boolean displayDebug;
	
	@PostConstruct
	public void initIt() {
		answeredQuestions = new ArrayList<>();
		message = "This message has to be set!";
        displayDebug = false;
		reset();
	}
	
	public void reset() {
		answeredQuestions.clear();
		displayMessage = false;
        definePossibleThings();
		defineNewQuestion();
	}
	
	public void answerYes() {
		addAnswer(RelationType.Y);
	}
	
	public void answerDunno() {
		addAnswer(RelationType.M);
	}
	
	public void answerNo() {
		addAnswer(RelationType.N);
	}
	
	private void addAnswer(RelationType relationType) {
		QuestionDTO questionDTO = getCurrentQuestion();
		if (questionDTO.getId() != null) {
			questionDTO.setRelation(relationType);
			answeredQuestions.add(questionDTO);
		}
        definePossibleThings();
		defineNewQuestion();
	}
	
	private List<Long> getAnsweredQuestionsIds() {
		List<Long> questionIdsBack = new ArrayList<>();
		for(QuestionDTO q : getAnsweredQuestions())
			questionIdsBack.add(q.getId());
		if (questionIdsBack.size() == 0)
			questionIdsBack.add(-1L);
		return questionIdsBack;
	}
	
	private void defineNewQuestion() {
        defineMostSignificantQuestion();
	}

    private void defineMostSignificantQuestion() {
        List<Long> answeredQuestions = getAnsweredQuestionsIds();
        List<Long> possibleThingIds = new ArrayList<>();
        getPossibleThings().stream().forEach(t -> possibleThingIds.add(t.getId()));

        if (possibleThingIds.size() == 0) {
            currentQuestion = new QuestionDTO();
            message = "app.game.nomorequestions";
            displayMessage = true;
            return;
        }

        List<Object[]> questionsExpectingYes =
                questionDAO.getQuestionsWithSignificance(RelationType.Y, possibleThingIds, answeredQuestions);
        List<Object[]> questionsExpectingNo =
                questionDAO.getQuestionsWithSignificance(RelationType.N, possibleThingIds, answeredQuestions);

        List<QuestionDTO> orderedQuestions = new ArrayList<>();
        for (Object[] q1 : questionsExpectingYes) {
            for (Object[] q2 : questionsExpectingNo) {
                if (((Question) q1[0]).getId() == ((Question) q2[0]).getId()) {
                    orderedQuestions.add(new QuestionDTO(((Question) q1[0]).getId(), ((Question) q1[0]).getText(),
                            Math.min(((long) q1[1]), (long) q2[1])));
                }
            }
        }
        orderedQuestions.sort(new QuestionDTO());

        if (orderedQuestions.size() == 0) {
            currentQuestion = new QuestionDTO();
            message = "app.game.foundout";
            displayMessage = true;
        } else {
            long greatestSignificance = orderedQuestions.get(0).getSignificance();
            orderedQuestions = orderedQuestions.stream()
                            .filter(questionDTO -> questionDTO.getSignificance() == greatestSignificance)
                            .collect(Collectors.toList());
            currentQuestion = orderedQuestions.get(new Random().nextInt(orderedQuestions.size()));
            displayMessage = false;
        }
    }
	
	public QuestionDTO getCurrentQuestion() {
		return currentQuestion;
	}

	public List<QuestionDTO> getAnsweredQuestions() {
		return answeredQuestions;
	}

    private void definePossibleThings() {
        List<Long> impossibleThings = getImpossibleThings();
        Iterable<Thing> allPossibleThings = thingDAO.getPossibleThings(impossibleThings);
        List<ThingDTO> possibleThingsBack = new ArrayList<>();
        for (Thing t : allPossibleThings) {
            possibleThingsBack.add(new ThingDTO(t.getId(), t.getName()));
        }
        possibleThings = possibleThingsBack;
    }

	public List<ThingDTO> getPossibleThings() {
        return possibleThings;
	}

    private List<Long> getImpossibleThings() {
        List<Long> answeredQuestionsWithYes = new ArrayList<>();
        List<Long> answeredQuestionsWithNo = new ArrayList<>();
        answeredQuestions.stream().filter(q -> q.getRelation().equals(RelationType.Y))
                .forEach(q -> answeredQuestionsWithYes.add(q.getId()));
        answeredQuestions.stream().filter(q -> q.getRelation().equals(RelationType.N))
                .forEach(q -> answeredQuestionsWithNo.add(q.getId()));
        if(answeredQuestionsWithYes.size() == 0)
            answeredQuestionsWithYes.add(-1L);
        if(answeredQuestionsWithNo.size() == 0)
            answeredQuestionsWithNo.add(-1L);
        List<Long> impossibleThingsBack = new ArrayList<>();
        List<Thing> impossibleThings;
        impossibleThings = thingDAO.getThingsByQuestion(answeredQuestionsWithYes, RelationType.N);
        impossibleThings.forEach(t -> impossibleThingsBack.add(t.getId()));
        impossibleThings = thingDAO.getThingsByQuestion(answeredQuestionsWithNo, RelationType.Y);
        impossibleThings.forEach(t -> impossibleThingsBack.add(t.getId()));
        if (impossibleThingsBack.size() == 0)
            impossibleThingsBack.add(-1L);
        return impossibleThingsBack;
    }

	public String getMessage() {
		return message;
	}

	public Boolean getDisplayMessage() {
		return displayMessage;
	}

    public Boolean getDisplayDebug() {
        return displayDebug;
    }

    public void enableDebug() {
        displayDebug = true;
    }

    public void disableDebug() {
        displayDebug = false;
    }
	
}
