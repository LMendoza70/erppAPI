package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Colindancia.
 */
@Entity
@Table(name = "colindancia")
public class Colindancia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "colindanciaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "colindanciaGenerator" , sequenceName="colindancia_seq")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    private Predio predioRel;

    @ManyToOne
    private TipoColin tipoColin;

    @ManyToOne
    private Vialidad vialidad;

    @ManyToOne(optional = false)
    @NotNull
    private Predio predio;

    @ManyToOne(optional = false)
    @NotNull
    private Orientacion orientacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Colindancia nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Predio getPredioRel() {
        return predioRel;
    }

    public Colindancia predioRel(Predio predio) {
        this.predioRel = predio;
        return this;
    }

    public void setPredioRel(Predio predio) {
        this.predioRel = predio;
    }

    public TipoColin getTipoColin() {
        return tipoColin;
    }

    public Colindancia tipoColin(TipoColin tipoColin) {
        this.tipoColin = tipoColin;
        return this;
    }

    public void setTipoColin(TipoColin tipoColin) {
        this.tipoColin = tipoColin;
    }

    public Vialidad getVialidad() {
        return vialidad;
    }

    public Colindancia vialidad(Vialidad vialidad) {
        this.vialidad = vialidad;
        return this;
    }

    public void setVialidad(Vialidad vialidad) {
        this.vialidad = vialidad;
    }

    public Predio getPredio() {
        return predio;
    }

    public Colindancia predio(Predio predio) {
        this.predio = predio;
        return this;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public Orientacion getOrientacion() {
        return orientacion;
    }

    public Colindancia orientacion(Orientacion orientacion) {
        this.orientacion = orientacion;
        return this;
    }

    public void setOrientacion(Orientacion orientacion) {
        this.orientacion = orientacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Colindancia colindancia = (Colindancia) o;
        if (colindancia.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), colindancia.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Colindancia{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
