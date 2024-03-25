package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A ModuloCampo.
 */
@Entity
@Table(name = "modulo_campo")
public class ModuloCampo implements Serializable {

    public Integer getInd_impresion() {
		return ind_impresion;
	}

	public void setInd_impresion(Integer ind_impresion) {
		this.ind_impresion = ind_impresion;
	}

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moduloCampoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "moduloCampoGenerator", sequenceName = "modulo_campo_seq")
    private Long id;

    @NotNull
    @Column(name = "requerido", nullable = false)
    private Boolean requerido;

    @NotNull
    @Column(name = "orden", nullable = false)
    private Integer orden;

    @OneToMany(mappedBy = "moduloCampo")
    @JsonIgnore
    private Set<ActoPredioCampo> actoPredioCamposParaModuloCampos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnore
    private Modulo modulo;

    @ManyToOne(optional = false)
    @NotNull
    private Campo campo;
    
    @Column(name = "presencial")
    private Boolean presencial; 
    
    @Column(name = "ind_hist")
    private Boolean indHist;

    @Column(name = "etiqueta", nullable = true, length = 80)
    private String etiqueta;

    @Column(name = "longitud", nullable = true)
    private Integer longitud;
    
    @Column(name = "ve")
    private Boolean ve;

    @Column(name = "condicion_campo", nullable = true)
    private String condicionCampo;
    
    @Column(name = "condicion_tipo_comparacion", nullable = true)
    private String condicionTipoComparacion;
    
    @Column(name = "condicion_expresion", nullable = true)
    private String condicionExprecion;
    
    @Column(name = "mayusculas", nullable = true)
    private Boolean mayusculas;
    
    @Column(name = "ind_impresion", nullable = true)
    private Integer ind_impresion;

	public Boolean getMayusculas() {
		return mayusculas;
	}

	public void setMayusculas(Boolean mayusculas) {
		this.mayusculas = mayusculas;
	}    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Boolean getVe(){
        return ve;
    }
    
    public void setVe(Boolean ve){
        this.ve = ve;
    }

    public Boolean isRequerido() {
        return requerido;
    }

    public ModuloCampo requerido(Boolean requerido) {
        this.requerido = requerido;
        return this;
    }

    public void setRequerido(Boolean requerido) {
        this.requerido = requerido;
    }

    public Integer getOrden() {
        return orden;
    }

    public ModuloCampo orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Set<ActoPredioCampo> getActoPredioCamposParaModuloCampos() {
        return actoPredioCamposParaModuloCampos;
    }

    public ModuloCampo actoPredioCamposParaModuloCampos(Set<ActoPredioCampo> actoPredioCampos) {
        this.actoPredioCamposParaModuloCampos = actoPredioCampos;
        return this;
    }

    public ModuloCampo addActoPredioCamposParaModuloCampo(ActoPredioCampo actoPredioCampo) {
        this.actoPredioCamposParaModuloCampos.add(actoPredioCampo);
        actoPredioCampo.setModuloCampo(this);
        return this;
    }

    public ModuloCampo removeActoPredioCamposParaModuloCampo(ActoPredioCampo actoPredioCampo) {
        this.actoPredioCamposParaModuloCampos.remove(actoPredioCampo);
        actoPredioCampo.setModuloCampo(null);
        return this;
    }

    public void setActoPredioCamposParaModuloCampos(Set<ActoPredioCampo> actoPredioCampos) {
        this.actoPredioCamposParaModuloCampos = actoPredioCampos;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public ModuloCampo modulo(Modulo modulo) {
        this.modulo = modulo;
        return this;
    }

    @JsonProperty
    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Campo getCampo() {
        return campo;
    }

    public ModuloCampo campo(Campo campo) {
        this.campo = campo;
        return this;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }
    
    public Boolean getPresencial() {
        return presencial;
    }

    public ModuloCampo presencial(Boolean presencial) {
        this.presencial = presencial;
        return this;
    }

    public void setPresencial(Boolean presencial) {
        this.presencial = presencial;
    }

    public Boolean getIndHist() {
        return indHist;
    }

    public ModuloCampo indHist(Boolean indHist) {
        this.indHist = indHist;
        return this;
    }

    public void setIndHist(Boolean indHist) {
        this.indHist = indHist;
    }
    
    public String getEtiqueta() {
        return etiqueta;
    }

    public ModuloCampo etiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
        return this;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Integer getLongitud() {
        return longitud;
    }

    public ModuloCampo longitud(Integer longitud) {
        this.longitud = longitud;
        return this;
    }

    public void setLongitud(Integer longitud) {
        this.longitud = longitud;
    }

    public String getCondicionCampo() {
        return condicionCampo;
    }
    public ModuloCampo numCondicionCampo(String condicionCampo) {
        this.condicionCampo = condicionCampo;
        return this;
    }
    public void setCondicionCampo(String condicionCampo) {
        this.condicionCampo = condicionCampo;
    }
    
    public String getCondicionTipoComparacion() {
        return condicionTipoComparacion;
    }
    public ModuloCampo numCondicionTipoComparacion(String condicionTipoComparacion) {
        this.condicionTipoComparacion = condicionTipoComparacion;
        return this;
    }
    public void setCondicionTipoComparacion(String condicionTipoComparacion) {
        this.condicionTipoComparacion = condicionTipoComparacion;
    }
    
    public String getCondicionExprecion() {
        return condicionExprecion;
    }
    public ModuloCampo numCondicionExprecion(String condicionExprecion) {
        this.condicionExprecion = condicionExprecion;
        return this;
    }
    public void setCondicionExprecion(String condicionExprecion) {
        this.condicionExprecion = condicionExprecion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModuloCampo moduloCampo = (ModuloCampo) o;
        if (moduloCampo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), moduloCampo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ModuloCampo{" +
            "id=" + getId() +
            ", requerido='" + isRequerido() + "'" +
            ", orden='" + getOrden() + "'" +
            ", presencial='" + getPresencial() + "'" +
            ", modulo='" + getModulo() + "'" +
            "}";
    }
}
