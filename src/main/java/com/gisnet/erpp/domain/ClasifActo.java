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
 * A ClasifActo.
 */
@Entity
@Table(name = "clasif_acto")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClasifActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clasifActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "clasifActoGenerator", sequenceName="clasif_acto_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne
    private TipoFolio tipoFolio;


    @OneToMany(mappedBy = "clasifActo")
    private Set<AreaClasifActo> areaClasifActosParaClasifActos = new HashSet<>();

    @OneToMany(mappedBy = "clasifActo")
    @JsonIgnore
    private Set<ReqClasifActo> reqClasifActosParaClasifActos = new HashSet<>();

    @OneToMany(mappedBy = "clasifActo")
    @JsonIgnore
    private Set<ServicioCosto> servicioCostosParaClasifActos = new HashSet<>();

    @OneToMany(mappedBy = "clasifActo")
    @JsonIgnore
    private Set<TipoActo> tipoActosParaClasifActos = new HashSet<>();

    @OneToMany(mappedBy = "clasifActo")
    @JsonIgnore
    private Set<Servicio> serviciosParaClasifActos = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public ClasifActo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public ClasifActo activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<AreaClasifActo> getAreaClasifActosParaClasifActos() {
        return areaClasifActosParaClasifActos;
    }

    public ClasifActo areaClasifActosParaClasifActos(Set<AreaClasifActo> areaClasifActos) {
        this.areaClasifActosParaClasifActos = areaClasifActos;
        return this;
    }

    public ClasifActo addAreaClasifActosParaClasifActo(AreaClasifActo areaClasifActo) {
        this.areaClasifActosParaClasifActos.add(areaClasifActo);
        areaClasifActo.setClasifActo(this);
        return this;
    }

    public ClasifActo removeAreaClasifActosParaClasifActo(AreaClasifActo areaClasifActo) {
        this.areaClasifActosParaClasifActos.remove(areaClasifActo);
        areaClasifActo.setClasifActo(null);
        return this;
    }

    public void setAreaClasifActosParaClasifActos(Set<AreaClasifActo> areaClasifActos) {
        this.areaClasifActosParaClasifActos = areaClasifActos;
    }

    public Set<ReqClasifActo> getReqClasifActosParaClasifActos() {
        return reqClasifActosParaClasifActos;
    }

    public ClasifActo reqClasifActosParaClasifActos(Set<ReqClasifActo> reqClasifActos) {
        this.reqClasifActosParaClasifActos = reqClasifActos;
        return this;
    }

    public ClasifActo addReqClasifActosParaClasifActo(ReqClasifActo reqClasifActo) {
        this.reqClasifActosParaClasifActos.add(reqClasifActo);
        reqClasifActo.setClasifActo(this);
        return this;
    }

    public ClasifActo removeReqClasifActosParaClasifActo(ReqClasifActo reqClasifActo) {
        this.reqClasifActosParaClasifActos.remove(reqClasifActo);
        reqClasifActo.setClasifActo(null);
        return this;
    }

    public void setReqClasifActosParaClasifActos(Set<ReqClasifActo> reqClasifActos) {
        this.reqClasifActosParaClasifActos = reqClasifActos;
    }

    public Set<ServicioCosto> getServicioCostosParaClasifActos() {
        return servicioCostosParaClasifActos;
    }

    public ClasifActo servicioCostosParaClasifActos(Set<ServicioCosto> servicioCostos) {
        this.servicioCostosParaClasifActos = servicioCostos;
        return this;
    }

    public ClasifActo addServicioCostosParaClasifActo(ServicioCosto servicioCosto) {
        this.servicioCostosParaClasifActos.add(servicioCosto);
        servicioCosto.setClasifActo(this);
        return this;
    }

    public ClasifActo removeServicioCostosParaClasifActo(ServicioCosto servicioCosto) {
        this.servicioCostosParaClasifActos.remove(servicioCosto);
        servicioCosto.setClasifActo(null);
        return this;
    }

    public void setServicioCostosParaClasifActos(Set<ServicioCosto> servicioCostos) {
        this.servicioCostosParaClasifActos = servicioCostos;
    }

    public Set<TipoActo> getTipoActosParaClasifActos() {
        return tipoActosParaClasifActos;
    }

    public ClasifActo tipoActosParaClasifActos(Set<TipoActo> tipoActos) {
        this.tipoActosParaClasifActos = tipoActos;
        return this;
    }

    public ClasifActo addTipoActosParaClasifActo(TipoActo tipoActo) {
        this.tipoActosParaClasifActos.add(tipoActo);
        tipoActo.setClasifActo(this);
        return this;
    }

    public ClasifActo removeTipoActosParaClasifActo(TipoActo tipoActo) {
        this.tipoActosParaClasifActos.remove(tipoActo);
        tipoActo.setClasifActo(null);
        return this;
    }

    public void setTipoActosParaClasifActos(Set<TipoActo> tipoActos) {
        this.tipoActosParaClasifActos = tipoActos;
    }

    public Set<Servicio> getServiciosParaClasifActos() {
        return serviciosParaClasifActos;
    }

    public ClasifActo serviciosParaClasifActos(Set<Servicio> servicios) {
        this.serviciosParaClasifActos = servicios;
        return this;
    }

    public ClasifActo addServiciosParaClasifActos(Servicio servicio) {
        this.serviciosParaClasifActos.add(servicio);
        servicio.setClasifActo(this);
        return this;
    }

    public ClasifActo removeServiciosParaClasifActos(Servicio servicio) {
        this.serviciosParaClasifActos.remove(servicio);
        servicio.setClasifActo(null);
        return this;
    }

    public void setServiciosParaClasifActos(Set<Servicio> servicios) {
        this.serviciosParaClasifActos = servicios;
    }

    public TipoFolio getTipoFolio(){
    	return tipoFolio;
    }

    public void setTipoFolio(TipoFolio tipoFolio){
    	this.tipoFolio = tipoFolio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClasifActo clasifActo = (ClasifActo) o;
        if (clasifActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clasifActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClasifActo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
