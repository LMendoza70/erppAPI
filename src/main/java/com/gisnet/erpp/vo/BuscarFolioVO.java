package com.gisnet.erpp.vo;

public class BuscarFolioVO {

    private Long oficinaId;
    private Integer folio;
    private Integer folioRealAnterior;
    private Integer auxiliar;

	public Long getOficinaId() {
		return oficinaId;
    }
    
	public void setOficinaId(Long oficinaId) {
		this.oficinaId = oficinaId;
    }
    
	public Integer getFolio() {
		return folio;
    }
    
	public void setFolio(Integer folio) {
		this.folio = folio;
    }
    
	public Integer getFolioRealAnterior() {
		return folioRealAnterior;
    }
    
	public void setFolioRealAnterior(Integer folioRealAnterior) {
		this.folioRealAnterior = folioRealAnterior;
    }
    
	public Integer getAuxiliar() {
		return auxiliar;
    }
    
	public void setAuxiliar(Integer auxiliar) {
		this.auxiliar = auxiliar;
    }
        
}