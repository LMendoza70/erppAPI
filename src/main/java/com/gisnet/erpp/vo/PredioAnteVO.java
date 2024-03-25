package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.Libro;

public class PredioAnteVO {

    private PredioVO predio;
    private AntecedenteVO antecedente;
    private Libro libro;
    private Boolean seleccionado; 





    public PredioVO getPredio(){

        return this.predio;
    }

    public void setPredio(PredioVO predio ){

        this.predio=predio;
    }



    public AntecedenteVO getAntecedente(){

        return this.antecedente;
    }

    public void setAntecedente(AntecedenteVO antecedente ){

        this.antecedente=antecedente;
    }

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public Boolean getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(Boolean seleccionado) {
		this.seleccionado = seleccionado;
	}



}
