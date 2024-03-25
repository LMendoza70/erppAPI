package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.Date;

/**
 * A TurnoElectronico.
 */
@Entity
@Table(name = "turno_electronico")
public class TurnoElectronico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "turnoElectronicoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "turnoElectronicoGenerator", sequenceName="turno_electronico_seq")
    private Long id;

    @Column(name = "fecha_hora_entrada")
    private Date fechaHoraEntrada;

    @Column(name = "fecha_hora_asigna")
    private Date fechaHoraAsigna;

    private Integer ponderacion;

    private Integer status; 

    @ManyToOne
    private Prelacion prelacion;

    @ManyToOne
    private Usuario usuarioOrigen;

    @ManyToOne
    private Usuario usuarioDestino;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaHoraEntrada(){
        return fechaHoraEntrada;
    }

    public TurnoElectronico fechaHoraEntrada(Date fechaHoraEntrada){
        this.fechaHoraEntrada = fechaHoraEntrada;
        return this;
    }

    public void setFechaHoraEntrada(Date fechaHoraEntrada){
        this.fechaHoraEntrada = fechaHoraEntrada;
    }

    public Date getFechaHoraAsigna(){
        return fechaHoraAsigna;
    }

    public TurnoElectronico fechaHoraAsigna(Date fechaHoraAsigna){
        this.fechaHoraAsigna = fechaHoraAsigna;
        return this;
    }

    public void setFechaHoraAsigna(Date fechaHoraAsigna){
        this.fechaHoraAsigna = fechaHoraAsigna;
    }

    public Integer getPonderacion(){
        return ponderacion;
    }

    public TurnoElectronico ponderacion(Integer ponderacion){
        this.ponderacion = ponderacion;
        return this;
    }

    public void setPonderacion(Integer ponderacion){
        this.ponderacion = ponderacion;
    }

    public Integer getStatus(){
        return status;
    }

    public TurnoElectronico status(Integer status){
        this.status = status;
        return this;
    }

    public void setStatus(Integer status){
        this.status = status;
    }

    public Prelacion getPrelacion(){
        return prelacion;
    }

    public TurnoElectronico prelacion(Prelacion prelacion){
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion){
        this.prelacion = prelacion;
    }

    public Usuario getUsuarioOrigen(){
        return usuarioOrigen;
    }

    public TurnoElectronico usuarioOrigen(Usuario usuarioOrigen){
        this.usuarioOrigen = usuarioOrigen;
        return this;
    }

    public void setUsuarioOrigen(Usuario usuarioOrigen){
        this.usuarioOrigen = usuarioOrigen;
    }

    public Usuario getUsuarioDestino(){
        return usuarioDestino;
    }

    public TurnoElectronico usuarioDestino(Usuario usuarioDestino){
        this.usuarioDestino = usuarioDestino;
        return this;
    }

    public void setUsuarioDestino(Usuario usuarioDestino){
        this.usuarioDestino = usuarioDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TurnoElectronico turnoElectronico = (TurnoElectronico) o;
        if (turnoElectronico.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turnoElectronico.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurnoElectronico{" +
            "id=" + getId() +
            "}";
    }
}