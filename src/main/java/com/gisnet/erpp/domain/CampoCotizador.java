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
 * A CampoCotizador.
 */
@Entity
@Table(name = "campo_cotizador")
public class CampoCotizador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campoCotizadorGenerator")
    @SequenceGenerator(allocationSize = 1, name = "campoCotizadorGenerator", sequenceName="campo_cotizador_seq")
    private Long id;

    @Size(max = 45)
    @Column(name = "nombre_campo", length = 45)
    private String nombreCampo;

    @Column(name = "propiedad", length = 45)
    private String propiedad;
    
    @Column(name = "ind_moneda")
    private Boolean indMoneda;
    
    @OneToMany(mappedBy = "campoCotizador")
    @JsonIgnore
    private Set<ConfCotizador> confCotizadoresParaCamposCotizador = new HashSet<>();
    
    @Transient
    @JsonProperty
    private String valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getPropiedad() {
        return propiedad;
    }


    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }

    public Set<ConfCotizador> getConfCotizadoresParaCamposCotizador() {
        return confCotizadoresParaCamposCotizador;
    }

    public CampoCotizador confCotizadoresParaCamposCotizador(Set<ConfCotizador> confCotizadors) {
        this.confCotizadoresParaCamposCotizador = confCotizadors;
        return this;
    }

    public CampoCotizador addConfCotizadoresParaCampoCotizador(ConfCotizador confCotizador) {
        this.confCotizadoresParaCamposCotizador.add(confCotizador);
        confCotizador.setCampoCotizador(this);
        return this;
    }

    public CampoCotizador removeConfCotizadoresParaCampoCotizador(ConfCotizador confCotizador) {
        this.confCotizadoresParaCamposCotizador.remove(confCotizador);
        confCotizador.setCampoCotizador(null);
        return this;
    }

    public void setConfCotizadoresParaCamposCotizador(Set<ConfCotizador> confCotizadors) {
        this.confCotizadoresParaCamposCotizador = confCotizadors;
    }
    
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CampoCotizador campoCotizador = (CampoCotizador) o;
        if (campoCotizador.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), campoCotizador.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "campoCotizador{" +
            "id=" + getId() +
            ", nombre_campo='" + getNombreCampo() + "'" +
            ", propiedad='" + getPropiedad() + "'" +
            ", valor='" + getValor() + "'" +
            "}";
    }

	public String getNombreCampo() {
		return nombreCampo;
	}

	public void setNombreCampo(String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Boolean getIndMoneda() {
		return indMoneda;
	}

	public void setIndMoneda(Boolean indMoneda) {
		this.indMoneda = indMoneda;
	}
	
}
