package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Table(name = "solicitud_devolucion")
@Entity
public class SolicitudDevolucion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitudDevolucionGenerator")
	@SequenceGenerator(allocationSize = 1, name = "solicitudDevolucionGenerator", sequenceName = "solicitud_devolucion_seq")
	private Long id;
	
	@Column(name="consecutivo")
	private Integer consecutivo;
	
	@Column(name="fecha_ingreso")
	private Date fechaIngreso;
	
	@Column(name="fecha_autoriza_devolucion")
	private Date fechaAutorizaDevolucion;
	
	@Column(name="fecha_entrega")
	private Date fechaEntrega;
	
	@ManyToOne
	private Prelacion prelacion;

	@ManyToOne
	private Usuario recepcion;

	@ManyToOne
	private Usuario registrador;
	
	@ManyToOne
	private Usuario solicitante;
	
	@Size(max=10000)
	@Column(name="fundamento_recepcion",length=10000)
	private String fundamentoRecepcion;
	
	@Size(max=10000)
	@Column(name="fundamento_devolucion",length=10000)
	private String fundamentoDevolucion;

	@OneToMany(mappedBy="solicitudDevolucion")
	private Set<Recibo> recibosParaSolicitudDevolucion = new HashSet<>();
	
	 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Date getFechaAutorizaDevolucion() {
		return fechaAutorizaDevolucion;
	}

	public void setFechaAutorizaDevolucion(Date fechaAutorizaDevolucion) {
		this.fechaAutorizaDevolucion = fechaAutorizaDevolucion;
	}

	public Prelacion getPrelacion() {
		return prelacion;
	}

	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}

	public Usuario getRecepcion() {
		return recepcion;
	}

	public void setRecepcion(Usuario recepcion) {
		this.recepcion = recepcion;
	}

	public Usuario getRegistrador() {
		return registrador;
	}

	public void setRegistrador(Usuario registrador) {
		this.registrador = registrador;
	}

	public String getFundamentoRecepcion() {
		return fundamentoRecepcion;
	}

	public void setFundamentoRecepcion(String fundamentoRecepcion) {
		this.fundamentoRecepcion = fundamentoRecepcion;
	}

	public String getFundamentoDevolucion() {
		return fundamentoDevolucion;
	}

	public void setFundamentoDevolucion(String fundamentoDevolucion) {
		this.fundamentoDevolucion = fundamentoDevolucion;
	}

	public Usuario getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Usuario solicitante) {
		this.solicitante = solicitante;
	}

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Set<Recibo> getRecibosParaSolicitudDevolucion() {
		return recibosParaSolicitudDevolucion;
	}

	public void setRecibosParaSolicitudDevolucion(Set<Recibo> recibosParaSolicitudDevolucion) {
		this.recibosParaSolicitudDevolucion = recibosParaSolicitudDevolucion;
	}

	public Date getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	

}
