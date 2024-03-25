package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Area.
 */
@Entity
@Table(name = "area")
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "areaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "areaGenerator", sequenceName="area_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @Size(max = 100)
    @Column(name = "nombre_largo", length = 100)
    private String nombreLargo;
    
    @Size(max = 100)
    @Column(name = "LEYENDA_TIPO_SERVICO", length = 100)
    private String leyendaTipoServicio;

    @ManyToOne
    private TipoFolio tipoFolio;
    
    @Column(name = "VISIBLE_PRESENCIAL")
    private Boolean visiblePresencial;
    
    @OneToMany(mappedBy = "area")
    @JsonIgnore
    private Set<AreaClasifActo> areaClasifActosParaAreas = new HashSet<>();

    @OneToMany(mappedBy = "area")
    @JsonIgnore
    private Set<Servicio> serviciosParaAreas = new HashSet<>();

    @OneToMany(mappedBy = "area")
    @JsonIgnore
    private Set<Telefono> telefonosParaAreas = new HashSet<>();

    @OneToMany(mappedBy = "area")
    @JsonIgnore
    private Set<UsuarioArea> usuarioAreasParaAreas = new HashSet<>();
    
    @OneToMany(mappedBy = "area")
    @JsonIgnore
    private Set<AreaRol> areasRolesParaAreas = new HashSet<>();

    @OneToMany(mappedBy = "area")
    @JsonIgnore
    private Set<TipoUsuarioArea> tipoUsuarioAreasParaArea = new HashSet<>();
 
	public Set<TipoUsuarioArea> getTipoUsuarioAreasParaArea() {
		return tipoUsuarioAreasParaArea;
	}

	public void setTipoUsuarioAreasParaArea(Set<TipoUsuarioArea> tipoUsuarioAreasParaArea) {
		this.tipoUsuarioAreasParaArea = tipoUsuarioAreasParaArea;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Area nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreLargo() {
        return nombreLargo;
    }

    public Area nombreLargo(String nombreLargo) {
        this.nombreLargo = nombreLargo;
        return this;
    }

    public void setNombreLargo(String nombreLargo) {
        this.nombreLargo = nombreLargo;
    }

    public Set<AreaClasifActo> getAreaClasifActosParaAreas() {
        return areaClasifActosParaAreas;
    }

    public Area areaClasifActosParaAreas(Set<AreaClasifActo> areaClasifActos) {
        this.areaClasifActosParaAreas = areaClasifActos;
        return this;
    }

    public Area addAreaClasifActosParaArea(AreaClasifActo areaClasifActo) {
        this.areaClasifActosParaAreas.add(areaClasifActo);
        areaClasifActo.setArea(this);
        return this;
    }

    public Area removeAreaClasifActosParaArea(AreaClasifActo areaClasifActo) {
        this.areaClasifActosParaAreas.remove(areaClasifActo);
        areaClasifActo.setArea(null);
        return this;
    }

    public void setAreaClasifActosParaAreas(Set<AreaClasifActo> areaClasifActos) {
        this.areaClasifActosParaAreas = areaClasifActos;
    }

    public Set<Servicio> getServiciosParaAreas() {
        return serviciosParaAreas;
    }

    public Area serviciosParaAreas(Set<Servicio> servicios) {
        this.serviciosParaAreas = servicios;
        return this;
    }

    public Area addServiciosParaArea(Servicio servicio) {
        this.serviciosParaAreas.add(servicio);
        servicio.setArea(this);
        return this;
    }

    public Area removeServiciosParaArea(Servicio servicio) {
        this.serviciosParaAreas.remove(servicio);
        servicio.setArea(null);
        return this;
    }

    public void setServiciosParaAreas(Set<Servicio> servicios) {
        this.serviciosParaAreas = servicios;
    }

    public Set<Telefono> getTelefonosParaAreas() {
        return telefonosParaAreas;
    }

    public Area telefonosParaAreas(Set<Telefono> telefonos) {
        this.telefonosParaAreas = telefonos;
        return this;
    }

    public Area addTelefonosParaArea(Telefono telefono) {
        this.telefonosParaAreas.add(telefono);
        telefono.setArea(this);
        return this;
    }

    public Area removeTelefonosParaArea(Telefono telefono) {
        this.telefonosParaAreas.remove(telefono);
        telefono.setArea(null);
        return this;
    }

    public void setTelefonosParaAreas(Set<Telefono> telefonos) {
        this.telefonosParaAreas = telefonos;
    }

    public Set<UsuarioArea> getUsuarioAreasParaAreas() {
        return usuarioAreasParaAreas;
    }

    public Area usuarioAreasParaAreas(Set<UsuarioArea> usuarioAreas) {
        this.usuarioAreasParaAreas = usuarioAreas;
        return this;
    }

    public Area areasRolesParaAreas(AreaRol areaRol) {
        this.areasRolesParaAreas.add(areaRol);
        areaRol.setArea(this);
        return this;
    }

    public Area removeAreasRolesParaAreas(AreaRol areaRol) {
        this.areasRolesParaAreas.remove(areaRol);
        areaRol.setArea(null);
        return this;
    }

    public Set<AreaRol> getAreasRolesParaAreas() {
        return this.areasRolesParaAreas; 
    }
    
    
    public Area areasRolesParaAreas(Set<UsuarioArea> usuarioAreas) {
        this.usuarioAreasParaAreas = usuarioAreas;
        return this;
    }

    public Area addUsuarioAreasParaArea(UsuarioArea usuarioArea) {
        this.usuarioAreasParaAreas.add(usuarioArea);
        usuarioArea.setArea(this);
        return this;
    }

    public Area removeUsuarioAreasParaArea(UsuarioArea usuarioArea) {
        this.usuarioAreasParaAreas.remove(usuarioArea);
        usuarioArea.setArea(null);
        return this;
    }

    public void setUsuarioAreasParaAreas(Set<UsuarioArea> usuarioAreas) {
        this.usuarioAreasParaAreas = usuarioAreas;
    }
    
    
    
    public TipoFolio getTipoFolio() {
		return tipoFolio;
	}

	public void setTipoFolio(TipoFolio tipoFolio) {
		this.tipoFolio = tipoFolio;
	}

	public Boolean getVisiblePresencial() {
		return visiblePresencial;
	}

	public void setVisiblePresencial(Boolean visiblePresencial) {
		this.visiblePresencial = visiblePresencial;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Area area = (Area) o;
        if (area.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), area.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Area{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", nombreLargo='" + getNombreLargo() + "'" +
            "}";
    }

	public String getLeyendaTipoServicio() {
		return leyendaTipoServicio;
	}

	public void setLeyendaTipoServicio(String leyendaTipoServicio) {
		this.leyendaTipoServicio = leyendaTipoServicio;
	}
}
