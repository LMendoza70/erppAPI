package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.Prelacion;

public class MuebleAnteVO {

    private Mueble mueble;
    private AntecedenteVO antecedente;
    private Prelacion prelacion;




    public Mueble getMueble(){

        return this.mueble;
    }

    public void setMueble(Mueble mueble ){

        this.mueble=mueble;
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
