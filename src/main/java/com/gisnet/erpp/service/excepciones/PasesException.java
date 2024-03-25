package com.gisnet.erpp.service.excepciones;

public class PasesException extends Exception {

	private static final long serialVersionUID = 275881878863711983L;

	private String message;

	public PasesException(String mensaje) {
		this.message = mensaje;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String mensaje) {
		this.message = mensaje;
	}

}