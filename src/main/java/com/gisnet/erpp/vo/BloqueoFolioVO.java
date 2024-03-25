package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.Motivo;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.Usuario;

public class BloqueoFolioVO {
	private Long       id;
	private PredioVO   predio;
	private String     observaciones;
	private Motivo     motivo;
	private Integer    version;
	private Usuario    usuario;
	private Long       tipoFolio;
	private PersonaJuridica personaJuridica;
	private FolioSeccionAuxiliar folioSeccionAuxiliar;
	private Mueble mueble;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public PredioVO getPredio() {
		return predio;
	}
	public void setPredio(PredioVO predio) {
		this.predio = predio;
    }
    
    public Motivo getMotivo() {
		return motivo;
	}
	public void setMotivo(Motivo motivo) {
		this.motivo = motivo;
	}

	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}


	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Long getTipoFolio() {
		return tipoFolio;
	}
	public void setTipoFolio(Long tipoFolio) {
		this.tipoFolio = tipoFolio;
	}
	public PersonaJuridica getPersonaJuridica() {
		return personaJuridica;
	}
	public void setPersonaJuridica(PersonaJuridica personaJuridica) {
		this.personaJuridica = personaJuridica;
	}
	public FolioSeccionAuxiliar getFolioSeccionAuxiliar() {
		return folioSeccionAuxiliar;
	}
	public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
		this.folioSeccionAuxiliar = folioSeccionAuxiliar;
	}
	public Mueble getMueble() {
		return mueble;
	}
	public void setMueble(Mueble mueble) {
		this.mueble = mueble;
	}

}
