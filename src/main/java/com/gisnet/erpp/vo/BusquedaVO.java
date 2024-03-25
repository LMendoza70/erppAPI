package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.Predio;

public class BusquedaVO {
    public Long              prelacion ;
    public Long              servicio;
    public Predio            predio;
    public BusquedaPersonaVO persona;

    public String predioStr;
    public Long busquedaPredioId;
    public Long busquedaPersonaId;

    public String getPredioVO() {
        return predioStr;
    }

    public void setPredioVO(String predioStr) {
        this.predioStr = predioStr;
    }

    public Long getPrelacion() {
        return prelacion;
    }

    public void setPrelacion(Long prelacion) {
        this.prelacion = prelacion;
    }

    public Long getServicio() {
        return servicio;
    }

    public void setServicio(Long servicio) {
        this.servicio = servicio;
    }

    public Predio getPredio() {
        return predio;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public BusquedaPersonaVO getPersona() {
        return persona;
    }

    public void setPersona(BusquedaPersonaVO persona) {
        this.persona = persona;
    }

    public String getPredioStr() {
        return predioStr;
    }

    public void setPredioStr(String predioStr) {
        this.predioStr = predioStr;
    }

    public Long getBusquedaPredioId() {
        return busquedaPredioId;
    }

    public void setBusquedaPredioId(Long busquedaPredioId) {
        this.busquedaPredioId = busquedaPredioId;
    }

    public Long getBusquedaPersonaId() {
        return busquedaPersonaId;
    }

    public void setBusquedaPersonaId(Long busquedaPersonaId) {
        this.busquedaPersonaId = busquedaPersonaId;
    }
}
