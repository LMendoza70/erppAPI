package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.EstadoCivil;
import com.gisnet.erpp.domain.Regimen;

public class BusquedaPersonaVO extends PersonaVO {
    private String      denominacion;
    private Boolean     fisica;
    private Regimen     regimen;
    private EstadoCivil estadoCivil;
    private Long        busquedaId;

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String val) {
        this.denominacion = val ==null? "": val.toUpperCase();;
    }

    public Boolean isFisica() {
        return fisica;
    }

    public void setFisica(Boolean fisica) {
        this.fisica = fisica;
    }

    public Long getBusquedaId() {
        return busquedaId;
    }

    public void setBusquedaId(Long busquedaId) {
        this.busquedaId = busquedaId;
    }

    public Boolean getFisica() {
        return fisica;
    }

    public Regimen getRegimen() {
        return regimen;
    }

    public void setRegimen(Regimen regimen) {
        this.regimen = regimen;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
}
