package com.gisnet.erpp.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name="ControlTipoRelPersona")
@Entity
public class ControlTipoRelPersona implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "controlTipoRelPersonaGenerator")
	 @SequenceGenerator(allocationSize = 1, name = "controlTipoRelPersonaGenerator", sequenceName="control_tipo_rel_persona_seq")
	 private Long id;
	
	 @ManyToOne
	 private TipoRelPersona tipoRelPersona;
	 
	 @ManyToOne
	 private TipoOrgano tipoOrgano;
	 
	 @ManyToOne
	 @NotNull
	 private CampoValores campoValores;

	public TipoRelPersona getTipoRelPersona() {
		return tipoRelPersona;
	}

	public void setTipoRelPersona(TipoRelPersona tipoRelPersona) {
		this.tipoRelPersona = tipoRelPersona;
	}

	public CampoValores getCampoValores() {
		return campoValores;
	}

	public void setCampoValores(CampoValores campoValores) {
		this.campoValores = campoValores;
	}

	public TipoOrgano getTipoOrgano() {
		return tipoOrgano;
	}

	public void setTipoOrgano(TipoOrgano tipoOrgano) {
		this.tipoOrgano = tipoOrgano;
	}
	 
	 
	 
}
