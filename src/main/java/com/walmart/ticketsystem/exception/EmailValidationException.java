package com.walmart.ticketsystem.exception;

/**
 * This class added to handle any custom exceptions
 * @author Sudhindra
 *
 */
public class EmailValidationException extends RuntimeException {

	private static final long serialVersionUID = -3366956474813503770L;
	private String msg;
	
	public EmailValidationException(String msg) {
		super();
		this.msg = msg;
	}
	
	public EmailValidationException(Throwable cause, String msg) {
		super(cause);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
