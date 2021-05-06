package com.matheus.cadastro.model;

public class CommitStatus {
	private boolean commited = false;
	private Exception exception;
	
	public CommitStatus() {}
	public CommitStatus(boolean commited) {
		this.commited = commited;
	}
	public CommitStatus(boolean commited, Exception exception) {
		this.commited = commited;
		this.exception = exception;
	}
	
	public boolean isCommited() {
		return commited;
	}
	public void setCommited(boolean commited) {
		this.commited = commited;
	}
	public Exception getException() {
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	
}
