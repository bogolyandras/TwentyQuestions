package com.bogolyandras.twentyquestions.dto;

import com.bogolyandras.twentyquestions.persistence.entity.RelationType;

public class RelationDTO {

	private Long question_id;
	private Long thing_id;
	private RelationType relationType;
	
	public RelationDTO() {
	}

	public Long getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(Long question_id) {
		this.question_id = question_id;
	}

	public Long getThing_id() {
		return thing_id;
	}

	public void setThing_id(Long thing_id) {
		this.thing_id = thing_id;
	}

	public String getRelation() {
		switch (relationType) {
			case Y:
				return "yes";
			case N:
				return "no";
			case M:
				return "irrelevant";
			default:
				throw new IllegalStateException();
		}
	}
	
	public RelationType getRelationEnum() {
		return relationType;
	}

	public void setRelation(String relation) {
		switch(relation) {
		case "yes":
			this.relationType = RelationType.Y;
			break;
		case "irrelevant":
			this.relationType = RelationType.M;
			break;
		case "no":
			this.relationType = RelationType.N;
			break;
		default:
			throw new UnsupportedOperationException();
				
		}
	}
	
	public void setRelation(RelationType relation) {
		this.relationType = relation;
	}
	
}
