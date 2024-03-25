package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TarifaPeriodo.
 */
@Entity
@Table(name = "tarifa_periodo")
public class TarifaPeriodo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tarifaPeriodoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tarifaPeriodoGenerator", sequenceName="tarifa_periodo_seq")
    private Long id;

    @Column(name = "importe_pago")
    private Double importePago;

    @ManyToOne
    private ConceptoPago conceptoPago;

    @ManyToOne
    private Periodo periodo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getImportePago() {
        return importePago;
    }

    public TarifaPeriodo importePago(Double importePago) {
        this.importePago = importePago;
        return this;
    }

    public void setImportePago(Double importePago) {
        this.importePago = importePago;
    }

    public ConceptoPago getConceptoPago(){
        return conceptoPago;
    }

    public TarifaPeriodo conceptoPago(ConceptoPago conceptoPago){
        this.conceptoPago = conceptoPago;
        return this;
    }

    public void setConceptoPago(ConceptoPago conceptoPago){
        this.conceptoPago = conceptoPago;
    }

    public Periodo getPeriodo(){
        return periodo;
    }

    public TarifaPeriodo periodo(Periodo periodo){
        this.periodo = periodo;
        return this;
    }

    public void setPeriodo(Periodo periodo){
        this.periodo = periodo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TarifaPeriodo tarifaPeriodo = (TarifaPeriodo) o;
        if (tarifaPeriodo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tarifaPeriodo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TarifaPeriodo{" +
            "id=" + getId() +
            "}";
    }
}
