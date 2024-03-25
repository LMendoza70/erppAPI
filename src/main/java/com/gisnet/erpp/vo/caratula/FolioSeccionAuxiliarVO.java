package com.gisnet.erpp.vo.caratula;

import java.util.List;

public class FolioSeccionAuxiliarVO {
	
	private AntecedenteVO antecedente;
	
	private Long folioSeccionAuxiliarId;
	
	private boolean primerRegistro;
	private Integer noFolio;
	
	private List<ActoVO> sumatoriaActosPorClasifActo;
	
	private List<TramiteVO> tramites;
	
	private List<DevArt71VO> devoluciones;
	
	private Boolean bloqueado = new Boolean(false);
	
	private boolean enProceso;

	public AntecedenteVO getAntecedente() {
		return antecedente;
	}

	public void setAntecedente(AntecedenteVO antecedente) {
		this.antecedente = antecedente;
	}

	public List<ActoVO> getSumatoriaActosPorClasifActo() {
		return sumatoriaActosPorClasifActo;
	}

	public void setSumatoriaActosPorClasifActo(List<ActoVO> sumatoriaActosPorClasifActo) {
		this.sumatoriaActosPorClasifActo = sumatoriaActosPorClasifActo;
	}

	public List<TramiteVO> getTramites() {
		return tramites;
	}

	public void setTramites(List<TramiteVO> tramites) {
		this.tramites = tramites;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public boolean isPrimerRegistro() {
		return primerRegistro;
	}

	public void setPrimerRegistro(boolean primerRegistro) {
		this.primerRegistro = primerRegistro;
	}

	public Integer getNoFolio() {
		return noFolio;
	}

	public void setNoFolio(Integer noFolio) {
		this.noFolio = noFolio;
	}

	public Long getFolioSeccionAuxiliarId() {
		return folioSeccionAuxiliarId;
	}

	public void setFolioSeccionAuxiliarId(Long folioSeccionAuxiliarId) {
		this.folioSeccionAuxiliarId = folioSeccionAuxiliarId;
	}

	public List<DevArt71VO> getDevoluciones() {
		return devoluciones;
	}

	public void setDevoluciones(List<DevArt71VO> devoluciones) {
		this.devoluciones = devoluciones;
	}

	public boolean isEnProceso() {
		return enProceso;
	}

	public void setEnProceso(boolean enProceso) {
		this.enProceso = enProceso;
	}

}
