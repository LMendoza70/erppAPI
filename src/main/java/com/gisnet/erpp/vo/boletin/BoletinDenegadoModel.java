package com.gisnet.erpp.vo.boletin;


public class BoletinDenegadoModel{
    String entrada;
    String motivo;
    String fechaDeNegacion;
    String observacion;
    String folio;
    String fechaTermino;
    String fundamento;

    public void setEntrada(String entrada){
        this.entrada= entrada;
    }

    public String getEntrada(){
        return this.entrada;
    }

    public void setMotivo(String motivo){
        this.motivo=motivo;

    }
    public String getMotivo(){
        return this.motivo;
    }

    public void setFechaDeNegacion(String fechaDeNegacion){
        this.fechaDeNegacion = fechaDeNegacion;
    }

    public String getFechaDeNegacion(){
        return this.fechaDeNegacion;
    }

    public void setObservacion(String observacion){
        this.observacion=observacion;
    }

    public String getObservacion(){
        return this.observacion;
    }

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getFechaTermino() {
		return fechaTermino;
	}

	public void setFechaTermino(String fechaTermino) {
		this.fechaTermino = fechaTermino;
	}

	public String getFundamento() {
		return fundamento;
	}

	public void setFundamento(String fundamento) {
		this.fundamento = fundamento;
	}

    
}