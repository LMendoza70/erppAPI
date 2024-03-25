package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * A TipoNotificacion. 
 */
@Entity
@Table(name = "tipo_notificacion")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoNotificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoNotificacionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoNotificacionGenerator", sequenceName="tipo_notificacion_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "tipo", length = 100)
    private String tipo;
    
    @Size(max = 100)
    @Column(name = "observacion", length = 100)
    private String observacion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
    
}
