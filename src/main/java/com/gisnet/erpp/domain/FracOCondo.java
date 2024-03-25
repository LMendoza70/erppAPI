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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A SI es Fraccionamiento o Condominio o NO CONSTA.
 */
@Entity
@Table(name = "frac_o_condo")
public class FracOCondo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fracOCondoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "fracOCondoGenerator", sequenceName="frac_o_condo_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;
    
    @OneToMany(mappedBy = "fracOCondo")
    @JsonIgnore
    private Set<Predio> prediosParaFracOCondos = new HashSet<>();

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
	
	public Set<Predio> getPrediosParaFracOCondos() {
		return prediosParaFracOCondos;
	}

	public void setPrediosParaFracOCondos(Set<Predio> prediosParaFracOCondos) {
		this.prediosParaFracOCondos = prediosParaFracOCondos;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FracOCondo tipoInmueble = (FracOCondo) o;
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
        return "FracOCondo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
