package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name = "pase_frac_cond")
@Entity
public class PaseFracCond implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paseFracCondGenerator")
	@SequenceGenerator(allocationSize = 1, name = "paseFracCondGenerator", sequenceName = "pase_frac_cond_seq")
	private Long id;
	@Column(name = "consecutivo")
	private Integer consecutivo;
	@Column(name = "fecha_registro")
	private Date fechaRegistro;
	@Column(name = "fecha_caduca")
	private Date fechaCaduca;
	@Column(name = "procesado")
	private Boolean procesado;

	@ManyToOne
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaCaduca() {
		return fechaCaduca;
	}

	public void setFechaCaduca(Date fechaCaduca) {
		this.fechaCaduca = fechaCaduca;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Boolean getProcesado() {
		return procesado;
	}

	public void setProcesado(Boolean procesado) {
		this.procesado = procesado;
	}

}
