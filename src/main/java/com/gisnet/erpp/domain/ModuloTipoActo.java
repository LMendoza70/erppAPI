package com.gisnet.erpp.domain;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ModuloTipoActo.
 */
@Entity
@Table(name = "modulo_tipo_acto")
public class ModuloTipoActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moduloTipoActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "moduloTipoActoGenerator", sequenceName="modulo_tipo_acto_seq")
    private Long id;

    @NotNull
    @Column(name = "multiple", nullable = false)
    private Boolean multiple;

    @NotNull
    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "num_columnas", nullable = true)
    private Integer numColumnas;

    @Column(name = "condicion_modulo", nullable = true)
    private String condicionModulo;

    @Column(name = "condicion_campo", nullable = true)
    private String condicionCampo;

    @Column(name = "condicion_tipo_comparacion", nullable = true)
    private String condicionTipoComparacion;

    @Column(name = "condicion_expresion", nullable = true)
    private String condicionExprecion;

    @OneToMany(mappedBy = "moduloTipoActo")
    @JsonIgnore
    private Set<ActoModulo> actoModulosParaModuloTipoActos = new LinkedHashSet<>();

    @ManyToOne(fetch= FetchType.LAZY, optional = false)
    @NotNull
    private Modulo modulo;

    @ManyToOne(fetch= FetchType.LAZY, optional = false)
    @NotNull
    private TipoActo tipoActo;
	
    @Column(name = "etiqueta", length = 100)
    private String etiqueta;

    @Column(name = "presencial")
    private Boolean presencial;

    @Column(name = "ve")
    private Boolean ve;

    @Column(name = "cf")
    private Integer cf;

    @Column(name = "hist")
    private Boolean hist;

    @Column(name = "ind_impresion", nullable = true)
    private Integer ind_impresion;

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

    public Integer getCf(){
        return cf;
    }

    public void setCf(Integer cf){
        this.cf = cf;
    }

    public Boolean isMultiple() {
        return multiple;
    }

    public ModuloTipoActo multiple(Boolean multiple) {
        this.multiple = multiple;
        return this;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public Integer getOrden() {
        return orden;
    }

    public ModuloTipoActo orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Integer getNumColumnas() {
        return numColumnas;
    }

    public ModuloTipoActo numColumnasOrden(Integer numColumnas) {
        this.numColumnas = numColumnas;
        return this;
    }

    public void setNumColumnas(Integer numColumnas) {
        this.numColumnas = numColumnas;
    }

    public String getCondicionModulo() {
        return condicionModulo;
    }
    public ModuloTipoActo numCondicionModulo(String condicionModulo) {
        this.condicionModulo = condicionModulo;
        return this;
    }
    public void setCondicionModulo(String condicionModulo) {
        this.condicionModulo = condicionModulo;
    }

    public String getCondicionCampo() {
        return condicionCampo;
    }
    public ModuloTipoActo numCondicionCampo(String condicionCampo) {
        this.condicionCampo = condicionCampo;
        return this;
    }
    public void setCondicionCampo(String condicionCampo) {
        this.condicionCampo = condicionCampo;
    }

    public String getCondicionTipoComparacion() {
        return condicionTipoComparacion;
    }
    public ModuloTipoActo numCondicionTipoComparacion(String condicionTipoComparacion) {
        this.condicionTipoComparacion = condicionTipoComparacion;
        return this;
    }
    public void setCondicionTipoComparacion(String condicionTipoComparacion) {
        this.condicionTipoComparacion = condicionTipoComparacion;
    }

    public String getCondicionExprecion() {
        return condicionExprecion;
    }
    public ModuloTipoActo numCondicionExprecion(String condicionExprecion) {
        this.condicionExprecion = condicionExprecion;
        return this;
    }
    public void setCondicionExprecion(String condicionExprecion) {
        this.condicionExprecion = condicionExprecion;
    }

    public Set<ActoModulo> getActoModulosParaModuloTipoActos() {
        return actoModulosParaModuloTipoActos;
    }

    public ModuloTipoActo actoModulosParaModuloTipoActos(Set<ActoModulo> actoModulos) {
        this.actoModulosParaModuloTipoActos = actoModulos;
        return this;
    }

    public ModuloTipoActo addActoModulosParaModuloTipoActo(ActoModulo actoModulo) {
        this.actoModulosParaModuloTipoActos.add(actoModulo);
        actoModulo.setModuloTipoActo(this);
        return this;
    }

    public ModuloTipoActo removeActoModulosParaModuloTipoActo(ActoModulo actoModulo) {
        this.actoModulosParaModuloTipoActos.remove(actoModulo);
        actoModulo.setModuloTipoActo(null);
        return this;
    }

    public void setActoModulosParaModuloTipoActos(Set<ActoModulo> actoModulos) {
        this.actoModulosParaModuloTipoActos = actoModulos;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public ModuloTipoActo modulo(Modulo modulo) {
        this.modulo = modulo;
        return this;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public TipoActo getTipoActo() {
        return tipoActo;
    }

    public ModuloTipoActo tipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
        return this;
    }

    public void setTipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
    public ModuloTipoActo etiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
        return this;
    }
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Boolean getPresencial() {
        return presencial;
    }
    public ModuloTipoActo presencial(Boolean presencial) {
        this.presencial = presencial;
        return this;
    }
    public void setPresencial(Boolean presencial) {
        this.presencial = presencial;
    }

    public Boolean getHist() {
		return hist;
	}

	public void setHist(Boolean hist) {
		this.hist = hist;
    }
    
    public Integer getInd_impresion() {
		return ind_impresion;
	}

	public void setInd_impresion(Integer ind_impresion) {
		this.ind_impresion = ind_impresion;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModuloTipoActo moduloTipoActo = (ModuloTipoActo) o;
        if (moduloTipoActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), moduloTipoActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ModuloTipoActo{" +
            "id=" + getId() +
            ", multiple='" + isMultiple() + "'" +
            ", orden='" + getOrden() + "'" +
            "}";
    }


}
