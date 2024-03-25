package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Campo.
 */
@Entity
@Table(name = "campo")
public class Campo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "campoGenerator", sequenceName="campo_seq")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "maxima")
    private Integer maxima;

    @Column(name = "minima")
    private Integer minima;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Size(max = 30)
    @Column(name = "tabla", length = 30)
    private String tabla;
    
    // @Column(name = "etiqueta", nullable = true)
    // private String etiqueta;

    @Size(max = 30)
    @Column(name = "tabla_campo", length = 30)
    private String tablaCampo;

    @OneToMany(mappedBy = "campo")
    @JsonIgnore
    private Set<CampoValores> campoValoresParaCampos = new HashSet<>();

    @OneToMany(mappedBy = "campo")
    @JsonIgnore
    private Set<ModuloCampo> moduloCamposParaCampos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private TipoCampo tipoCampo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Campo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getMaxima() {
        return maxima;
    }

    public Campo maxima(Integer maxima) {
        this.maxima = maxima;
        return this;
    }

    public void setMaxima(Integer maxima) {
        this.maxima = maxima;
    }

    public Integer getMinima() {
        return minima;
    }

    public Campo minima(Integer minima) {
        this.minima = minima;
        return this;
    }

    public void setMinima(Integer minima) {
        this.minima = minima;
    }
    
    // public String getEtiqueta() {
    //     return etiqueta;
    // }
    // public void setEtiqueta(String etiqueta) {
    //     this.etiqueta = etiqueta;
    // }

    public Boolean isActivo() {
        return activo;
    }

    public Campo activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getTabla() {
        return tabla;
    }

    public Campo tabla(String tabla) {
        this.tabla = tabla;
        return this;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getTablaCampo() {
        return tablaCampo;
    }

    public Campo tablaCampo(String tablaCampo) {
        this.tablaCampo = tablaCampo;
        return this;
    }

    public void setTablaCampo(String tablaCampo) {
        this.tablaCampo = tablaCampo;
    }

    public Set<CampoValores> getCampoValoresParaCampos() {
        return campoValoresParaCampos;
    }

    @JsonProperty
    public Campo campoValoresParaCampos(Set<CampoValores> campoValores) {
        this.campoValoresParaCampos = campoValores;
        return this;
    }

    public Campo addCampoValoresParaCampo(CampoValores campoValores) {
        this.campoValoresParaCampos.add(campoValores);
        campoValores.setCampo(this);
        return this;
    }

    public Campo removeCampoValoresParaCampo(CampoValores campoValores) {
        this.campoValoresParaCampos.remove(campoValores);
        campoValores.setCampo(null);
        return this;
    }

    public void setCampoValoresParaCampos(Set<CampoValores> campoValores) {
        this.campoValoresParaCampos = campoValores;
    }

    public Set<ModuloCampo> getModuloCamposParaCampos() {
        return moduloCamposParaCampos;
    }

    public Campo moduloCamposParaCampos(Set<ModuloCampo> moduloCampos) {
        this.moduloCamposParaCampos = moduloCampos;
        return this;
    }

    public Campo addModuloCamposParaCampo(ModuloCampo moduloCampo) {
        this.moduloCamposParaCampos.add(moduloCampo);
        moduloCampo.setCampo(this);
        return this;
    }

    public Campo removeModuloCamposParaCampo(ModuloCampo moduloCampo) {
        this.moduloCamposParaCampos.remove(moduloCampo);
        moduloCampo.setCampo(null);
        return this;
    }

    public void setModuloCamposParaCampos(Set<ModuloCampo> moduloCampos) {
        this.moduloCamposParaCampos = moduloCampos;
    }

    public TipoCampo getTipoCampo() {
        return tipoCampo;
    }

    public Campo tipoCampo(TipoCampo tipoCampo) {
        this.tipoCampo = tipoCampo;
        return this;
    }

    public void setTipoCampo(TipoCampo tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Campo campo = (Campo) o;
        if (campo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), campo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Campo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", maxima='" + getMaxima() + "'" +
            ", minima='" + getMinima() + "'" +
            ", activo='" + isActivo() + "'" +
            ", tabla='" + getTabla() + "'" +
            ", tablaCampo='" + getTablaCampo() + "'" +
            ", tipoCampo='"+getTipoCampo()+ "'" +
            ", valoresCampo='"+getCampoValoresParaCampos()+ "'" +
            "}";
    }
}
