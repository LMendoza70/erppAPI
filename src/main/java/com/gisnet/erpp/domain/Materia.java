package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Materia.
 */
@Entity
@Table(name = "materia")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Materia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materiaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "materiaGenerator", sequenceName="materia_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    /*@OneToMany(mappedBy = "materia")
    @JsonIgnore
    private Set<Juez> juezesParaMaterias = new HashSet<>();*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Materia nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*public Set<Juez> getJuezesParaMaterias() {
        return juezesParaMaterias;
    }

    public Materia juezesParaMaterias(Set<Juez> juezs) {
        this.juezesParaMaterias = juezs;
        return this;
    }

    public Materia addJuezesParaMateria(Juez juez) {
        this.juezesParaMaterias.add(juez);
        juez.setMateria(this);
        return this;
    }

    public Materia removeJuezesParaMateria(Juez juez) {
        this.juezesParaMaterias.remove(juez);
        juez.setMateria(null);
        return this;
    }

    public void setJuezesParaMaterias(Set<Juez> juezs) {
        this.juezesParaMaterias = juezs;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Materia materia = (Materia) o;
        if (materia.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materia.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Materia{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
