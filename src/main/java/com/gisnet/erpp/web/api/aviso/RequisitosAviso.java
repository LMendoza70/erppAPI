package com.gisnet.erpp.web.api.aviso;

public class RequisitosAviso {
	private boolean cumpleNotario = false;
	private boolean cumpleActos = false;
	private boolean cumplePartes = false;
	private boolean cumpleDocumento = true;
	public boolean isCumpleNotario() {
		return cumpleNotario;
	}
	public void setCumpleNotario(boolean cumpleNotario) {
		this.cumpleNotario = cumpleNotario;
	}
	public boolean isCumpleActos() {
		return cumpleActos;
	}
	public void setCumpleActos(boolean cumpleActos) {
		this.cumpleActos = cumpleActos;
	}
	public boolean isCumplePartes() {
		return cumplePartes;
	}
	public void setCumplePartes(boolean cumplePartes) {
		this.cumplePartes = cumplePartes;
	}
	public boolean isCumpleDocumento() {
		return cumpleDocumento;
	}
	public void setCumpleDocumento(boolean cumpleDocumento) {
		this.cumpleDocumento = cumpleDocumento;
	}

	public boolean cumpleAviso() {
		return cumpleNotario &&
				cumpleActos &&
				cumplePartes &&
				cumpleDocumento;
	}

}
