package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.PersonaJuridica;

public class FoliosAnteVO {

	private PredioVO predio;
	private Mueble mueble;
	private FolioSeccionAuxiliar folioSeccionAuxiliar;
	private PersonaJuridica personaJuridica;
    private AntecedenteVO antecedente;
    private Boolean valores;
    private String enProceso;


	public PredioVO getPredio() {
		return predio;
	}
	public void setPredio(PredioVO predio) {
		this.predio = predio;
	}
	public Mueble getMueble() {
		return mueble;
	}
	public void setMueble(Mueble mueble) {
		this.mueble = mueble;
	}
	public FolioSeccionAuxiliar getFolioSeccionAuxiliar() {
		return folioSeccionAuxiliar;
	}
	public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
		this.folioSeccionAuxiliar = folioSeccionAuxiliar;
	}
	public PersonaJuridica getPersonaJuridica() {
		return personaJuridica;
	}
	public void setPersonaJuridica(PersonaJuridica personaJuridica) {
		this.personaJuridica = personaJuridica;
	}
	public AntecedenteVO getAntecedente() {
		return antecedente;
	}
	public void setAntecedente(AntecedenteVO antecedente) {
		this.antecedente = antecedente;
	}
	public Boolean getValores() {
		return valores;
	}
	public void setValores(Boolean valores) {
		this.valores = valores;
	}
	public String getEnProceso() {
		return enProceso;
	}
	public void setEnProceso(String enProceso) {
		this.enProceso = enProceso;
	}
	

}
