package com.bogolyandras.twentyquestions.persistence.dao;

import com.bogolyandras.twentyquestions.persistence.entity.RelationType;
import com.bogolyandras.twentyquestions.persistence.entity.Thing;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ThingDAO extends CrudRepository<Thing, Long> {

    @Query("SELECT t FROM Thing t WHERE t.id not in (:excludedThings)")
	public List<Thing> getPossibleThings(List<Long> excludedThings);

    @Query("SELECT t FROM Thing t JOIN t.relations r JOIN r.question q WHERE q.id in (:questions) AND r.type = :relationType")
    public List<Thing> getThingsByQuestion(List<Long> questions, RelationType relationType);

}
