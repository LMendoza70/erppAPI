package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ActoAnte.
 */
@Entity
@Table(name = "acto_ante")
public class ActoAnte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoAnteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoAnteGenerator", sequenceName="acto_ante_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Antecedente antecedente;

    @ManyToOne(optional = false)
    @NotNull
    private Acto acto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Antecedente getAntecedente() {
        return antecedente;
    }

    public ActoAnte antecedente(Antecedente antecedente) {
        this.antecedente = antecedente;
        return this;
    }

    public void setAntecedente(Antecedente antecedente) {
        this.antecedente = antecedente;
    }

    public Acto getActo() {
        return acto;
    }

    public ActoAnte acto(Acto acto) {
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
        ActoAnte actoAnte = (ActoAnte) o;
        if (actoAnte.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actoAnte.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActoAnte{" +
            "id=" + getId() +
            "}";
    }
}
