package com.gisnet.erpp.web.api.predio;

import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.vo.caratula.PropietarioVO;

public class PredioDTO extends Predio{
	private Colindancia[] colindancias;
	private PropietarioVO[] titulares;

	public Colindancia[] getColindancias() {
		return colindancias;
	}

	public void setColindancias(Colindancia[] colindancias) {
		this.colindancias = colindancias;
	}

	public PropietarioVO[] getTitulares() {
		return titulares;
	}

	public void setTitulares(PropietarioVO[] titulares) {
		this.titulares = titulares;
	}

}
