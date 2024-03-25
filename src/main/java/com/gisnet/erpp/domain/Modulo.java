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
 * A Modulo.
 */
@Entity
@Table(name = "modulo")
public class Modulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moduloGenerator")
    @SequenceGenerator(allocationSize = 1, name = "moduloGenerator", sequenceName="modulo_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "modulo")
    @JsonIgnore
    @OrderBy("orden ASC")
    private Set<ModuloCampo> moduloCamposParaModulos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "modulo")
    @JsonIgnore
    @OrderBy("orden ASC")
    private Set<ModuloTipoActo> moduloTipoActosParaModulos = new LinkedHashSet<>();
    
    @Column(name = "comportamiento_modulo")
    private Integer comportamientoModulo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Modulo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<ModuloCampo> getModuloCamposParaModulos() {
        return moduloCamposParaModulos;
    }

    public Modulo moduloCamposParaModulos(Set<ModuloCampo> moduloCampos) {
        this.moduloCamposParaModulos = moduloCampos;
        return this;
    }

    public Modulo addModuloCamposParaModulo(ModuloCampo moduloCampo) {
        this.moduloCamposParaModulos.add(moduloCampo);
        moduloCampo.setModulo(this);
        return this;
    }

    public Modulo removeModuloCamposParaModulo(ModuloCampo moduloCampo) {
        this.moduloCamposParaModulos.remove(moduloCampo);
        moduloCampo.setModulo(null);
        return this;
    }

    public void setModuloCamposParaModulos(Set<ModuloCampo> moduloCampos) {
        this.moduloCamposParaModulos = moduloCampos;
    }

    public Set<ModuloTipoActo> getModuloTipoActosParaModulos() {
        return moduloTipoActosParaModulos;
    }

    public Modulo moduloTipoActosParaModulos(Set<ModuloTipoActo> moduloTipoActos) {
        this.moduloTipoActosParaModulos = moduloTipoActos;
        return this;
    }

    public Modulo addModuloTipoActosParaModulo(ModuloTipoActo moduloTipoActo) {
        this.moduloTipoActosParaModulos.add(moduloTipoActo);
        moduloTipoActo.setModulo(this);
        return this;
    }

    public Modulo removeModuloTipoActosParaModulo(ModuloTipoActo moduloTipoActo) {
        this.moduloTipoActosParaModulos.remove(moduloTipoActo);
        moduloTipoActo.setModulo(null);
        return this;
    }

    public void setModuloTipoActosParaModulos(Set<ModuloTipoActo> moduloTipoActos) {
        this.moduloTipoActosParaModulos = moduloTipoActos;
    }
    
    public Integer getComportamientoModulo() {
        return comportamientoModulo;
    }

    public Modulo ComportamientoModulo(Integer comportamientoModulo) {
        this.comportamientoModulo = comportamientoModulo;
        return this;
    }

    public void setComportamientoModulo(Integer comportamientoModulo) {
        this.comportamientoModulo = comportamientoModulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Modulo modulo = (Modulo) o;
        if (modulo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), modulo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Modulo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
