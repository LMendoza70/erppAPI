package com.gisnet.erpp.web.api.firma;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;

import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;


public class FirmaDTO {	
	long secuencia;
    String politica;
    String digestionTS;
    long secuenciaTS;
    Date estampilla;
    

    public FirmaDTO() {
        
    }

    public FirmaDTO(String estampa, String dateFormat) {
    	this.secuencia = Long.parseLong(estampa);
        
    }
    
    
    @Override
    public String toString() {
        return "ActoFirma{" +
            ", secuencia='" + getSecuencia() + "'" +
            ", politica='" + getPolitica() + "'" +
            ", digestion='" + getDigestionTS() + "'" +
            ", secuenciaTS='" + getSecuenciaTS() + "'" +
            ", estampilla='" + getEstampilla() + "'" +
            "}";
    }

	public long getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(long secuencia) {
		this.secuencia = secuencia;
	}

	public String getPolitica() {
		return politica;
	}

	public void setPolitica(String politica) {
		this.politica = politica;
	}

	public String getDigestionTS() {
		return digestionTS;
	}

	public void setDigestionTS(String digestionTS) {
		this.digestionTS = digestionTS;
	}

	public long getSecuenciaTS() {
		return secuenciaTS;
	}

	public void setSecuenciaTS(long secuenciaTS) {
		this.secuenciaTS = secuenciaTS;
	}

	public Date getEstampilla() {
		return estampilla;
	}

	public void setEstampilla(Date estampilla) {
		this.estampilla = estampilla;
	}
    
    
    

}
