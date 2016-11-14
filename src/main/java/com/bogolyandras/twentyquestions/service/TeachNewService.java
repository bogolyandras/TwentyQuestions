package com.bogolyandras.twentyquestions.service;

import com.bogolyandras.twentyquestions.dto.GameState;
import com.bogolyandras.twentyquestions.dto.QuestionDTO;
import com.bogolyandras.twentyquestions.dto.ThingDTO;
import com.bogolyandras.twentyquestions.persistence.dao.QuestionDAO;
import com.bogolyandras.twentyquestions.persistence.dao.RelationDAO;
import com.bogolyandras.twentyquestions.persistence.dao.ThingDAO;
import com.bogolyandras.twentyquestions.persistence.entity.Question;
import com.bogolyandras.twentyquestions.persistence.entity.Relation;
import com.bogolyandras.twentyquestions.persistence.entity.RelationType;
import com.bogolyandras.twentyquestions.persistence.entity.Thing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class TeachNewService {

    @Autowired
    private GameService gameService;

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private ThingDAO thingDAO;

    @Autowired
    private RelationDAO relationDAO;

    @PostConstruct
    public void reset() {
        answeredQuestions.clear();
        answeredThings.clear();
        remainigQuestions.clear();
        remainingThings.clear();
        thing = new ThingDTO();
        question = new QuestionDTO();
    }

    //<editor-fold desc="Variables">
    private List<QuestionDTO> answeredQuestions = new ArrayList<>();
    private List<ThingDTO> answeredThings = new ArrayList<>();
    private List<QuestionDTO> remainigQuestions = new ArrayList<>();
    private List<ThingDTO> remainingThings = new ArrayList<>();
    private ThingDTO thing;
    private QuestionDTO question;
    private ThingDTO currentThing;
    private QuestionDTO currentQuestion;
    private Random random = new Random();
    //</editor-fold>

    public void copyGameResults() {
        if (gameService.getGameState() != GameState.End)
            throw new IllegalStateException();

        reset();

        gameService.getAnsweredQuestions()
                .forEach(questionDTO -> answeredQuestions.add(questionDTO));

        List<Long> answeredQuestionIds = answeredQuestions
                .stream()
                .map(questionDTO -> questionDTO.getId())
                .collect(Collectors.toList());
        if (answeredQuestionIds.size()==0)
            answeredQuestionIds.add(-1L);

        questionDAO.getRemainingQuestions(
            answeredQuestionIds
        ).forEach(question -> remainigQuestions.add(new QuestionDTO(question.getId(), question.getText())));

        thingDAO.findAll().forEach(thing -> remainingThings.add(new ThingDTO(thing.getId(), thing.getName())));

        defineCurrentStuff();
    }

    private void defineCurrentStuff() {
        if (remainingThings.size() > 0) {
            currentThing = remainingThings.get(random.nextInt(remainingThings.size()));
        } else {
            currentThing = null;
        }
        if (remainigQuestions.size() > 0) {
            currentQuestion = remainigQuestions.get(random.nextInt(remainigQuestions.size()));
        } else {
            currentQuestion = null;
        }
    }

    public void defineThingName(String name) {
        thing.setName(name);
    }

    public void defineQuestionText(String text) {
        question.setText(text);
        remainingThings.add(new ThingDTO(thing.getName()));
        defineCurrentStuff();
    }

    //<editor-fold desc="Answering logic">
    public void p2AnswerYes() {
        currentQuestion.setRelation(RelationType.Y);
        persistAnswer();
    }

    public void p2AnswerIrrelevant() {
        currentQuestion.setRelation(RelationType.M);
        persistAnswer();
    }

    public void p2AnswerNo() {
        currentQuestion.setRelation(RelationType.N);
        persistAnswer();
    }

    private void persistAnswer() {
        answeredQuestions.add(currentQuestion);
        remainigQuestions.remove(currentQuestion);
        defineCurrentStuff();
    }
    //</editor-fold>

    //<editor-fold desc="Thing answering logic">
    public void p4AnswerYes() {
        currentThing.setRelation(RelationType.Y);
        persistThing();
    }

    public void p4AnswerIrrelevant() {
        currentThing.setRelation(RelationType.M);
        persistThing();
    }

    public void p4AnswerNo() {
        currentThing.setRelation(RelationType.N);
        persistThing();
    }

    private void persistThing() {
        answeredThings.add(currentThing);
        remainingThings.remove(currentThing);
        defineCurrentStuff();
    }
    //</editor-fold>

    public void persistKnowledge() {
        if (remainigQuestions.size() != 0 || remainingThings.size() != 0) {
            throw new IllegalStateException();
        }

        //New Thing
        ThingDTO newThing = null;
        for(ThingDTO thingDTO : answeredThings) {
            if (thingDTO.getId() == null) {
                newThing = thingDTO;
                break;
            }
        }
        Thing t = new Thing(newThing.getName());
        thingDAO.save(t);
        newThing.setId(t.getId());

        //Questions for new thing
        answeredQuestions.forEach(questionDTO ->
            relationDAO.save(new Relation(questionDTO.getRelation(), questionDAO.findOne(questionDTO.getId()), t))
        );

        //New Question
        Question q = new Question(question.getText());
        questionDAO.save(q);

        //Things for new Question
        answeredThings.forEach(thingDTO ->
                relationDAO.save(new Relation((thingDTO.getRelation()), q, thingDAO.findOne(thingDTO.getId())))
        );

        reset();
    }

    //<editor-fold desc="Getters">
    public List<QuestionDTO> getRemainigQuestions() {
        return remainigQuestions;
    }

    public List<ThingDTO> getRemainingThings() {
        return remainingThings;
    }

    public ThingDTO getThing() {
        return thing;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public QuestionDTO getCurrentQuestion() {
        return currentQuestion;
    }

    public ThingDTO getCurrentThing() {
        return currentThing;
    }

    public List<QuestionDTO> getAnsweredQuestions() {
        return answeredQuestions;
    }

    public List<ThingDTO> getAnsweredThings() {
        return answeredThings;
    }
    //</editor-fold>
}
