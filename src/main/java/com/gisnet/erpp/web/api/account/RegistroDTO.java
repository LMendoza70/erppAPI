package com.gisnet.erpp.web.api.account;

public class RegistroDTO  {
    
    private String email;

    public RegistroDTO() {
    }
    
    public RegistroDTO(String email) {
		super();
		this.email = email;
	}


    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
    public String toString() {
		StringBuilder sb = new StringBuilder("Registro=");
		sb.append("email=").append(getEmail());
        return sb.toString();
    }
}
