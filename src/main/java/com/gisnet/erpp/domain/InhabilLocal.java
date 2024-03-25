package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A InhabilLocal.
 */
@Entity
@Table(name = "inhabil_local")
public class InhabilLocal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inhabilLocalGenerator")
    @SequenceGenerator(allocationSize = 1, name = "inhabilLocalGenerator", sequenceName="inhabil_local_seq")
    private Long id;

    @ManyToOne
    private Oficina oficina;

    @ManyToOne
    private DiaInhabil diaInhabil;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Oficina getOficina(){
        return oficina;
    }

    public InhabilLocal oficina(Oficina oficina){
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina){
        this.oficina = oficina;
    }

    public DiaInhabil getDiaInhabil(){
        return diaInhabil;
    }

    public InhabilLocal diaInhabil(DiaInhabil diaInhabil){
        this.diaInhabil = diaInhabil;
        return this;
    }

    public void setDiaInhabil(DiaInhabil diaInhabil){
        this.diaInhabil = diaInhabil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InhabilLocal inhabilLocal = (InhabilLocal) o;
        if (inhabilLocal.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), inhabilLocal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InhabilLocal{" +
            "id=" + getId() +
            "}";
    }
}