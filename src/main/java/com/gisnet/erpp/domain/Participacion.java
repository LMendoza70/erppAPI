package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.SortNatural;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;


@Entity
@Table(name = "participacion")
public class Participacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participacionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "participacionGenerator", sequenceName="participacion_seq")
    private Long id;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    private Date fechaFin;
    
    @Size(max = 80)
    @Column(name = "hash_fila", length = 80)
    private String hashFila;

    @ManyToOne
    private RolSociedad rolSociedad;

    public String getHashFila() {
		return hashFila;
	}

	public void setHashFila(String hashFila) {
		this.hashFila = hashFila;
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

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public RolSociedad getRolSociedad() {
        return rolSociedad;
    }

    public void setRolSociedad(RolSociedad rolSociedad) {
        this.rolSociedad = rolSociedad;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Participacion{" +
            "id=" + getId() +
            "}";
    }
}