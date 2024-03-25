package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StatusActo.
 */
@Entity
@Table(name = "nombre_libro")
public class NombreLibro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nombreLibroGenerator")
    @SequenceGenerator(allocationSize = 1, name = "nombreLibroGenerator", sequenceName="nombre_libro_seq")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "nombreLibro")
    @JsonIgnore
    private Set<Libro> librosParaNombreLibro = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public NombreLibro nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Libro> getPrelacionAnteCapHistParaStatusAntecedente() {
		return librosParaNombreLibro;
	}

	public void setLibrosParaNombreLibro(Set<Libro> librosParaNombreLibro) {
		this.librosParaNombreLibro = librosParaNombreLibro;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NombreLibro statusActo = (NombreLibro) o;
        if (statusActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), statusActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StatusAntecedente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
