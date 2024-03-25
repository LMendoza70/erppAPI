package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ReciboServicio.
 */
@Entity
@Table(name = "recibo_servicio")
public class ReciboServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reciboServicioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "reciboServicioGenerator", sequenceName="recibo_servicio_seq")
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "subtotal", precision=10, scale=2)
    private BigDecimal subtotal;

    @Column(name = "valor_pecunario")
    private Boolean valorPecunario;

    @OneToMany(mappedBy = "reciboServicio")
    @JsonIgnore
    private Set<ReciboServicioCampo> reciboServicioCamposParaReciboServicios = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Recibo recibo;

    @ManyToOne(optional = false)
    @NotNull
    private Servicio servicio;

    @ManyToOne
    private SubTipoActo subTipoActo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public ReciboServicio cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public ReciboServicio subtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Boolean isValorPecunario() {
        return valorPecunario;
    }

    public ReciboServicio valorPecunario(Boolean valorPecunario) {
        this.valorPecunario = valorPecunario;
        return this;
    }

    public void setValorPecunario(Boolean valorPecunario) {
        this.valorPecunario = valorPecunario;
    }

    public Set<ReciboServicioCampo> getReciboServicioCamposParaReciboServicios() {
        return reciboServicioCamposParaReciboServicios;
    }

    public ReciboServicio reciboServicioCamposParaReciboServicios(Set<ReciboServicioCampo> reciboServicioCampos) {
        this.reciboServicioCamposParaReciboServicios = reciboServicioCampos;
        return this;
    }

    public ReciboServicio addReciboServicioCamposParaReciboServicio(ReciboServicioCampo reciboServicioCampo) {
        this.reciboServicioCamposParaReciboServicios.add(reciboServicioCampo);
        reciboServicioCampo.setReciboServicio(this);
        return this;
    }

    public ReciboServicio removeReciboServicioCamposParaReciboServicio(ReciboServicioCampo reciboServicioCampo) {
        this.reciboServicioCamposParaReciboServicios.remove(reciboServicioCampo);
        reciboServicioCampo.setReciboServicio(null);
        return this;
    }

    public void setReciboServicioCamposParaReciboServicios(Set<ReciboServicioCampo> reciboServicioCampos) {
        this.reciboServicioCamposParaReciboServicios = reciboServicioCampos;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public ReciboServicio recibo(Recibo recibo) {
        this.recibo = recibo;
        return this;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public ReciboServicio servicio(Servicio servicio) {
        this.servicio = servicio;
        return this;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public SubTipoActo getSubTipoActo() {
        return subTipoActo;
    }

    public ReciboServicio subTipoActo(SubTipoActo subTipoActo) {
        this.subTipoActo = subTipoActo;
        return this;
    }

    public void setSubTipoActo(SubTipoActo subTipoActo) {
        this.subTipoActo = subTipoActo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReciboServicio reciboServicio = (ReciboServicio) o;
        if (reciboServicio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reciboServicio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReciboServicio{" +
            "id=" + getId() +
            ", cantidad='" + getCantidad() + "'" +
            ", subtotal='" + getSubtotal() + "'" +
            ", valorPecunario='" + isValorPecunario() + "'" +
            "}";
    }
}
