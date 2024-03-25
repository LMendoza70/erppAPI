package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.BitacoraImpresion;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.vo.*;

public class PrelacionBitacoraImpresionVO {
	
	private Boolean ventanilla;
	private Prelacion prelacion;
	private BitacoraImpresion bitacoraImpresion;
		
	
	public Boolean getVentanilla() {
		return ventanilla;
	}
	public void setVentanilla(Boolean ventanilla) {
		this.ventanilla = ventanilla;
	}
	public Prelacion getPrelacion() {
		return prelacion;
	}
	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}
	public BitacoraImpresion getBitacoraImpresion() {
		return bitacoraImpresion;
	}
	public void setBitacoraImpresion(BitacoraImpresion bitacoraImpresion) {
		this.bitacoraImpresion = bitacoraImpresion;
	}

}
