package com.gisnet.erpp.vo;

import java.util.Date;

public class ArchivoFirmaVO {

    private Long    id;
    private String  pkcs7;
    private Integer secuencia;
    private String  politica;
    private String  digestion;
    private Integer secuenciaTS;
    private Date    estampilla;

    private Long actoId;


    public Long getActoId() {
		return actoId;
	}

	public void setActoId(Long actoId) {
		this.actoId = actoId;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPkcs7() {
        return pkcs7;
    }



    public void setPkcs7(String pkcs7) {
        this.pkcs7 = pkcs7;
    }

    public Integer getSecuencia() {
        return secuencia;
    }



    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public String getPolitica() {
        return politica;
    }



    public void setPolitica(String politica) {
        this.politica = politica;
    }

    public String getDigestion() {
        return digestion;
    }

   

    public void setDigestion(String digestion) {
        this.digestion = digestion;
    }

    public Integer getSecuenciaTS() {
        return secuenciaTS;
    }

   

    public void setSecuenciaTS(Integer secuenciaTS) {
        this.secuenciaTS = secuenciaTS;
    }

    public Date getEstampilla() {
        return estampilla;
    }

   

    public void setEstampilla(Date estampilla) {
        this.estampilla = estampilla;
    }

    @Override
    public String toString(){
            return "Datos de la firma ->  Estampilla : "+this.estampilla+ " -  Politica : "+this.politica+ " -  Secuencia : "+this.secuencia +"  - SecuenciaTS : "+
            this.secuenciaTS+ " -  Digestion : "+this.digestion+ " - pkcs7 : "+this.pkcs7;
    }

}