package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ActoModulo.
 */
@Entity
@Table(name = "acto_modulo")
public class ActoModulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoModuloGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoModuloGenerator", sequenceName="acto_modulo_seq")
    private Long id;

    @NotNull
    @Column(name = "total", nullable = false)
    private Integer total;

    @ManyToOne(optional = false)
    @NotNull
    private ModuloTipoActo moduloTipoActo;

    @ManyToOne(optional = false)
    @NotNull
    private Acto acto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public ActoModulo total(Integer total) {
        this.total = total;
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ModuloTipoActo getModuloTipoActo() {
        return moduloTipoActo;
    }

    public ActoModulo moduloTipoActo(ModuloTipoActo moduloTipoActo) {
        this.moduloTipoActo = moduloTipoActo;
        return this;
    }

    public void setModuloTipoActo(ModuloTipoActo moduloTipoActo) {
        this.moduloTipoActo = moduloTipoActo;
    }

    public Acto getActo() {
        return acto;
    }

    public ActoModulo acto(Acto acto) {
        this.acto = acto;
        return this;
    }

    public void setActo(Acto acto) {
        this.acto = acto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActoModulo actoModulo = (ActoModulo) o;
        if (actoModulo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actoModulo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActoModulo{" +
            "id=" + getId() +
            ", total='" + getTotal() + "'" +
            "}";
    }
}
