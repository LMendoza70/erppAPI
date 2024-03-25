package com.gisnet.erpp.vo;

import com.gisnet.erpp.vo.prelacion.TitularModel;

import java.util.Date;
import java.util.List;

public class AvisoCautelarVO {
    Integer            folio;

    String escrituraNo;
    String volumen;
    String libro;
    String foja;
    String porActa;
    String porOficio;


    Date               fecha;
    String diaNumero;
    String mesLetra;
    String anio;

    String             municipio;
    String             estado;
    List<TitularModel> adquirientes;
    Date               fechaCautelar;

    String licenciado;
    String noFedatario;
    String enajenante;
    String enCalidad;
    Double cantidad;

    String diaRegistro;
    String mesLetraRegistro;
    String anioRegistro;
    String reviso;

    public String getDiaRegistro() {
        return diaRegistro;
    }

    public void setDiaRegistro(String diaRegistro) {
        this.diaRegistro = diaRegistro;
    }

    public String getMesLetraRegistro() {
        return mesLetraRegistro;
    }

    public void setMesLetraRegistro(String mesLetraRegistro) {
        this.mesLetraRegistro = mesLetraRegistro;
    }

    public String getAnioRegistro() {
        return anioRegistro;
    }

    public void setAnioRegistro(String anioRegistro) {
        this.anioRegistro = anioRegistro;
    }

    public String getReviso() {
        return reviso;
    }

    public void setReviso(String reviso) {
        this.reviso = reviso;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDiaNumero() {
        return diaNumero;
    }

    public void setDiaNumero(String diaNumero) {
        this.diaNumero = diaNumero;
    }

    public String getMesLetra() {
        return mesLetra;
    }

    public void setMesLetra(String mesLetra) {
        this.mesLetra = mesLetra;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<TitularModel> getAdquirientes() {
        return adquirientes;
    }

    public void setAdquirientes(List<TitularModel> adquirientes) {
        this.adquirientes = adquirientes;
    }

    public Date getFechaCautelar() {
        return fechaCautelar;
    }

    public void setFechaCautelar(Date fechaCautelar) {
        this.fechaCautelar = fechaCautelar;
    }

    public String getLicenciado() {
        return licenciado;
    }

    public void setLicenciado(String licenciado) {
        this.licenciado = licenciado;
    }

    public String getNoFedatario() {
        return noFedatario;
    }

    public void setNoFedatario(String noFedatario) {
        this.noFedatario = noFedatario;
    }

    public String getEnajenante() {
        return enajenante;
    }

    public void setEnajenante(String enajenante) {
        this.enajenante = enajenante;
    }

    public String getEnCalidad() {
        return enCalidad;
    }

    public void setEnCalidad(String enCalidad) {
        this.enCalidad = enCalidad;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getEscrituraNo() {
        return escrituraNo;
    }

    public void setEscrituraNo(String escrituraNo) {
        this.escrituraNo = escrituraNo;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getFoja() {
        return foja;
    }

    public void setFoja(String foja) {
        this.foja = foja;
    }

    public String getPorActa() {
        return porActa;
    }

    public void setPorActa(String porActa) {
        this.porActa = porActa;
    }

    public String getPorOficio() {
        return porOficio;
    }

    public void setPorOficio(String porOficio) {
        this.porOficio = porOficio;
    }
}
