package com.gisnet.erpp.service.materializacion;

public class FraccionVO {
	public long predioId;
	public double superficie;

	public FraccionVO() {
		superficie = 0;
	}
	
	public FraccionVO(long predioId) {
		this.predioId = predioId;
		superficie = 0;
	}

}
