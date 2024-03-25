package com.gisnet.erpp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Table(name="tipo_inconformidad")
@Entity
public class TipoInconformidad implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="tipoInconformidadGenerator")
	@SequenceGenerator(allocationSize=1,name="tipoInconformidadGenerator",sequenceName="tipo_inconformidad_seq")
	private Long id;
	
	@Size(max=150)
	@Column(name="nombre",length=150)
	private String nombre;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
