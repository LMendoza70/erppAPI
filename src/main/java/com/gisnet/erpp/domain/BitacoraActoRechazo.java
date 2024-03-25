package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.SortNatural;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Bitacora_Acto_rechazo.
 */
@Entity
@Table(name = "bitacora_acto_rechazo")
public class BitacoraActoRechazo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraActoRechazoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bitacoraActoRechazoGenerator", sequenceName="bitacora_acto_rechazo_seq")
    private Long id;
    
    @Column(name = "fecha")
    private Date fecha;

    @Size(max = 10000)
    @Column(name="observaciones")
    private String observaciones;

    @Column(name="motivo")
    private String motivo;

    @Column(name="fundamento")
    private String fundamento;
    
    @ManyToOne
    private Acto acto;
    
    @ManyToOne
    private Usuario usuario;


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
	
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BitacoraActoRechazo observaciones(String observaciones){
        this.observaciones = observaciones;
        return this;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public void setMotivo (String motivo){
        this.motivo = motivo;
    }

    public BitacoraActoRechazo motivo(String motivo) {
        this.motivo = motivo;
        return this;
    }
    
    public String getFundamento() {
        return this.fundamento;
    }

    public void setFundamento (String fundamento){
        this.fundamento = fundamento;
    }

    public BitacoraActoRechazo fundamento (String fundamento) {
        this.fundamento = fundamento;
        return this;
    }
    
    public Acto getActo() {
        return this.acto;
    }

    public void setActo (Acto acto){
        this.acto = acto;
    }

    public BitacoraActoRechazo acto (Acto acto) {
        this.acto = acto;
        return this;
    }
    
    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario (Usuario usuario){
        this.usuario = usuario;
    }

    public BitacoraActoRechazo usuario (Usuario usuario) {
        this.usuario = usuario;
        return this;
    }


	
}
