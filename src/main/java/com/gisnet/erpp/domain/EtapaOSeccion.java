package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Etapa o Seccion.
 */
@Entity
@Table(name = "etapa_o_seccion")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EtapaOSeccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "etapaOSeccionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "etapaOSeccionGenerator", sequenceName="etapa_o_seccion_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;
    
    @OneToMany(mappedBy = "etapaOSeccion")
    @JsonIgnore
    private Set<Predio> prediosParaEstapaOSecciones = new HashSet<>();

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public Set<Predio> getPrediosParaEstapaOSecciones() {
		return prediosParaEstapaOSecciones;
	}

	public void setPrediosParaEstapaOSecciones(Set<Predio> prediosParaEstapaOSecciones) {
		this.prediosParaEstapaOSecciones = prediosParaEstapaOSecciones;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EtapaOSeccion tipoInmueble = (EtapaOSeccion) o;
        if (tipoInmueble.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoInmueble.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "etapaOSeccion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
