package com.gisnet.erpp.vo;


import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.Prelacion;

public class FolioSeccionAuxiliarAnteVO {

    private FolioSeccionAuxiliar folioSeccionAuxiliar;
    private AntecedenteVO antecedente;
    private Prelacion prelacion;




    public FolioSeccionAuxiliar getFolioSeccionAuxiliar(){

        return this.folioSeccionAuxiliar;
    }

    public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar ){

        this.folioSeccionAuxiliar=folioSeccionAuxiliar;
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



}
