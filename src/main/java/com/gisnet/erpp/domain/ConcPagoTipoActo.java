package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ConcPagoTipoActo.
 */
@Entity
@Table(name = "conc_pago_tipo_acto")
public class ConcPagoTipoActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "concPagoTipoActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "concPagoTipoActoGenerator", sequenceName="conc_pago_tipo_acto_seq")
    private Long id;

    @ManyToOne
    private ConceptoPago conceptoPago;

    @ManyToOne
    private TipoActo tipoActo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConceptoPago getConceptoPago(){
        return conceptoPago;
    }

    public ConcPagoTipoActo conceptoPago(ConceptoPago conceptoPago){
        this.conceptoPago = conceptoPago;
        return this;
    }

    public void setConceptoPago(ConceptoPago conceptoPago){
        this.conceptoPago = conceptoPago;
    }

    public TipoActo getTipoActo(){
        return tipoActo;
    }

    public ConcPagoTipoActo tipoActo(TipoActo tipoActo){
        this.tipoActo = tipoActo;
        return this;
    }

    public void setTipoActo(TipoActo tipoActo){
        this.tipoActo = tipoActo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConcPagoTipoActo conceptoPagoTA = (ConcPagoTipoActo) o;
        if (conceptoPagoTA.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conceptoPagoTA.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConcPagoTipoActo{" +
            "id=" + getId() +
            "}";
    }
}
