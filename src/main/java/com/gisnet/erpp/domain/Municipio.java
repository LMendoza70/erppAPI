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
 * A Municipio.
 */
@Entity
@Table(name = "municipio")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "municipioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "municipioGenerator", sequenceName="municipio_seq")
    private Long id;

    @Size(max = 3)
    @Column(name = "num_municipio", length = 3)
    private String numMunicipio;

    @Size(max = 200)
    @Column(name = "nombre", length = 200)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "municipio")
    @JsonIgnore
    private Set<Asentamiento> asentamientosParaMunicipios = new HashSet<>();

    @OneToMany(mappedBy = "municipio")
    @JsonIgnore
    private Set<Documento> documentosParaMunicipios = new HashSet<>();

    @OneToMany(mappedBy = "municipio")
    @JsonIgnore
    private Set<Juez> juezesParaMunicipios = new HashSet<>();

    @OneToMany(mappedBy = "municipio")
    @JsonIgnore
    private Set<Notario> notariosParaMunicipios = new HashSet<>();

    @OneToMany(mappedBy = "municipio")
    @JsonIgnore
    private Set<Parametro> parametrosParaMunicipios = new HashSet<>();

    @OneToMany(mappedBy = "municipio")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaMunicipios = new HashSet<>();

    @OneToMany(mappedBy = "municipio")
    @JsonIgnore
    private Set<Vialidad> vialidadesParaMunicipios = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Estado estado;

    @ManyToOne
    private Oficina oficina;

    @ManyToOne
    private RegionCat regionCat;
    
    @Column(name = "clave_catastral")
    private String claveCatastral;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumMunicipio() {
        return numMunicipio;
    }

    public Municipio numMunicipio(String numMunicipio) {
        this.numMunicipio = numMunicipio;
        return this;
    }

    public void setNumMunicipio(String numMunicipio) {
        this.numMunicipio = numMunicipio;
    }

    public String getNombre() {
        return nombre;
    }

    public Municipio nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Municipio activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Asentamiento> getAsentamientosParaMunicipios() {
        return asentamientosParaMunicipios;
    }

    public Municipio asentamientosParaMunicipios(Set<Asentamiento> asentamientos) {
        this.asentamientosParaMunicipios = asentamientos;
        return this;
    }

    public Municipio addAsentamientosParaMunicipio(Asentamiento asentamiento) {
        this.asentamientosParaMunicipios.add(asentamiento);
        asentamiento.setMunicipio(this);
        return this;
    }

    public Municipio removeAsentamientosParaMunicipio(Asentamiento asentamiento) {
        this.asentamientosParaMunicipios.remove(asentamiento);
        asentamiento.setMunicipio(null);
        return this;
    }

    public void setAsentamientosParaMunicipios(Set<Asentamiento> asentamientos) {
        this.asentamientosParaMunicipios = asentamientos;
    }

    public Set<Documento> getDocumentosParaMunicipios() {
        return documentosParaMunicipios;
    }

    public Municipio documentosParaMunicipios(Set<Documento> documentos) {
        this.documentosParaMunicipios = documentos;
        return this;
    }

    public Municipio addDocumentosParaMunicipio(Documento documento) {
        this.documentosParaMunicipios.add(documento);
        documento.setMunicipio(this);
        return this;
    }

    public Municipio removeDocumentosParaMunicipio(Documento documento) {
        this.documentosParaMunicipios.remove(documento);
        documento.setMunicipio(null);
        return this;
    }

    public void setDocumentosParaMunicipios(Set<Documento> documentos) {
        this.documentosParaMunicipios = documentos;
    }

    public Set<Juez> getJuezesParaMunicipios() {
        return juezesParaMunicipios;
    }

    public Municipio juezesParaMunicipios(Set<Juez> juezs) {
        this.juezesParaMunicipios = juezs;
        return this;
    }

    public Municipio addJuezesParaMunicipio(Juez juez) {
        this.juezesParaMunicipios.add(juez);
        juez.setMunicipio(this);
        return this;
    }

    public Municipio removeJuezesParaMunicipio(Juez juez) {
        this.juezesParaMunicipios.remove(juez);
        juez.setMunicipio(null);
        return this;
    }

    public void setJuezesParaMunicipios(Set<Juez> juezs) {
        this.juezesParaMunicipios = juezs;
    }

    public Set<Notario> getNotariosParaMunicipios() {
        return notariosParaMunicipios;
    }

    public Municipio notariosParaMunicipios(Set<Notario> notarios) {
        this.notariosParaMunicipios = notarios;
        return this;
    }

    public Municipio addNotariosParaMunicipio(Notario notario) {
        this.notariosParaMunicipios.add(notario);
        notario.setMunicipio(this);
        return this;
    }

    public Municipio removeNotariosParaMunicipio(Notario notario) {
        this.notariosParaMunicipios.remove(notario);
        notario.setMunicipio(null);
        return this;
    }

    public void setNotariosParaMunicipios(Set<Notario> notarios) {
        this.notariosParaMunicipios = notarios;
    }

    public Set<Parametro> getParametrosParaMunicipios() {
        return parametrosParaMunicipios;
    }

    public Municipio parametrosParaMunicipios(Set<Parametro> parametros) {
        this.parametrosParaMunicipios = parametros;
        return this;
    }

    public Municipio addParametrosParaMunicipio(Parametro parametro) {
        this.parametrosParaMunicipios.add(parametro);
        parametro.setMunicipio(this);
        return this;
    }

    public Municipio removeParametrosParaMunicipio(Parametro parametro) {
        this.parametrosParaMunicipios.remove(parametro);
        parametro.setMunicipio(null);
        return this;
    }

    public void setParametrosParaMunicipios(Set<Parametro> parametros) {
        this.parametrosParaMunicipios = parametros;
    }

    public Set<Prelacion> getPrelacionesParaMunicipios() {
        return prelacionesParaMunicipios;
    }

    public Municipio prelacionesParaMunicipios(Set<Prelacion> prelacions) {
        this.prelacionesParaMunicipios = prelacions;
        return this;
    }

    public Municipio addPrelacionesParaMunicipio(Prelacion prelacion) {
        this.prelacionesParaMunicipios.add(prelacion);
        prelacion.setMunicipio(this);
        return this;
    }

    public Municipio removePrelacionesParaMunicipio(Prelacion prelacion) {
        this.prelacionesParaMunicipios.remove(prelacion);
        prelacion.setMunicipio(null);
        return this;
    }

    public void setPrelacionesParaMunicipios(Set<Prelacion> prelacions) {
        this.prelacionesParaMunicipios = prelacions;
    }

    public Set<Vialidad> getVialidadesParaMunicipios() {
        return vialidadesParaMunicipios;
    }

    public Municipio vialidadesParaMunicipios(Set<Vialidad> vialidads) {
        this.vialidadesParaMunicipios = vialidads;
        return this;
    }

    public Municipio addVialidadesParaMunicipio(Vialidad vialidad) {
        this.vialidadesParaMunicipios.add(vialidad);
        vialidad.setMunicipio(this);
        return this;
    }

    public Municipio removeVialidadesParaMunicipio(Vialidad vialidad) {
        this.vialidadesParaMunicipios.remove(vialidad);
        vialidad.setMunicipio(null);
        return this;
    }

    public void setVialidadesParaMunicipios(Set<Vialidad> vialidads) {
        this.vialidadesParaMunicipios = vialidads;
    }

    public Estado getEstado() {
        return estado;
    }

    public Municipio estado(Estado estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public Municipio oficina(Oficina oficina) {
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public RegionCat getRegionCat() {
        return regionCat;
    }

    public Municipio regionCat(RegionCat regionCat) {
        this.regionCat = regionCat;
        return this;
    }

    public void setRegionCat(RegionCat regionCat) {
        this.regionCat = regionCat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Municipio municipio = (Municipio) o;
        if (municipio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), municipio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Municipio{" +
            "id=" + getId() +
            ", numMunicipio='" + getNumMunicipio() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }

	public String getClaveCatastral() {
		return claveCatastral;
	}

	public void setClaveCatastral(String claveCatastral) {
		this.claveCatastral = claveCatastral;
	}
}
