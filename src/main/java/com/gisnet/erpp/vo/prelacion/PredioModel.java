package com.gisnet.erpp.vo.prelacion;

public class	PredioModel {
	private int folio;
	private String folioFusion;
	private String noFolioFutuReg;
	private String noFolioAux;
	private String leyendaFolio;

	public int getFolio() {
		return folio;
	}

	public void setFolio(int folio) {
		this.folio = folio;
	}

	public void setFolioFusion(String folioFusion){
		this.folioFusion = folioFusion;
	}

	public String getFolioFusion(){
		return this.folioFusion;
	}

	public String getNoFolioFutuReg() {
		return noFolioFutuReg;
	}

	public void setNoFolioFutuReg(String noFolioFutuReg) {
		this.noFolioFutuReg = noFolioFutuReg;
	}

	public String getNoFolioAux() {
		return noFolioAux;
	}

	public void setNoFolioAux(String noFolioAux) {
		this.noFolioAux = noFolioAux;
	}

	public String getLeyendaFolio() {
		return leyendaFolio;
	}

	public void setLeyendaFolio(String leyendaFolio) {
		this.leyendaFolio = leyendaFolio;
	}
	
	
}
