package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ActoRequisito.
 */
@Entity
@Table(name = "acto_requisito")
public class ActoRequisito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoRequisitoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoRequisitoGenerator", sequenceName="acto_requisito_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Requisito requisito;

    @ManyToOne(optional = false)
    @NotNull
    private Archivo archivo;

    @ManyToOne(optional = false)
    @NotNull
    private Acto acto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Requisito getRequisito() {
        return requisito;
    }

    public ActoRequisito requisito(Requisito requisito) {
        this.requisito = requisito;
        return this;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public ActoRequisito archivo(Archivo archivo) {
        this.archivo = archivo;
        return this;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    public Acto getActo() {
        return acto;
    }

    public ActoRequisito acto(Acto acto) {
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
        ActoRequisito actoRequisito = (ActoRequisito) o;
        if (actoRequisito.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actoRequisito.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActoRequisito{" +
            "id=" + getId() +
            "}";
    }
}
