package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.Area;

public class ServicioAndSubVO {

	private Long   id;
    private Long   idServicio;
    private String servicio;
    private Long   idSubServicio;
	private String subServicio;
	private Area   area;



    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

    public Long getIdServicio() {
		return idServicio;
	}
	public void setIdServicio(Long idServicio) {
		this.idServicio = idServicio;
	}
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public Long getIdSubServicio() {
		return idSubServicio;
	}
	public void setIdSubServicio(Long idSubServicio) {
		this.idSubServicio = idSubServicio;
	}
	public String getSubServicio() {
		return subServicio;
	}
	public void setSubServicio(String subServicio) {
		this.subServicio = subServicio;
	}

	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}

}