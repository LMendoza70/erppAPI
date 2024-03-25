package com.gisnet.erpp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ModuloCancelaActo.
 */
@Entity
@Table(name = "campo_cancela_acto")
public class CampoCancelaActo  implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campoCancelaActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "campoCancelaActoGenerator", sequenceName="campo_cancela_acto_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private CampoValores campoValor;
    
    @ManyToOne(optional = false)
    @NotNull
    private TipoActo tipoActo;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CampoValores getCampoValor() {
        return campoValor;
    }

    public CampoCancelaActo campoValor(CampoValores campoValor) {
        this.campoValor = campoValor;
        return this;
    }

    public void setCampoValor(CampoValores campoValor) {
        this.campoValor = campoValor;
    }
    
    public TipoActo getTipoActo() {
        return tipoActo;
    }

    public CampoCancelaActo tipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
        return this;
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
        CampoCancelaActo cancelaActo = (CampoCancelaActo) o;
        if (cancelaActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cancelaActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CampoCancelaActo{" +
            "id=" + getId() +
            ", campoValor='" + (getCampoValor() != null ? getCampoValor().getId() : null) + "'" +
            ", tipoActo='" + (getTipoActo() != null ? getTipoActo().getId() : null) + "'" +
            "}";
    }
}
