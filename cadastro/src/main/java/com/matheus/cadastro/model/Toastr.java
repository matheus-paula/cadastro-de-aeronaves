package com.matheus.cadastro.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Toastr implements Serializable {
	private String title;
	private String message;
	private ToastrType type;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ToastrType getType() {
		return type;
	}

	public void setType(ToastrType type) {
		this.type = type;
	}
}