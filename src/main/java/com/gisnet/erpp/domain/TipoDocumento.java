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
 * A TipoDocumento.
 */
@Entity
@Table(name = "tipo_documento")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoDocumentoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoDocumentoGenerator", sequenceName="tipo_documento_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "tipoDocumento")
    @JsonIgnore
    private Set<Documento> documentosParaTipoDocumentos = new HashSet<>();

    @OneToMany(mappedBy = "tipoDocumento")
    @JsonIgnore
    private Set<TipoDocTipoActo> tipoDocTipoActosParaTipoDocumentos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoDocumento nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public TipoDocumento activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Documento> getDocumentosParaTipoDocumentos() {
        return documentosParaTipoDocumentos;
    }

    public TipoDocumento documentosParaTipoDocumentos(Set<Documento> documentos) {
        this.documentosParaTipoDocumentos = documentos;
        return this;
    }

    public TipoDocumento addDocumentosParaTipoDocumento(Documento documento) {
        this.documentosParaTipoDocumentos.add(documento);
        documento.setTipoDocumento(this);
        return this;
    }

    public TipoDocumento removeDocumentosParaTipoDocumento(Documento documento) {
        this.documentosParaTipoDocumentos.remove(documento);
        documento.setTipoDocumento(null);
        return this;
    }

    public void setDocumentosParaTipoDocumentos(Set<Documento> documentos) {
        this.documentosParaTipoDocumentos = documentos;
    }

    public Set<TipoDocTipoActo> getTipoDocTipoActosParaTipoDocumentos() {
        return tipoDocTipoActosParaTipoDocumentos;
    }

    public TipoDocumento tipoDocTipoActosParaTipoDocumentos(Set<TipoDocTipoActo> tipoDocTipoActos) {
        this.tipoDocTipoActosParaTipoDocumentos = tipoDocTipoActos;
        return this;
    }

    public TipoDocumento addTipoDocTipoActosParaTipoDocumento(TipoDocTipoActo tipoDocTipoActo) {
        this.tipoDocTipoActosParaTipoDocumentos.add(tipoDocTipoActo);
        tipoDocTipoActo.setTipoDocumento(this);
        return this;
    }

    public TipoDocumento removeTipoDocTipoActosParaTipoDocumento(TipoDocTipoActo tipoDocTipoActo) {
        this.tipoDocTipoActosParaTipoDocumentos.remove(tipoDocTipoActo);
        tipoDocTipoActo.setTipoDocumento(null);
        return this;
    }

    public void setTipoDocTipoActosParaTipoDocumentos(Set<TipoDocTipoActo> tipoDocTipoActos) {
        this.tipoDocTipoActosParaTipoDocumentos = tipoDocTipoActos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoDocumento tipoDocumento = (TipoDocumento) o;
        if (tipoDocumento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoDocumento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoDocumento{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
