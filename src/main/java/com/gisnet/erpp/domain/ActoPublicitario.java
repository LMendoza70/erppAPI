package com.gisnet.erpp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "acto_publicitario")
public class ActoPublicitario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoPublicitarioGenerator")
	@SequenceGenerator(allocationSize = 1, name = "actoPublicitarioGenerator", sequenceName="acto_publicitario_seq")
    private Long id;
	
	@Column(name = "numero")
    private int numero;
	
	@ManyToOne(optional = false)
	@NotNull
	private Acto acto;
	
	@ManyToOne()	
	private Oficina oficina;
	
	@Column(name = "id_folio_real")
	private Integer id_folio_real;

	@Column(name = "num_folio_real")
    private Integer num_folio_real;
	

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public Acto getActo() {
		return acto;
	}

	public ActoPublicitario acto(Acto acto){
		this.acto=acto;
		return this;
	}

	public void setActo(Acto acto) {
		this.acto = acto;
	}

	public Oficina getOficina() {
		return oficina;
	}
	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}
	public Integer getId_folio_real() {
		return id_folio_real;
	}
	public void setId_folio_real(Integer id_folio_real) {
		this.id_folio_real = id_folio_real;
	}
	public Integer getNum_folio_real() {
		return num_folio_real;
	}
	public void setNum_folio_real(Integer num_folio_real) {
		this.num_folio_real = num_folio_real;
	}

	public String toString() {
		return "id: "+this.getId()+" acto: "+this.getActo()+" numeroPublicitario: "+
	this.getNumero()+" oficina: "+ this.oficina+ " num_folio_real: "+this.num_folio_real+" id_folio_real: "+this.id_folio_real;
	
	}
}