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
@Table(name = "acto_control_hereda")
public class ActoControlHereda implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoControlHeredaGenerator")
	@SequenceGenerator(allocationSize = 1, name = "actoControlHeredaGenerator", sequenceName = "acto_control_hereda_seq")
	private Long id;

	@ManyToOne
	private Acto acto;

	@ManyToOne
	private Acto actoHeredado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Acto getActo() {
		return acto;
	}

	public void setActo(Acto acto) {
		this.acto = acto;
	}

	public Acto getActoHeredado() {
		return actoHeredado;
	}

	public void setActoHeredado(Acto actoHeredado) {
		this.actoHeredado = actoHeredado;
	}

}
