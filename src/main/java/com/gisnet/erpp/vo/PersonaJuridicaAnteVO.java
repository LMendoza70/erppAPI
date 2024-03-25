package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.Prelacion;

public class PersonaJuridicaAnteVO {

    private PersonaJuridica personaJuridica;
    private AntecedenteVO antecedente;
    private Prelacion prelacion;
    private Boolean seleccionado;




    public PersonaJuridica getPersonaJuridica(){

        return this.personaJuridica;
    }

    public void setPersonaJuridica(PersonaJuridica personaJuridica ){

        this.personaJuridica=personaJuridica;
    }



    public AntecedenteVO getAntecedente(){

        return this.antecedente;
    }

    public void setAntecedente(AntecedenteVO antecedente ){

        this.antecedente=antecedente;
    }

	public Prelacion getPrelacion() {
		return prelacion;
	}

	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}

    public Boolean getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(Boolean seleccionado) {
		this.seleccionado = seleccionado;
	}


}
