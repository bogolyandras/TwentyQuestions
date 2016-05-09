package com.bogolyandras.twentyquestions.persistence.dao;

import com.bogolyandras.twentyquestions.persistence.entity.Question;
import com.bogolyandras.twentyquestions.persistence.entity.RelationType;
import com.bogolyandras.twentyquestions.persistence.entity.Thing;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionDAO extends CrudRepository<Question, Long> {

	@Query("SELECT q, r FROM Thing t JOIN t.relations r JOIN r.question q WHERE t=:thing")
	public List<Object[]> getDefinedQuestions(Thing thing);
	
	@Query("SELECT q FROM Question q WHERE q NOT IN "
			+ "(SELECT q FROM Thing t JOIN t.relations r JOIN r.question q WHERE t=:thing)")
	public List<Question> getUndefinedQuestions(Thing thing);
	
	@Query("SELECT q FROM Question q WHERE q.id NOT IN (:answeredQuestions)")
	public List<Question> getRemainingQuestions(List<Long> answeredQuestions);

    @Query("SELECT q, COUNT(r) AS significance FROM Question q JOIN q.relations r JOIN r.thing t " +
            "WHERE r.type = :relationType AND t.id IN (:possibleThings) AND q.id NOT IN (:answeredQuestions) " +
            "GROUP BY q ORDER BY significance DESC")
    public List<Object[]> getQuestionsWithSignificance(RelationType relationType,
                                                       List<Long> possibleThings,
                                                       List<Long> answeredQuestions);


}
