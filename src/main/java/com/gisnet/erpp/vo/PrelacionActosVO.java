package com.gisnet.erpp.vo;

import java.util.List;

import com.gisnet.erpp.domain.Prelacion;


public class PrelacionActosVO {

    private Prelacion prelacion;
    private List<ActoVO> actosVO;
	
    public Prelacion getPrelacion() {
		return prelacion;
	}
	
	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}
	
	public List<ActoVO> getActosVO() {
		return actosVO;
	}
	
	public void setActosVO(List<ActoVO> actosVO) {
		this.actosVO = actosVO;
	}    
}