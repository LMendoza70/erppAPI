package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ParametroCotizador.
 */
@Entity
@Table(name = "parametro_cotizador")
public class ParametroCotizador implements Serializable {

	  private static final long serialVersionUID = 1L;

	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parametroCotizadorGenerator")
	    @SequenceGenerator(allocationSize = 1, name = "parametroCotizadorGenerator", sequenceName="parametro_cotizador_seq")
	    private Long id;

	    @Size(max = 1024)
	    @Column(name = "contenido", length = 1024)
	    private String contenido;
	    
	    @Column(name = "nombre", length = 45)
	    private String nombre;

	    @OneToMany(mappedBy = "parametroCotizador")
	    @JsonIgnore
	    private Set<ParametroCotizadorCosto> parametroCotizadorCostosParaParametroCotizador = new HashSet<>();

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getContenido() {
	        return contenido;
	    }
	    
	    public String getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public void setContenido(String contenido) {
	        this.contenido = contenido;
	    }

	    public Set<ParametroCotizadorCosto> getParametroCotizadorCostosParaParametroCotizador() {
	        return parametroCotizadorCostosParaParametroCotizador;
	    }

	    public ParametroCotizador parametroCotizadorCostosParaParametroCotizador(Set<ParametroCotizadorCosto> parametroCotizadorCostos) {
	        this.parametroCotizadorCostosParaParametroCotizador = parametroCotizadorCostos;
	        return this;
	    }

	    public void setParametroCotizadorCostosParaParametroCotizador(Set<ParametroCotizadorCosto> parametroCotizadorCostos) {
	        this.parametroCotizadorCostosParaParametroCotizador = parametroCotizadorCostos;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) {
	            return true;
	        }
	        if (o == null || getClass() != o.getClass()) {
	            return false;
	        }
	        ParametroCotizador parametroCotizador = (ParametroCotizador) o;
	        if (parametroCotizador.getId() == null || getId() == null) {
	            return false;
	        }
	        return Objects.equals(getId(), parametroCotizador.getId());
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hashCode(getId());
	    }

	    @Override
	    public String toString() {
	        return "ParametroGeneral{" +
	            "id=" + getId() +
	            ", contenido='" + getContenido() + "'" +
	            "}";
	    }
}
