package com.gisnet.erpp.web.api.usuario;

public class EmailDTO {
	
	private String email;
	
	public EmailDTO() {
		
	}

	public EmailDTO(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
