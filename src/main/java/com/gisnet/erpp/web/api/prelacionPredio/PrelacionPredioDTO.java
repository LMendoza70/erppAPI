package com.gisnet.erpp.web.api.prelacionPredio;

import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.PrelacionPredio;

public class PrelacionPredioDTO {
	private PrelacionPredio prelacionPredio;
	private Antecedente antecedente;
	public PrelacionPredio getPrelacionPredio() {
		return prelacionPredio;
	}
	public void setPrelacionPredio(PrelacionPredio prelacionPredio) {
		this.prelacionPredio = prelacionPredio;
	}
	public Antecedente getAntecedente() {
		return antecedente;
	}
	public void setAntecedente(Antecedente antecedente) {
		this.antecedente = antecedente;
	}
	
	

}
