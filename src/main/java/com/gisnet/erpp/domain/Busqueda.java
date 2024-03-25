package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Busqueda.
 */
@Entity
@Table(name = "busqueda")
public class Busqueda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "busquedaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "busquedaGenerator", sequenceName="busqueda_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Prelacion prelacion;

    @OneToMany(mappedBy = "busqueda")
    @JsonIgnore
    private Set<DetalleBusqueda> detalleBusquedas = new HashSet<>();

    //@ManyToOne(optional = true)
    //@NotNull
    @Column(name="TIPO_BUSQUEDA_ID")
    private Integer tipoBusqueda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prelacion getPrelacion() {
        return prelacion;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public Busqueda prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public Integer getTipoBusqueda() {
        return tipoBusqueda;
    }

    public void setTipoBusqueda(Integer tipoBusqueda) {
        this.tipoBusqueda= tipoBusqueda;
    }

    public Set<DetalleBusqueda> getDetalleBusquedas() {
        return detalleBusquedas;
    }

    public void setDetalleBusquedas(Set<DetalleBusqueda> detalleBusquedas) {
        this.detalleBusquedas = detalleBusquedas;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
