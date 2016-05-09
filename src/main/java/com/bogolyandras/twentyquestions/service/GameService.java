package com.bogolyandras.twentyquestions.service;

import com.bogolyandras.twentyquestions.dto.GameState;
import com.bogolyandras.twentyquestions.dto.QuestionDTO;
import com.bogolyandras.twentyquestions.dto.ThingDTO;
import com.bogolyandras.twentyquestions.persistence.dao.QuestionDAO;
import com.bogolyandras.twentyquestions.persistence.dao.ThingDAO;
import com.bogolyandras.twentyquestions.persistence.entity.Question;
import com.bogolyandras.twentyquestions.persistence.entity.RelationType;
import com.bogolyandras.twentyquestions.persistence.entity.Thing;
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

    @PostConstruct
    public void reset() {
        answeredQuestions.clear();
        gameState = GameState.InProgress;
        defineNewQuestion();
    }

    //<editor-fold desc="Variables">
    private List<QuestionDTO> answeredQuestions = new ArrayList<>();
    private List<ThingDTO> possibleThings;
	private QuestionDTO currentQuestion;
    private Boolean displayDebug = false;
    private GameState gameState;
    private Random random = new Random();
    //</editor-fold>

    //<editor-fold desc="Answering logic">
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
		defineNewQuestion();
	}
    //</editor-fold>

    //<editor-fold desc="Question defining logic">
    private List<Long> getAnsweredQuestionsIds() {
		List<Long> questionIdsBack = new ArrayList<>();
		for(QuestionDTO q : getAnsweredQuestions())
			questionIdsBack.add(q.getId());
		if (questionIdsBack.size() == 0)
			questionIdsBack.add(-1L);
		return questionIdsBack;
	}
	
	private void defineNewQuestion() {
        definePossibleThings();
        List<Long> answeredQuestions = getAnsweredQuestionsIds();
        List<Long> possibleThingIds =
                getPossibleThings()
                        .stream()
                        .map(thingDTO -> thingDTO.getId())
                        .collect(Collectors.toList());

        if (possibleThingIds.size() == 0) {
            gameState = GameState.End;
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
        orderedQuestions.sort((q1, q2) -> (int)(q2.getSignificance() - q1.getSignificance()));

        if (orderedQuestions.size() == 0) {
            gameState = GameState.End;
        } else {
            long greatestSignificance = orderedQuestions.get(0).getSignificance();
            orderedQuestions = orderedQuestions.stream()
                    .filter(questionDTO -> questionDTO.getSignificance() == greatestSignificance)
                    .collect(Collectors.toList());
            currentQuestion = orderedQuestions.get(random.nextInt(orderedQuestions.size()));
        }
	}
    //</editor-fold>

    //<editor-fold desc="Possible and impossible things">
    private void definePossibleThings() {
        List<Long> impossibleThings = getImpossibleThings();
        Iterable<Thing> allPossibleThings = thingDAO.getPossibleThings(impossibleThings);
        List<ThingDTO> possibleThingsBack = new ArrayList<>();
        for (Thing t : allPossibleThings) {
            possibleThingsBack.add(new ThingDTO(t.getId(), t.getName()));
        }
        possibleThings = possibleThingsBack;
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
    //</editor-fold>

    //<editor-fold desc="Getters and setters">
    public QuestionDTO getCurrentQuestion() {
        return currentQuestion;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public List<ThingDTO> getPossibleThings() {
        return possibleThings;
    }

    public List<QuestionDTO> getAnsweredQuestions() {
        return answeredQuestions;
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
    //</editor-fold>
	
}
