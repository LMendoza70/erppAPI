package com.gisnet.erpp.web.api.aviso;

import java.time.Instant;
import java.util.Date;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Aviso;

public class AvisoDTO {
	private Long id;
	private Date fechaIni;
	private Date fechaExp;
	private Acto acto;
	private Boolean vigente;

	public AvisoDTO() {
	}

	public AvisoDTO(Aviso aviso) {
		if (aviso == null) {
			return;
		}

		this.id = aviso.getId();
		this.fechaIni = aviso.getFechaIni();
		this.fechaExp = aviso.getFechaExp();
		this.acto = aviso.getActo();
		this.vigente = avisoEsVigente(aviso);
	}

	private Boolean avisoEsVigente(Aviso aviso) {
		if (aviso.getFechaExp() == null) {
			return false;
		}

		Instant hoy = Instant.now();
		Instant exp = aviso.getFechaExp().toInstant();

		return hoy.isBefore(exp);
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
	public Date getFechaExp() {
		return fechaExp;
	}
	public void setFechaExp(Date fechaExp) {
		this.fechaExp = fechaExp;
	}
	public Acto getActo() {
		return acto;
	}
	public void setActo(Acto acto) {
		this.acto = acto;
	}
	public Boolean getVigente() {
		return vigente;
	}
	public void setVigente(Boolean vigente) {
		this.vigente = vigente;
	}

}
