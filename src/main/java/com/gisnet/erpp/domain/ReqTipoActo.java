package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ReqClasifActo.
 */
@Entity
@Table(name = "req_tipo_acto")
public class ReqTipoActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reqTipoActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "reqTipoActoGenerator", sequenceName="req_tipo_acto_seq")
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @ManyToOne(optional = false)
    @NotNull
    private TipoActo tipoActo;

    @ManyToOne(optional = false)
    @NotNull
    private Requisito requisito;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public ReqTipoActo cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public TipoActo getTipoActo() {
        return this.tipoActo;
    }

    public ReqTipoActo tipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
        return this;
    }

    public void setTipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
    }

    public Requisito getRequisito() {
        return requisito;
    }

    public ReqTipoActo requisito(Requisito requisito) {
        this.requisito = requisito;
        return this;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReqClasifActo reqClasifActo = (ReqClasifActo) o;
        if (reqClasifActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reqClasifActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReqClasifActo{" +
            "id=" + getId() +
            ", cantidad='" + getCantidad() + "'" +
            "}";
    }
}
