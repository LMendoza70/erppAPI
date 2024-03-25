package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Servicio.
 */
@Entity
@Table(name = "servicio")
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servicioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "servicioGenerator", sequenceName="servicio_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "acto")
    private Boolean acto;

    @Column(name = "ventanilla_elec")
    private Boolean ventanillaElec;
    
    private Integer ponderacion;
    
    @Column(name = "dias_tramitar")
    private Integer diasTramitar;


    @OneToMany(mappedBy = "servicio")
    @JsonIgnore
    private Set<ReciboServicio> reciboServiciosParaServicios = new HashSet<>();

    @OneToMany(mappedBy = "servicio")
    @JsonIgnore
    private Set<ServicioCosto> servicioCostosParaServicios = new HashSet<>();

    @OneToMany(mappedBy = "servicio")
    @JsonIgnore
    private Set<SubServicio> subServiciosParaServicios = new HashSet<>();
    
    @OneToMany(mappedBy = "servicio")
    @JsonIgnore
    private Set<PrelacionServicio> prelacionServiciosParaServicios = new HashSet<>();

    @ManyToOne
    private TipoActo tipoActo;
    
    @ManyToOne
    private ClasifActo clasifActo;

    @ManyToOne(optional = false)
    @NotNull
    private Area area;
    
    @ManyToOne
    private ConceptoPago conceptoPago;
    
    @OneToMany(mappedBy = "servicio")
    @JsonIgnore
    private Set<PrelacionServicio> prelacionServicioParaServicios  = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Servicio nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Integer getPonderacion(){
        return ponderacion;
    }
    
    public void setPonderacion(Integer ponderacion){
        this.ponderacion = ponderacion;
    }
    
    public Integer getDiasTramitar(){
        return diasTramitar;
    }
    
    public void setDiasTramitar(Integer diasTramitar){
        this.diasTramitar = diasTramitar;
    }

    public Boolean isActo() {
        return acto;
    }

    public Servicio acto(Boolean acto) {
        this.acto = acto;
        return this;
    }

    public void setActo(Boolean acto) {
        this.acto = acto;
    }

    public Boolean isVentanillaElec() {
        return ventanillaElec;
    }

    public Servicio ventanillaElec(Boolean ventanillaElec) {
        this.ventanillaElec = ventanillaElec;
        return this;
    }

    public void setVentanillaElec(Boolean ventanillaElec) {
        this.ventanillaElec = ventanillaElec;
    }

    public Set<ReciboServicio> getReciboServiciosParaServicios() {
        return reciboServiciosParaServicios;
    }

    public Servicio reciboServiciosParaServicios(Set<ReciboServicio> reciboServicios) {
        this.reciboServiciosParaServicios = reciboServicios;
        return this;
    }

    public Servicio addReciboServiciosParaServicio(ReciboServicio reciboServicio) {
        this.reciboServiciosParaServicios.add(reciboServicio);
        reciboServicio.setServicio(this);
        return this;
    }

    public Servicio removeReciboServiciosParaServicio(ReciboServicio reciboServicio) {
        this.reciboServiciosParaServicios.remove(reciboServicio);
        reciboServicio.setServicio(null);
        return this;
    }

    public void setReciboServiciosParaServicios(Set<ReciboServicio> reciboServicios) {
        this.reciboServiciosParaServicios = reciboServicios;
    }

    public Set<ServicioCosto> getServicioCostosParaServicios() {
        return servicioCostosParaServicios;
    }

    public Servicio servicioCostosParaServicios(Set<ServicioCosto> servicioCostos) {
        this.servicioCostosParaServicios = servicioCostos;
        return this;
    }

    public Servicio addServicioCostosParaServicio(ServicioCosto servicioCosto) {
        this.servicioCostosParaServicios.add(servicioCosto);
        servicioCosto.setServicio(this);
        return this;
    }

    public Servicio removeServicioCostosParaServicio(ServicioCosto servicioCosto) {
        this.servicioCostosParaServicios.remove(servicioCosto);
        servicioCosto.setServicio(null);
        return this;
    }

    public void setServicioCostosParaServicios(Set<ServicioCosto> servicioCostos) {
        this.servicioCostosParaServicios = servicioCostos;
    }

    public Set<SubServicio> getSubServiciosParaServicios() {
        return subServiciosParaServicios;
    }

    public Servicio subServiciosParaServicios(Set<SubServicio> subServicios) {
        this.subServiciosParaServicios = subServicios;
        return this;
    }

    public Servicio addSubServiciosParaServicio(SubServicio subServicio) {
        this.subServiciosParaServicios.add(subServicio);
        subServicio.setServicio(this);
        return this;
    }

    public Servicio removeSubServiciosParaServicio(SubServicio subServicio) {
        this.subServiciosParaServicios.remove(subServicio);
        subServicio.setServicio(null);
        return this;
    }

    public void setSubServiciosParaServicios(Set<SubServicio> subServicios) {
        this.subServiciosParaServicios = subServicios;
    }
    
    public Set<PrelacionServicio> getPrelacionServiciosParaServicios() {
		return prelacionServiciosParaServicios;
	}

	public void setPrelacionServiciosParaServicios(Set<PrelacionServicio> prelacionServiciosParaServicios) {
		this.prelacionServiciosParaServicios = prelacionServiciosParaServicios;
	}

	public TipoActo getTipoActo() {
        return tipoActo;
    }

    public Servicio tipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
        return this;
    }

    public void setTipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
    }
    
    public ClasifActo getClasifActo() {
        return clasifActo;
    }

    public Servicio clasifActo(ClasifActo clasifActo) {
        this.clasifActo = clasifActo;
        return this;
    }

    public void setClasifActo(ClasifActo clasifActo) {
        this.clasifActo = clasifActo;
    }

    public Area getArea() {
        return area;
    }

    public Servicio area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }
    
    public ConceptoPago getConceptoPago() {
        return conceptoPago;
    }

    public Servicio conceptoPago(ConceptoPago conceptoPago) {
        this.conceptoPago = conceptoPago;
        return this;
    }

    public void setConceptoPago(ConceptoPago conceptoPago) {
        this.conceptoPago = conceptoPago;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Servicio servicio = (Servicio) o;
        if (servicio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), servicio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Servicio{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", acto='" + isActo() + "'" +
            ", ventanillaElec='" + isVentanillaElec() + "'" +
            "}";
    }

	public Set<PrelacionServicio> getPrelacionServicioParaServicios() {
		return prelacionServicioParaServicios;
	}

	public void setPrelacionServicioParaServicios(Set<PrelacionServicio> prelacionServicioParaServicios) {
		this.prelacionServicioParaServicios = prelacionServicioParaServicios;
	}
}
