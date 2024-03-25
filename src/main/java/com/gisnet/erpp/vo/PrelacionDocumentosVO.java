package com.gisnet.erpp.vo;

import java.util.List;

import com.gisnet.erpp.domain.Prelacion;


public class PrelacionDocumentosVO {

    private Prelacion prelacion;
	private List<RequisitoVO> requisitoVO;
	private List<BitacoraDocumentoEntradaVO> bitacoraVO;
	
    public Prelacion getPrelacion() {
		return prelacion;
	}
	
	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}
	
	public List<RequisitoVO> getRequisitoVO() {
		return requisitoVO;
	}
	
	public void setRequisitoVO(List<RequisitoVO> requisitoVO) {
		this.requisitoVO = requisitoVO;
	}

	public List<BitacoraDocumentoEntradaVO> getBitacoraVO() {
		return bitacoraVO;
	}

	public void setBitacoraVO(List<BitacoraDocumentoEntradaVO> bitacoraVO) {
		this.bitacoraVO = bitacoraVO;
	} 
	
	
}