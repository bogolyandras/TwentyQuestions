package com.bogolyandras.twentyquestions.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ThingDTO {

	private Long id;
	
	@NotNull
	@Size(min=1, max=45)
	private String name;
	
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
	
}
