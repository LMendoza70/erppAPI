package com.gisnet.erpp.web.api.firma;

public class FirmaRequestDTO {
	private String original;
	private String pkcs7;
	private String algoritmo;
	
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public String getPkcs7() {
		return pkcs7;
	}
	public void setPkcs7(String pkcs7) {
		this.pkcs7 = pkcs7;
	}
	
	public String getAlgoritmo() {
		return algoritmo;
	}
	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}

}
