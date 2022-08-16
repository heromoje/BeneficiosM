package com.bcp.bo.discounts.model;

public class Result {
	private String Exceptions;
	private String Message;
	private boolean State;
	public boolean isState() {
		return State;
	}
	public void setState(boolean state) {
		State = state;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getExceptions() {
		return Exceptions;
	}
	public void setExceptions(String exceptions) {
		Exceptions = exceptions;
	}
}
