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

@Entity
@Table(name = "acto_publicitaro_campos_busqueda")
public class ActoPublicitarioCamposBusqueda implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoPublicitaroCamposBusquedaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoPublicitaroCamposBusquedaGenerator", sequenceName="actoPublicitaroCamposBusqueda_seq")
    private Long id;

    @Column
	private Long  moduloCampoId;

    @Column
	private Long  tipoActo;
    
    @Column
	private String tipo;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}




	public Long getModuloCampoId() {
		return moduloCampoId;
	}

	public void setModuloCampoId(Long moduloCampoId) {
		this.moduloCampoId = moduloCampoId;
	}

	public Long getTipoActo() {
		return tipoActo;
	}

	public void setTipoActo(Long tipoActo) {
		this.tipoActo = tipoActo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
