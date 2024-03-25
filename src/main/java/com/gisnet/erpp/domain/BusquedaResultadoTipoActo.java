package com.gisnet.erpp.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Entity
@Table(name = "busqueda_resultado_tipo_acto")
public class BusquedaResultadoTipoActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "busquedaResultadoTipoActoGenerator")
    @SequenceGenerator(allocationSize = 1,name = "busquedaResultadoTipoActoGenerator", sequenceName="busqueda_resultado_tipo_acto_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private BusquedaResultado busquedaResultado;

    @ManyToOne(optional = false)
    @NotNull
    private TipoActo tipoActo;

    private Boolean caratulaGenerada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusquedaResultado getBusquedaResultado() {
        return busquedaResultado;
    }

    public void setBusquedaResultado(BusquedaResultado busquedaResultado) {
        this.busquedaResultado = busquedaResultado;
    }

    public TipoActo getTipoActo() {
        return tipoActo;
    }

    public void setTipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
    }
    
    
    public Boolean getCaratulaGenerada() {
        return caratulaGenerada;
    }

    public void setCaratulaGenerada(Boolean caratulaGenerada) {
        this.caratulaGenerada = caratulaGenerada;
    }
}
