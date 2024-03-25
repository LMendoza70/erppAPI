package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Requisito.
 */
@Entity
@Table(name = "requisito")
public class Requisito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requisitoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "requisitoGenerator", sequenceName="requisito_seq")
    private Long id;

    @Size(max = 200)
    @Column(name = "nombre", length = 200)
    private String nombre;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @Column(name = "ve")
    private Boolean ve;

    @OneToMany(mappedBy = "requisito")
    @JsonIgnore
    private Set<ActoRequisito> actoRequisitosParaRequisitos = new HashSet<>();

    @OneToMany(mappedBy = "requisito")
    @JsonIgnore
    private Set<ReqClasifActo> reqClasifActosParaRequisitos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Requisito nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Requisito activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getVe() {
		return ve;
	}

	public void setVe(Boolean ve) {
		this.ve = ve;
	}

	public Set<ActoRequisito> getActoRequisitosParaRequisitos() {
        return actoRequisitosParaRequisitos;
    }

    public Requisito actoRequisitosParaRequisitos(Set<ActoRequisito> actoRequisitos) {
        this.actoRequisitosParaRequisitos = actoRequisitos;
        return this;
    }

    public Requisito addActoRequisitosParaRequisito(ActoRequisito actoRequisito) {
        this.actoRequisitosParaRequisitos.add(actoRequisito);
        actoRequisito.setRequisito(this);
        return this;
    }

    public Requisito removeActoRequisitosParaRequisito(ActoRequisito actoRequisito) {
        this.actoRequisitosParaRequisitos.remove(actoRequisito);
        actoRequisito.setRequisito(null);
        return this;
    }

    public void setActoRequisitosParaRequisitos(Set<ActoRequisito> actoRequisitos) {
        this.actoRequisitosParaRequisitos = actoRequisitos;
    }

    public Set<ReqClasifActo> getReqClasifActosParaRequisitos() {
        return reqClasifActosParaRequisitos;
    }

    public Requisito reqClasifActosParaRequisitos(Set<ReqClasifActo> reqClasifActos) {
        this.reqClasifActosParaRequisitos = reqClasifActos;
        return this;
    }

    public Requisito addReqClasifActosParaRequisito(ReqClasifActo reqClasifActo) {
        this.reqClasifActosParaRequisitos.add(reqClasifActo);
        reqClasifActo.setRequisito(this);
        return this;
    }

    public Requisito removeReqClasifActosParaRequisito(ReqClasifActo reqClasifActo) {
        this.reqClasifActosParaRequisitos.remove(reqClasifActo);
        reqClasifActo.setRequisito(null);
        return this;
    }

    public void setReqClasifActosParaRequisitos(Set<ReqClasifActo> reqClasifActos) {
        this.reqClasifActosParaRequisitos = reqClasifActos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Requisito requisito = (Requisito) o;
        if (requisito.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requisito.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Requisito{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
