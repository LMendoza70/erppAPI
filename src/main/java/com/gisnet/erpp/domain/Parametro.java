package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Parametro.
 */
@Entity
@Table(name = "parametro")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Parametro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parametroGenerator")
    @SequenceGenerator(allocationSize = 1, name = "parametroGenerator", sequenceName="parametro_seq")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "cve", length = 100, nullable = false)
    private String cve;

    @Size(max = 300)
    @Column(name = "valor", length = 300)
    private String valor;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne
    private Municipio municipio;

    @ManyToOne
    private Oficina oficina;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCve() {
        return cve;
    }

    public Parametro cve(String cve) {
        this.cve = cve;
        return this;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public String getValor() {
        return valor;
    }

    public Parametro valor(String valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Parametro activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public Parametro municipio(Municipio municipio) {
        this.municipio = municipio;
        return this;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public Parametro oficina(Oficina oficina) {
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Parametro parametro = (Parametro) o;
        if (parametro.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), parametro.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Parametro{" +
            "id=" + getId() +
            ", cve='" + getCve() + "'" +
            ", valor='" + getValor() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
