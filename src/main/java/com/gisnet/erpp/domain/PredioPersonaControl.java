package com.gisnet.erpp.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="predio_persona_control")
public class PredioPersonaControl  implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "predioPersonaControlGenerator")
    @SequenceGenerator(allocationSize = 1, name = "predioPersonaControlGenerator", sequenceName="predio_persona_control_seq")
    private Long id;
	
    @ManyToOne
	private PredioPersona predioPersona;
    
    @ManyToOne
    private Acto acto;
    
    @ManyToOne
    private PjPersona PjPersona;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PredioPersona getPredioPersona() {
		return predioPersona;
	}

	public void setPredioPersona(PredioPersona predioPersona) {
		this.predioPersona = predioPersona;
	}

	public Acto getActo() {
		return acto;
	}

	public void setActo(Acto acto) {
		this.acto = acto;
	}

	public PjPersona getPjPersona() {
		return PjPersona;
	}

	public void setPjPersona(PjPersona pjPersona) {
		PjPersona = pjPersona;
	}
	
}
