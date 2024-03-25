package com.gisnet.erpp.vo;


import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.CampoCotizador;
import com.gisnet.erpp.domain.ClasifActo;
import com.gisnet.erpp.domain.ServicioCosto;
import com.gisnet.erpp.domain.TipoActo;

public class ServicioVO {

	private Long id;
	private CampoCotizador[] campos;
    private TipoActo tipoActo;
    private ClasifActo clasifActo;
    private Area area;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CampoCotizador[] getCampos() {
		return campos;
	}
	public void setCampos(CampoCotizador[] campos) {
		this.campos = campos;
	}
	public TipoActo getTipoActo() {
		return tipoActo;
	}
	public void setTipoActo(TipoActo tipoActo) {
		this.tipoActo = tipoActo;
	}
	public ClasifActo getClasifActo() {
		return clasifActo;
	}
	public void setClasifActo(ClasifActo clasifActo) {
		this.clasifActo = clasifActo;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}

}
