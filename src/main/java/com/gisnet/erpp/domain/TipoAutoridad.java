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
 * A TipoAutoridad.
 */
@Entity
@Table(name = "tipo_autoridad")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoAutoridad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoAutoridadGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoAutoridadGenerator", sequenceName="tipo_autoridad_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "tipoAutoridad")
    @JsonIgnore
    private Set<Documento> documentosParaTipoAutoridads = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoAutoridad nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Documento> getDocumentosParaTipoAutoridads() {
        return documentosParaTipoAutoridads;
    }

    public TipoAutoridad documentosParaTipoAutoridads(Set<Documento> documentos) {
        this.documentosParaTipoAutoridads = documentos;
        return this;
    }

    public TipoAutoridad addDocumentosParaTipoAutoridad(Documento documento) {
        this.documentosParaTipoAutoridads.add(documento);
        documento.setTipoAutoridad(this);
        return this;
    }

    public TipoAutoridad removeDocumentosParaTipoAutoridad(Documento documento) {
        this.documentosParaTipoAutoridads.remove(documento);
        documento.setTipoAutoridad(null);
        return this;
    }

    public void setDocumentosParaTipoAutoridads(Set<Documento> documentos) {
        this.documentosParaTipoAutoridads = documentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoAutoridad tipoAutoridad = (TipoAutoridad) o;
        if (tipoAutoridad.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoAutoridad.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoAutoridad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
