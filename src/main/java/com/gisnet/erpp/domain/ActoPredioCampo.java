package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ActoPredioCampo.
 */
@Entity
@Table(name = "acto_predio_campo")
public class ActoPredioCampo implements Serializable,Comparable<ActoPredioCampo> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoPredioCampoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoPredioCampoGenerator", sequenceName="acto_predio_campo_seq")
    private Long id;

   
    @Column(name = "valor")
    private String valor;

    @Column(name = "orden")
    private Integer orden;

    @ManyToOne(optional = false)
    @NotNull
//    @JsonIgnore
    private ActoPredio actoPredio;

    @ManyToOne(optional = false)
    @JoinColumn(name="modulo_campo_id")
    @NotNull
    private ModuloCampo moduloCampo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public ActoPredioCampo valor(String valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Integer getOrden() {
        return orden;
    }

    public ActoPredioCampo orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public ActoPredio getActoPredio() {
        return actoPredio;
    }

    public ActoPredioCampo actoPredio(ActoPredio actoPredio) {
        this.actoPredio = actoPredio;
        return this;
    }

    public void setActoPredio(ActoPredio actoPredio) {
        this.actoPredio = actoPredio;
    }

    public ModuloCampo getModuloCampo() {
        return moduloCampo;
    }

    public ActoPredioCampo moduloCampo(ModuloCampo moduloCampo) {
        this.moduloCampo = moduloCampo;
        return this;
    }

    public void setModuloCampo(ModuloCampo moduloCampo) {
        this.moduloCampo = moduloCampo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActoPredioCampo actoPredioCampo = (ActoPredioCampo) o;
        if (actoPredioCampo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actoPredioCampo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActoPredioCampo{" +
            "id=" + getId() +
            ", valor='" + getValor() + "'" +
            ", orden='" + getOrden() + "'" +
            ", ModuloCampo='" + getModuloCampo().getId() + "'" +
            ", ActoPredio='" + getActoPredio().getId() + "'" +
            "}";
    }

	@Override
	public int compareTo(ActoPredioCampo o) {
		// TODO Auto-generated method stub	es la comparacion de objetos(es el mismo) para el orden asc o desc"o.orden-this.orden"
		return (int)(this.orden-o.orden);
	}
}
