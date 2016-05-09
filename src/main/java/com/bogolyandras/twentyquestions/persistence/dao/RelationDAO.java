package com.bogolyandras.twentyquestions.persistence.dao;

import com.bogolyandras.twentyquestions.persistence.entity.Question;
import com.bogolyandras.twentyquestions.persistence.entity.Relation;
import com.bogolyandras.twentyquestions.persistence.entity.Thing;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RelationDAO extends CrudRepository<Relation, Long> {

	@Query("SELECT r FROM Relation r JOIN r.question q JOIN r.thing t WHERE q=:question AND t=:thing")
	public Relation getSpecificRelation(Question question, Thing thing);
	
}
