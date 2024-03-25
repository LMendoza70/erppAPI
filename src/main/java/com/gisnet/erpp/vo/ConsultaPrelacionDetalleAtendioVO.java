package com.gisnet.erpp.vo;


import com.gisnet.erpp.domain.Usuario;

public class ConsultaPrelacionDetalleAtendioVO {

	private Long id;
	private Usuario usuarioAnalista;
	private Usuario usuarioCalificador;
	private Usuario usuarioCoordinador;
	private Usuario usuarioVentanilla;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Usuario getUsuarioAnalista() {
		return usuarioAnalista;
	}
	public void setUsuarioAnalista(Usuario usuarioAnalista) {
		this.usuarioAnalista = usuarioAnalista;
	}
	public Usuario getUsuarioCalificador() {
		return usuarioCalificador;
	}
	public void setUsuarioCalificador(Usuario usuarioCalificador) {
		this.usuarioCalificador = usuarioCalificador;
	}
	public Usuario getUsuarioCoordinador() {
		return usuarioCoordinador;
	}
	public void setUsuarioCoordinador(Usuario usuarioCoordinador) {
		this.usuarioCoordinador = usuarioCoordinador;
	}
	public Usuario getUsuarioVentanilla() {
		return usuarioVentanilla;
	}
	public void setUsuarioVentanilla(Usuario usuarioVentanilla) {
		this.usuarioVentanilla = usuarioVentanilla;
	}
	
	
}
