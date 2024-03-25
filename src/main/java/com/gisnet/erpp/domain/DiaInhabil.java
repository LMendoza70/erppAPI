package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Date;

/**
 * A DiaInhabil.
 */
@Entity
@Table(name = "dia_inhabil")
public class DiaInhabil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diaInhabilGenerator")
    @SequenceGenerator(allocationSize = 1, name = "diaInhabilGenerator", sequenceName="dia_inhabil_seq")
    private Long id;

    @NotNull
    @Column(name = "fecha_dia_inhabil", nullable = false)
    private Date fechaDiaInhabil;

    private Integer dia;

    private Integer mes;

    @Column(name = "ley_motivo")
    private String leyMotivo;

    @Column(name = "todo_el_estado")
    private Boolean todoElEstado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaDiaInhabil() {
        return fechaDiaInhabil;
    }

    public DiaInhabil fechaDiaInhabil(Date fechaDiaInhabil) {
        this.fechaDiaInhabil = fechaDiaInhabil;
        return this;
    }

    public void setFechaDiaInhabil(Date fechaDiaInhabil) {
        this.fechaDiaInhabil = fechaDiaInhabil;
    }

    public Integer getDia() {
        return dia;
    }

    public DiaInhabil dia(Integer dia) {
        this.dia = dia;
        return this;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getMes() {
        return mes;
    }

    public DiaInhabil mes(Integer mes) {
        this.mes = mes;
        return this;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public String getLeyMotivo() {
        return leyMotivo;
    }

    public DiaInhabil leyMotivo(String leyMotivo) {
        this.leyMotivo = leyMotivo;
        return this;
    }

    public void setLeyMotivo(String leyMotivo) {
        this.leyMotivo = leyMotivo;
    }

    public Boolean getTodoElEstado() {
        return todoElEstado;
    }

    public DiaInhabil todoElEstado(Boolean todoElEstado) {
        this.todoElEstado = todoElEstado;
        return this;
    }

    public void setTodoElEstado(Boolean todoElEstado) {
        this.todoElEstado = todoElEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiaInhabil diaInhabil = (DiaInhabil) o;
        if (diaInhabil.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), diaInhabil.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DiaInhabil{" +
            "id=" + getId() +
            "}";
    }
}
