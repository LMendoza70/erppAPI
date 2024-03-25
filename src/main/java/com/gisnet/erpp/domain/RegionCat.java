package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A RegionCat.
 */
@Entity
@Table(name = "region_cat")
public class RegionCat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regionCatGenerator")
    @SequenceGenerator(allocationSize = 1, name = "regionCatGenerator", sequenceName="region_cat_seq")
    private Long id;

    @NotNull
    @Size(max = 3)
    @Column(name = "num_region_cat", length = 3, nullable = false)
    private String numRegionCat;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "regionCat")
    @JsonIgnore
    private Set<Municipio> municipiosParaRegionCats = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Estado estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumRegionCat() {
        return numRegionCat;
    }

    public RegionCat numRegionCat(String numRegionCat) {
        this.numRegionCat = numRegionCat;
        return this;
    }

    public void setNumRegionCat(String numRegionCat) {
        this.numRegionCat = numRegionCat;
    }

    public Boolean isActivo() {
        return activo;
    }

    public RegionCat activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Municipio> getMunicipiosParaRegionCats() {
        return municipiosParaRegionCats;
    }

    public RegionCat municipiosParaRegionCats(Set<Municipio> municipios) {
        this.municipiosParaRegionCats = municipios;
        return this;
    }

    public RegionCat addMunicipiosParaRegionCat(Municipio municipio) {
        this.municipiosParaRegionCats.add(municipio);
        municipio.setRegionCat(this);
        return this;
    }

    public RegionCat removeMunicipiosParaRegionCat(Municipio municipio) {
        this.municipiosParaRegionCats.remove(municipio);
        municipio.setRegionCat(null);
        return this;
    }

    public void setMunicipiosParaRegionCats(Set<Municipio> municipios) {
        this.municipiosParaRegionCats = municipios;
    }

    public Estado getEstado() {
        return estado;
    }

    public RegionCat estado(Estado estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegionCat regionCat = (RegionCat) o;
        if (regionCat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), regionCat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RegionCat{" +
            "id=" + getId() +
            ", numRegionCat='" + getNumRegionCat() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
