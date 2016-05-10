package com.bogolyandras.twentyquestions.service;

import com.bogolyandras.twentyquestions.dto.GameState;
import com.bogolyandras.twentyquestions.dto.QuestionDTO;
import com.bogolyandras.twentyquestions.dto.ThingDTO;
import com.bogolyandras.twentyquestions.persistence.dao.QuestionDAO;
import com.bogolyandras.twentyquestions.persistence.dao.ThingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope("session")
public class TeachNewService {

    @Autowired
    GameService gameService;

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private ThingDAO thingDAO;

    @PostConstruct
    public void reset() {
        answeredQuestions.clear();
        answeredThings.clear();
        remainigQuestions.clear();
        remainingThings.clear();
        thingName = null;
        questionText = null;
        currentThing = null;
        currentQuestion = null;
    }

    //<editor-fold desc="Variables">
    private List<QuestionDTO> answeredQuestions = new ArrayList<>();
    private List<ThingDTO> answeredThings = new ArrayList<>();
    private List<QuestionDTO> remainigQuestions = new ArrayList<>();
    private List<ThingDTO> remainingThings = new ArrayList<>();
    private String thingName;
    private String questionText;
    private QuestionDTO currentQuestion;
    private ThingDTO currentThing;
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
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<QuestionDTO> getRemainigQuestions() {
        return remainigQuestions;
    }

    public List<ThingDTO> getRemainingThings() {
        return remainingThings;
    }

    public QuestionDTO getCurrentQuestion() {
        return currentQuestion;
    }

    public ThingDTO getCurrentThing() {
        return currentThing;
    }
}
