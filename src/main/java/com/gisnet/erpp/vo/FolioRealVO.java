package com.gisnet.erpp.vo;

import java.util.Set;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.Predio;

public class FolioRealVO {
	private Predio predio;
	private PersonaJuridica personaJuridica;
	private Set<Acto> actos;

	public Predio getPredio() {
		return predio;
	}

	public void setPredio(Predio predio) {
		this.predio = predio;
	}

	public PersonaJuridica getPersonaJuridica() {
		return personaJuridica;
	}

	public void setPersonaJuridica(PersonaJuridica personaJuridica) {
		this.personaJuridica = personaJuridica;
	}

	public Set<Acto> getActos() {
		return actos;
	}

	public void setActos(Set<Acto> actos) {
		this.actos = actos;
	}
}
