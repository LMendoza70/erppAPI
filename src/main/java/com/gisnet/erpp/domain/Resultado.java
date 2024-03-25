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
 * A Resultado.
 */
@Entity
@Table(name = "resultado")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Resultado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resultadoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "resultadoGenerator", sequenceName="resultado_seq")
    private Long id;

    @Size(max = 50)
    @Column(name = "nombre", length = 50)
    private String nombre;

    @OneToMany(mappedBy = "resultado")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaResultados = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Resultado nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Prelacion> getPrelacionesParaResultados() {
        return prelacionesParaResultados;
    }

    public Resultado prelacionesParaResultados(Set<Prelacion> prelacions) {
        this.prelacionesParaResultados = prelacions;
        return this;
    }

    public Resultado addPrelacionesParaResultado(Prelacion prelacion) {
        this.prelacionesParaResultados.add(prelacion);
        prelacion.setResultado(this);
        return this;
    }

    public Resultado removePrelacionesParaResultado(Prelacion prelacion) {
        this.prelacionesParaResultados.remove(prelacion);
        prelacion.setResultado(null);
        return this;
    }

    public void setPrelacionesParaResultados(Set<Prelacion> prelacions) {
        this.prelacionesParaResultados = prelacions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resultado resultado = (Resultado) o;
        if (resultado.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resultado.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Resultado{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
