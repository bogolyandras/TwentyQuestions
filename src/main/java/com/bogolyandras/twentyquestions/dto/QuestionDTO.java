package com.bogolyandras.twentyquestions.dto;

import com.bogolyandras.twentyquestions.persistence.entity.RelationType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QuestionDTO {
	
	private Long id;
	
	@NotNull
	@Size(min=1, max=512)
	private String text;
	
	private RelationType relation;

	private long significance;
	
	public QuestionDTO() {
	}

	public QuestionDTO(String text) {
		this.text = text;
	}

	public QuestionDTO(Long id, String text) {
		this.id = id;
		this.text = text;
	}

	public QuestionDTO(Long id, String text, long significance) {
		this.id = id;
		this.text = text;
		this.significance = significance;
	}

	public QuestionDTO(Long id, String text, RelationType relation) {
		this.id = id;
		this.text = text;
		this.relation = relation;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RelationType getRelation() {
		return relation;
	}

	public void setRelation(RelationType relation) {
		this.relation = relation;
	}

	public long getSignificance() {
		return significance;
	}

	public void setSignificance(long significance) {
		this.significance = significance;
	}

}
