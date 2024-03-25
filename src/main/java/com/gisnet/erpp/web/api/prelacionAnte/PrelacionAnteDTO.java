package com.gisnet.erpp.web.api.prelacionAnte;

import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PrelacionAnte;
import com.gisnet.erpp.domain.PrelacionAnteCapHist;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.web.api.predio.PredioDTO;

public class PrelacionAnteDTO {
	private PrelacionPredio prelacionPredio;
	private Antecedente antecedente;
	private PrelacionAnte prelacionAnte;
	private PrelacionAnteCapHist prelacionAnteCapHist;
	private PredioDTO predioDTO;
	private Predio predio;
	private Mueble mueble;
	private PersonaJuridica personaJuridica;
	private FolioSeccionAuxiliar folioSeccionAuxiliar;
	
	public PrelacionPredio getPrelacionPredio() {
		return prelacionPredio;
	}
	public void setPrelacionPredio(PrelacionPredio prelacionPredio) {
		this.prelacionPredio = prelacionPredio;
	}
	public PredioDTO getPredioDTO() {
		return predioDTO;
	}
	public void setPredioDTO(PredioDTO predioDTO) {
		this.predioDTO = predioDTO;
	}
	public Predio getPredio() {
		return predio;
	}
	public void setPredio(Predio predio) {
		this.predio = predio;
	}
	public Mueble getMueble() {
		return mueble;
	}
	public void setMueble(Mueble mueble) {
		this.mueble = mueble;
	}
	public PersonaJuridica getPersonaJuridica() {
		return personaJuridica;
	}
	public void setPersonaJuridica(PersonaJuridica personaJuridica) {
		this.personaJuridica = personaJuridica;
	}
	public FolioSeccionAuxiliar getFolioSeccionAuxiliar() {
		return folioSeccionAuxiliar;
	}
	public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
		this.folioSeccionAuxiliar = folioSeccionAuxiliar;
	}
	public Antecedente getAntecedente() {
		return antecedente;
	}
	public void setAntecedente(Antecedente antecedente) {
		this.antecedente = antecedente;
	}
	public PrelacionAnte getPrelacionAnte() {
		return prelacionAnte;
	}
	public void setPrelacionAnte(PrelacionAnte prelacionAnte) {
		this.prelacionAnte = prelacionAnte;
	}
	
	public PrelacionAnteCapHist getPrelacionAnteCapHist() {
		return prelacionAnteCapHist;
	}
	public void setPrelacionAnteCapHist(PrelacionAnteCapHist prelacionAnteCapHist) {
		this.prelacionAnteCapHist = prelacionAnteCapHist;
	}
	
	

}
