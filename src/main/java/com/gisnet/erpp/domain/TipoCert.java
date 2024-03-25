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
 * A TipoCert.
 */
@Entity
@Table(name = "tipo_cert")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoCert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoCertGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoCertGenerator", sequenceName="tipo_cert_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "tipoCert")
    @JsonIgnore
    private Set<Documento> documentosParaTipoCerts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoCert nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Documento> getDocumentosParaTipoCerts() {
        return documentosParaTipoCerts;
    }

    public TipoCert documentosParaTipoCerts(Set<Documento> documentos) {
        this.documentosParaTipoCerts = documentos;
        return this;
    }

    public TipoCert addDocumentosParaTipoCert(Documento documento) {
        this.documentosParaTipoCerts.add(documento);
        documento.setTipoCert(this);
        return this;
    }

    public TipoCert removeDocumentosParaTipoCert(Documento documento) {
        this.documentosParaTipoCerts.remove(documento);
        documento.setTipoCert(null);
        return this;
    }

    public void setDocumentosParaTipoCerts(Set<Documento> documentos) {
        this.documentosParaTipoCerts = documentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoCert tipoCert = (TipoCert) o;
        if (tipoCert.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoCert.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoCert{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
