package com.bogolyandras.twentyquestions.persistence.dao;

import java.util.List;

import com.bogolyandras.twentyquestions.persistence.entity.RelationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bogolyandras.twentyquestions.persistence.entity.Question;
import com.bogolyandras.twentyquestions.persistence.entity.Thing;

public interface QuestionDAO extends CrudRepository<Question, Long> {

	@Query("SELECT q, r FROM Thing t JOIN t.relations r JOIN r.question q WHERE t=:thing")
	public List<Object[]> getDefinedQuestions(@Param("thing") Thing thing);
	
	@Query("SELECT q FROM Question q WHERE q NOT IN "
			+ "(SELECT q FROM Thing t JOIN t.relations r JOIN r.question q WHERE t=:thing)")
	public List<Question> getUndefinedQuestions(@Param("thing") Thing thing);
	
	@Query("SELECT q FROM Question q WHERE q.id NOT IN (:answeredQuestions)")
	public List<Question> getRemainingQuestions(@Param("answeredQuestions") List<Long> answeredQuestions);

    @Query("SELECT q, COUNT(r) AS significance FROM Question q JOIN q.relations r JOIN r.thing t " +
            "WHERE r.type = :relationType AND t.id IN (:possibleThings) AND q.id NOT IN (:answeredQuestions) " +
            "GROUP BY q ORDER BY significance DESC")
    public List<Object[]> getQuestionsWithSignificance(@Param("relationType") RelationType relationType,
                                                       @Param("possibleThings") List<Long> possibleThings,
                                                       @Param("answeredQuestions") List<Long> answeredQuestions);

//    SELECT questions.id, questions.text, COUNT(relations.id) AS significance
//    FROM questions
//    JOIN relations ON questions.id = relations.question_id
//    JOIN things ON things.id = relations.thing_id
//    WHERE relations.type = 'Y' AND things.id in (1, 2)
//    GROUP BY questions.id, questions.text
//    ORDER BY significance DESC

}
