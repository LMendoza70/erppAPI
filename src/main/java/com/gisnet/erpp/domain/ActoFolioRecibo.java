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
import javax.validation.constraints.NotNull;

/**
 * Relacion de Acto Predio y Recibo, ActoPredio es ActoFolio para cualquier tipo
 * de folio.
 */
@Entity
@Table(name = "acto_folio_recibo")
public class ActoFolioRecibo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoFolioReciboGenerator")
	@SequenceGenerator(allocationSize = 1, name = "actoFolioReciboGenerator", sequenceName = "acto_folio_recibo_seq")
	private Long id;

	@ManyToOne(optional = false)
	@NotNull
	private ActoPredio actoPredio;

	@ManyToOne(optional = false)
	@NotNull
	private Recibo recibo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ActoPredio getActoPredio() {
		return actoPredio;
	}

	public void setActoPredio(ActoPredio actoPredio) {
		this.actoPredio = actoPredio;
	}

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ActoFolioRecibo actoFolioRecibo = (ActoFolioRecibo) o;
		if (actoFolioRecibo.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), actoFolioRecibo.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "ActoFolioRecibo{" + "id=" + getId() + "}";
	}
}
