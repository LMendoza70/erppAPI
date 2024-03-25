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

@Table(name = "recurso_inconformidad")
@Entity
public class RecursoInconformidad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recursoInconformidadGenerator")
	@SequenceGenerator(allocationSize = 1, name = "recursoInconformidadGenerator", sequenceName = "recurso_inconformidad_seq")
	private Long id;

	@ManyToOne
	private Acto acto;

	@ManyToOne
	private Prelacion prelacion;

	@Column(name = "confirma_denegacion")
	private Boolean confirmaDenegacion;

	@Column(name = "fundamento")
	private String fundamento;

	@ManyToOne
	private TipoInconformidad tipoInconformidad;

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

	public Prelacion getPrelacion() {
		return prelacion;
	}

	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}

	public Boolean getConfirmaDenegacion() {
		return confirmaDenegacion;
	}

	public void setConfirmaDenegacion(Boolean confirmaDenegacion) {
		this.confirmaDenegacion = confirmaDenegacion;
	}

	public String getFundamento() {
		return fundamento;
	}

	public void setFundamento(String fundamento) {
		this.fundamento = fundamento;
	}

	public TipoInconformidad getTipoInconformidad() {
		return tipoInconformidad;
	}

	public void setTipoInconformidad(TipoInconformidad tipoInconformidad) {
		this.tipoInconformidad = tipoInconformidad;
	}

}
