package com.gisnet.erpp.web.api.notario;


import com.gisnet.erpp.domain.Notario;


public class NotarioDTO {
	
    private Long id;
    
    private Integer noNotario;

    private String email;
    
    private Boolean activo;
    
    public NotarioDTO() {
    	
    }
    
    public NotarioDTO(Notario notario) {
    
    	this.setId(notario.getId());
    	this.setNoNotario(notario.getNoNotario());
    	this.setEmail(notario.getEmail());
    	this.setActivo(notario.isActivo());
    }
    
    public void setId(Long id) {
    	this.id = id;
    }
    public Long getId() {
    	return this.id;
    }
    
    public void setNoNotario(Integer noNotario) {
    	this.noNotario = noNotario;
    }
    public Integer getNoNotario() {
    	return this.noNotario;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }
    public String getEmail() {
    	return this.email;
    }
    
    public void setActivo(Boolean activo) {
    	this.activo = activo;
    }
    public Boolean isActivo() {
    	return this.activo;
    }

}
