package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ConfCotizador.
 */
@Entity
@Table(name = "conf_cotizador")
public class ConfCotizador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confCotizadorGenerator")
    @SequenceGenerator(allocationSize = 1, name = "confCotizadorGenerator", sequenceName="conf_cotizador_seq")
    private Long id;

    @Column(name = "orden")
    private Integer orden;

    @OneToMany(mappedBy = "confCotizador")
    @JsonIgnore
    private Set<ReciboServicioCampo> reciboServicioCamposParaConfCotizadors = new HashSet<>();

    @ManyToOne
    private CampoCotizador campoCotizador;

    @ManyToOne(optional = false)
    @NotNull
    private ConceptoPago conceptoPago;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrden() {
        return orden;
    }

    public ConfCotizador orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Set<ReciboServicioCampo> getReciboServicioCamposParaConfCotizadors() {
        return reciboServicioCamposParaConfCotizadors;
    }

    public ConfCotizador reciboServicioCamposParaConfCotizadors(Set<ReciboServicioCampo> reciboServicioCampos) {
        this.reciboServicioCamposParaConfCotizadors = reciboServicioCampos;
        return this;
    }

    public ConfCotizador addReciboServicioCamposParaConfCotizador(ReciboServicioCampo reciboServicioCampo) {
        this.reciboServicioCamposParaConfCotizadors.add(reciboServicioCampo);
        reciboServicioCampo.setConfCotizador(this);
        return this;
    }

    public ConfCotizador removeReciboServicioCamposParaConfCotizador(ReciboServicioCampo reciboServicioCampo) {
        this.reciboServicioCamposParaConfCotizadors.remove(reciboServicioCampo);
        reciboServicioCampo.setConfCotizador(null);
        return this;
    }

    public void setReciboServicioCamposParaConfCotizadors(Set<ReciboServicioCampo> reciboServicioCampos) {
        this.reciboServicioCamposParaConfCotizadors = reciboServicioCampos;
    }

    public CampoCotizador getCampoCotizador() {
        return campoCotizador;
    }

    public ConfCotizador campoCotizador(CampoCotizador campoCotizador) {
        this.campoCotizador = campoCotizador;
        return this;
    }

    public void setCampoCotizador(CampoCotizador campoCotizador) {
        this.campoCotizador = campoCotizador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfCotizador confCotizador = (ConfCotizador) o;
        if (confCotizador.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), confCotizador.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfCotizador{" +
            "id=" + getId() +
            ", orden='" + getOrden() + "'" +
            "}";
    }

	public ConceptoPago getConceptoPago() {
		return conceptoPago;
	}

	public void setConceptoPago(ConceptoPago conceptoPago) {
		this.conceptoPago = conceptoPago;
	}
}
