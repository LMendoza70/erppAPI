package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Concepto.
 */
@Entity
@Table(name = "concepto_pago")
public class ConceptoPago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conceptoPagoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "conceptoPagoGenerator", sequenceName="concepto_pago_seq")
    private Long id;

    @Column(name = "activo")
    private Boolean activo;

    @Size(max = 200)
    @Column(name = "nombre", length = 200)
    private String nombre;
	    
    @ManyToOne
    private ClasifActo clasifActo;

    @ManyToOne
    private Area area;
	
    @ManyToOne
    private ServicioClasifConcepto servicioClasifConcepto;
        
    @Size(max = 100)
    @Column(name = "CLAVE_CONCEPTO_FINANZAS", length = 100)
    private String cveConceptoFinanzas;
    
    @Size(max = 1000)
    @Column(name = "formula", length = 1000)
    private String formula;
    
    @Size(max = 1000)
    @Column(name = "formula_cantidad", length = 1000)
    private String formulaCantidad;

    
    @Size(max = 100)
    @Column(name = "conceptos_adicionales", length = 100)
    private String conceptosAdicionales;
    
    @Column(name = "excento_pago")
    private Boolean excentoPago;

    @Column(name = "pago_minimo")
    private Boolean pagoMinimo;

    @Column(name = "pago_tope")
    private Boolean pagoTope;
    
    @OneToMany(mappedBy = "conceptoPago")
    @JsonIgnore
    private Set<ConfCotizador> confCotizadoresParaConceptosPago = new HashSet<>();
    
    @OneToMany(mappedBy = "conceptoPago")
    @JsonIgnore
    private Set<ServicioConceptoPago> serviciosConceptoPago = new HashSet<>();
    
    @Column(name = "tarifa_unitaria")
    private Boolean tarifaUnitaria;        

	public Set<ServicioConceptoPago> getServiciosConceptoPago() {
		return serviciosConceptoPago;
	}

	public void setServiciosConceptoPago(Set<ServicioConceptoPago> serviciosConceptoPago) {
		this.serviciosConceptoPago = serviciosConceptoPago;
	}

	public String getConceptosAdicionales() {
		return conceptosAdicionales;
	}

	public void setConceptosAdicionales(String conceptosAdicionales) {
		this.conceptosAdicionales = conceptosAdicionales;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
    public Boolean getActivo() {
        return activo;
    }

    public ConceptoPago activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public ConceptoPago nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public ClasifActo getClasifActo(){
        return clasifActo;
    }

    public ConceptoPago clasifActo(ClasifActo clasifActo){
        this.clasifActo = clasifActo;
        return this;
    }

    public void setClasifActo(ClasifActo clasifActo){
        this.clasifActo = clasifActo;
    }

    public Area getArea(){
        return area;
    }

    public ConceptoPago area(Area area){
        this.area = area;
        return this;
    }

    public void setArea(Area area){
        this.area = area;
    }
	
	
    public ServicioClasifConcepto getServicioClasifConcepto(){
        return servicioClasifConcepto;
    }

    public ConceptoPago servicioClasifConcepto(ServicioClasifConcepto servicioClasifConcepto){
        this.servicioClasifConcepto = servicioClasifConcepto;
        return this;
    }

    public void setServicioClasifConcepto(ServicioClasifConcepto servicioClasifConcepto){
        this.servicioClasifConcepto = servicioClasifConcepto;
    }
    
    public Boolean getExcentoPago() {
		return excentoPago==null?false:excentoPago;
	}

	public void setExcentoPago(Boolean excentoPago) {
		this.excentoPago = excentoPago;
	}

	public Boolean getPagoMinimo() {
		return pagoMinimo==null?false:pagoMinimo;
	}

	public void setPagoMinimo(Boolean pagoMinimo) {
		this.pagoMinimo = pagoMinimo;
	}

	public Boolean getPagoTope() {
		return pagoTope==null?false:pagoTope;
	}

	public void setPagoTope(Boolean pagoTope) {
		this.pagoTope = pagoTope;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConceptoPago conceptoPago = (ConceptoPago) o;
        if (conceptoPago.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conceptoPago.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConceptoPago{" +
            "id=" + getId() +
            ", activo='" + getActivo() + "'" +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
    
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Set<ConfCotizador> getConfCotizadoresParaConceptosPago() {
		return confCotizadoresParaConceptosPago;
	}

	public void setConfCotizadoresParaConceptosPago(Set<ConfCotizador> confCotizadoresParaConceptosPago) {
		this.confCotizadoresParaConceptosPago = confCotizadoresParaConceptosPago;
	}

	public String getCveConceptoFinanzas() {
		return cveConceptoFinanzas;
	}

	public void setCveConceptoFinanzas(String cveConceptoFinanzas) {
		this.cveConceptoFinanzas = cveConceptoFinanzas;
	}

	public Boolean getTarifaUnitaria() {
		return tarifaUnitaria==null?false:tarifaUnitaria;
	}

	public void setTarifaUnitaria(Boolean tarifaUnitaria) {
		this.tarifaUnitaria = tarifaUnitaria;
	}

	public String getFormulaCantidad() {
		return formulaCantidad;
	}

	public void setFormulaCantidad(String formulaCantidad) {
		this.formulaCantidad = formulaCantidad;
	}
	
}
