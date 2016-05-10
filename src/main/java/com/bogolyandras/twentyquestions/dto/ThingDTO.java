package com.bogolyandras.twentyquestions.dto;

import com.bogolyandras.twentyquestions.persistence.entity.RelationType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ThingDTO {

	private Long id;
	
	@NotNull
	@Size(min=1, max=45)
	private String name;

	private RelationType relation;
	
	public ThingDTO() {
	}

	public ThingDTO(String name) {
		this.name = name;
	}

	public ThingDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RelationType getRelation() {
		return relation;
	}

	public void setRelation(RelationType relation) {
		this.relation = relation;
	}
}
