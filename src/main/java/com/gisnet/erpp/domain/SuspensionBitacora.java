package com.gisnet.erpp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "suspension_bitacora")
@Entity
public class SuspensionBitacora {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suspensionBitacoraGenerator")
	@SequenceGenerator(allocationSize = 1, name = "suspensionBitacoraGenerator", sequenceName = "servicio_seq")
	private Long id;

	@ManyToOne(optional = false)
	@NotNull
	@JsonIgnore
	private Prelacion prelacion;

	@Column(name = "estatus_suspencion")
	private Long estatusSuspension;

	@Column(name = "fecha_suspencion")
	private Date fechaSuspension;

	@Column(name = "fecha_calculo")
	private Date fechaCalculo;

	@Column(name = "datos")
	@JsonIgnore
	private String datos;

	@Size(max = 4000)
	@Column(name = "observaciones", length = 4000)
	private String observaciones;

	@Size(max = 2000)
	@Column(name = "motivo", length = 2000)
	private String motivo;

	@Size(max = 2000)
	@Column(name = "fundamento", length = 2000)
	private String fundamento;

	@ManyToOne
	private Usuario usuario;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Prelacion getPrelacion() {
		return prelacion;
	}

	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}

	public Long getEstatusSuspension() {
		return estatusSuspension;
	}

	public void setEstatusSuspension(Long estatusSuspension) {
		this.estatusSuspension = estatusSuspension;
	}

	public Date getFechaSuspension() {
		return fechaSuspension;
	}

	public void setFechaSuspension(Date fechaSuspension) {
		this.fechaSuspension = fechaSuspension;
	}

	public Date getFechaCalculo() {
		return fechaCalculo;
	}

	public void setFechaCalculo(Date fechaCalculo) {
		this.fechaCalculo = fechaCalculo;
	}

	public String getDatos() {
		return datos;
	}

	public void setDatos(String datos) {
		this.datos = datos;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getFundamento() {
		return fundamento;
	}

	public void setFundamento(String fundamento) {
		this.fundamento = fundamento;
	}

}
