package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.gisnet.erpp.vo.PrelacionVO;

/**
 * A Recibo.
 */
@Entity
@Table(name = "cita")
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "citaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "citaGenerator", sequenceName="cita_seq")
    private Long id;

    @Column(name = "fecha")
    private Date fecha;

    @ManyToOne(optional = false)
    @NotNull
    private Oficina oficina;

    @OneToOne(optional = false)
    @NotNull
    private Prelacion prelacion;


    private Long status;

     public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public Cita fecha(Date fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Prelacion getPrelacion() {
        return prelacion;
    }

    public Cita prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public Cita oficina(Oficina oficina) {
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recibo recibo = (Recibo) o;
        if (recibo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recibo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cita{" +
            "id=" + getId() +
            "}";
    }
}
