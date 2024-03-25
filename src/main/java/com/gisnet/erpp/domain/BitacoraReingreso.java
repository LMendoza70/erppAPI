package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A BitacoraReingreso
 */
@Entity
@Table(name = "bitacora_reingreso")
 public class BitacoraReingreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraReingresoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bitacoraReingresoGenerator", sequenceName="bitacora_entrega_seq")
    private Long id;

	@Column(name = "fecha")
	@NotNull
    private Date fecha;
    
    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    @ManyToOne(optional = false)
    @NotNull
    private Prelacion prelacion;

	@Column(name = "subindice")
	@NotNull
    private Long subindice;

	@ManyToOne
    private Motivo motivo; 

	@ManyToOne
    private TipoRechazo tipoRechazo;

    @Size(max = 4000)
    @Column(name = "observaciones", length = 4000)
    private String observaciones;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Prelacion getPrelacion() {
		return prelacion;
	}

	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}

	public Long getSubindice() {
		return subindice;
	}

	public void setSubindice(Long subindice) {
		this.subindice = subindice;
	}

	public Motivo getMotivo() {
		return motivo;
	}

	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public TipoRechazo getTipoRechazo() {
		return tipoRechazo;
	}

	public void setTipoRechazo(TipoRechazo tipoRechazo) {
		this.tipoRechazo = tipoRechazo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
    
    @Override
    public String toString() {        
        return "BitacoraReingreso {"+
        " id="+getId()+
        " fecha="+getFecha().getTime()+
        " Usuario="+getUsuario().getNombreCompleto()+
        " Prelacion.consecutivo="+getPrelacion().getConsecutivo()+
        " SubIndice="+getSubindice()+
        " Motivo="+getMotivo().getNombre()+
        " TipoMotivo="+getTipoRechazo().getNombre()+
        " Observaciones="+getObservaciones()!=null?getObservaciones():""+
        " }";
    }    
     
 }