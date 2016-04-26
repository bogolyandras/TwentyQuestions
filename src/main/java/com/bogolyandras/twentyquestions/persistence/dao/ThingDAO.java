package com.bogolyandras.twentyquestions.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bogolyandras.twentyquestions.persistence.entity.RelationType;
import com.bogolyandras.twentyquestions.persistence.entity.Thing;

public interface ThingDAO extends CrudRepository<Thing, Long> {

    @Query("SELECT t FROM Thing t WHERE t.id not in (:excludedThings)")
	public List<Thing> getPossibleThings(@Param("excludedThings") List<Long> excludedThings);

    @Query("SELECT t FROM Thing t JOIN t.relations r JOIN r.question q WHERE q.id in (:questions) AND r.type = :relationType")
    public List<Thing> getThingsByQuestion(@Param("questions") List<Long> questions, @Param("relationType") RelationType relationType);

}
