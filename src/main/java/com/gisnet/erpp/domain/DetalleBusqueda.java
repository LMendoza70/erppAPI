package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A DetalleBusqueda.
 */
@Entity
@Table(name = "detalle_busqueda")
public class DetalleBusqueda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalleBusquedaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "detalleBusquedaGenerator", sequenceName="detalle_busqueda_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Busqueda busqueda;

    @Size(max = 50)
    @Column(name = "clave", length = 50)
    private String clave;

    @Size(max = 255)
    @Column(name = "valor", length = 255)
    private String valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Busqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(Busqueda busqueda) {
        this.busqueda = busqueda;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
