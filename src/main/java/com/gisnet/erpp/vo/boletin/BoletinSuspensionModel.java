package com.gisnet.erpp.vo.boletin;


public class BoletinSuspensionModel{
	String entrada;
	String folio;    
	String fechaFirma;
    String fechaDeSuspension;    
	String fechaTermino;
	String motivo;
	String fundamento;
	String observaciones;
	

    public void setEntrada(String entrada){
        this.entrada= entrada;
	}
	
	public String getEntrada(){
        return this.entrada;
	}
	
	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getFechaFirma() {
		return fechaFirma;
	}

	public void setFechaFirma(String fechaFirma){
		this.fechaFirma= fechaFirma;
	}

    public String getFechaDeSuspension() {
		return fechaDeSuspension;
	}

	public void setFechaDeSuspension(String fechaDeSuspension) {
		this.fechaDeSuspension = fechaDeSuspension;
	}

	public String getFechaTermino() {
		return fechaTermino;
	}

	public void setFechaTermino(String fechaTermino) {
		this.fechaTermino = fechaTermino;
	}

	public void setMotivo(String motivo){
        this.motivo=motivo;

    }
    public String getMotivo(){
        return this.motivo;
    }

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getFundamento() {
		return fundamento;
	}

	public void setFundamento(String fundamento) {
		this.fundamento = fundamento;
	}

    
}