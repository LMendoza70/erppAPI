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
 * A Estado.
 */
@Entity
@Table(name = "estado")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Estado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estadoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "estadoGenerator", sequenceName="estado_seq")
    private Long id;

    @NotNull
    @Size(max = 25)
    @Column(name = "nombre", length = 25, nullable = false)
    private String nombre;

    @Size(max = 2)
    @Column(name = "num_estado", length = 2)
    private String numEstado;

    @OneToMany(mappedBy = "estado")
    @JsonIgnore
    private Set<Municipio> municipiosParaEstados = new HashSet<>();

    @OneToMany(mappedBy = "estado")
    @JsonIgnore
    private Set<Oficina> oficinasParaEstados = new HashSet<>();

    @OneToMany(mappedBy = "estado")
    @JsonIgnore
    private Set<RegionCat> regionCatsParaEstados = new HashSet<>();
    
    @OneToMany(mappedBy = "estado")
    @JsonIgnore
    private Set<Dependencia> dependenciasParaEstados = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Estado nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumEstado() {
        return numEstado;
    }

    public Estado numEstado(String numEstado) {
        this.numEstado = numEstado;
        return this;
    }

    public void setNumEstado(String numEstado) {
        this.numEstado = numEstado;
    }

    public Set<Municipio> getMunicipiosParaEstados() {
        return municipiosParaEstados;
    }

    public Estado municipiosParaEstados(Set<Municipio> municipios) {
        this.municipiosParaEstados = municipios;
        return this;
    }

    public Estado addMunicipiosParaEstado(Municipio municipio) {
        this.municipiosParaEstados.add(municipio);
        municipio.setEstado(this);
        return this;
    }

    public Estado removeMunicipiosParaEstado(Municipio municipio) {
        this.municipiosParaEstados.remove(municipio);
        municipio.setEstado(null);
        return this;
    }

    public void setMunicipiosParaEstados(Set<Municipio> municipios) {
        this.municipiosParaEstados = municipios;
    }

    public Set<Oficina> getOficinasParaEstados() {
        return oficinasParaEstados;
    }

    public Estado oficinasParaEstados(Set<Oficina> oficinas) {
        this.oficinasParaEstados = oficinas;
        return this;
    }

    public Estado addOficinasParaEstado(Oficina oficina) {
        this.oficinasParaEstados.add(oficina);
        oficina.setEstado(this);
        return this;
    }

    public Estado removeOficinasParaEstado(Oficina oficina) {
        this.oficinasParaEstados.remove(oficina);
        oficina.setEstado(null);
        return this;
    }

    public void setOficinasParaEstados(Set<Oficina> oficinas) {
        this.oficinasParaEstados = oficinas;
    }

    public Set<RegionCat> getRegionCatsParaEstados() {
        return regionCatsParaEstados;
    }

    public Estado regionCatsParaEstados(Set<RegionCat> regionCats) {
        this.regionCatsParaEstados = regionCats;
        return this;
    }

    public Estado addRegionCatsParaEstado(RegionCat regionCat) {
        this.regionCatsParaEstados.add(regionCat);
        regionCat.setEstado(this);
        return this;
    }
    
    public Estado removeRegionCatsParaEstado(RegionCat regionCat) {
        this.regionCatsParaEstados.remove(regionCat);
        regionCat.setEstado(null);
        return this;
    }

    public void setRegionCatsParaEstados(Set<RegionCat> regionCats) {
        this.regionCatsParaEstados = regionCats;
    }
    
    public Set<Dependencia> getDependenciasParaEstados() {
        return dependenciasParaEstados;
    }

    public Estado dependenciasParaEstados(Set<Dependencia> dependencias) {
        this.dependenciasParaEstados = dependencias;
        return this;
    }

    public Estado addDependenciaParaEstado(Dependencia dependencia) {
        this.dependenciasParaEstados.add(dependencia);
        dependencia.setEstado(this);
        return this;
    }

    public Estado removeDependenciaParaEstado(Dependencia dependencia) {
        this.dependenciasParaEstados.remove(dependencia);
        dependencia.setEstado(null);
        return this;
    }

    public void setDependenciasParaEstados(Set<Dependencia> dependencias) {
        this.dependenciasParaEstados = dependencias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Estado estado = (Estado) o;
        if (estado.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), estado.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Estado{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", numEstado='" + getNumEstado() + "'" +
            "}";
    }
}
