package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TipoDocTipoActo.
 */
@Entity
@Table(name = "tipo_doc_tipo_acto")
public class TipoDocTipoActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoDocTipoActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoDocTipoActoGenerator", sequenceName="tipo_doc_tipo_acto_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private TipoDocumento tipoDocumento;

    @ManyToOne(optional = false)
    @NotNull
    private TipoActo tipoActo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public TipoDocTipoActo tipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        return this;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public TipoActo getTipoActo() {
        return tipoActo;
    }

    public TipoDocTipoActo tipoActo(TipoActo tipoActo) {
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
        TipoDocTipoActo tipoDocTipoActo = (TipoDocTipoActo) o;
        if (tipoDocTipoActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoDocTipoActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoDocTipoActo{" +
            "id=" + getId() +
            "}";
    }
}
