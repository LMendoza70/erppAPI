package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Tipos de acto que estan relacionados con otros tipos de actos .
 */
@Entity
@Table(name = "tipo_acto_tipo_acto")
public class TipoActoTipoActo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoActotipoActoGenerator")
	@SequenceGenerator(allocationSize = 1, name = "tipoActotipoActoGenerator", sequenceName = "tipo_acto_tipo_acto_seq")
	private Long id;

	@ManyToOne
	private TipoActo tipoActoPadre;

	@ManyToOne
	private TipoActo tipoActo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoActo getTipoActoPadre() {
		return tipoActoPadre;
	}

	public void setTipoActoPadre(TipoActo tipoActoPadre) {
		this.tipoActoPadre = tipoActoPadre;
	}

	public TipoActo getTipoActo() {
		return tipoActo;
	}

	public void setTipoActo(TipoActo tipoActo) {
		this.tipoActo = tipoActo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TipoActoTipoActo tipoActotipoActo = (TipoActoTipoActo) o;
		if (tipoActotipoActo.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), tipoActotipoActo.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "Tipo Acto Tipo Acto id=" + getId();
	}

}
