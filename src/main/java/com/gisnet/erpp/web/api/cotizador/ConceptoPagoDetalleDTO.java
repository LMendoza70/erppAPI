package com.gisnet.erpp.web.api.cotizador;

import java.math.BigDecimal;

public class ConceptoPagoDetalleDTO {

	private Long id;
	private String nombre;
	private String clave;
	private BigDecimal costo;
	private int cantidad;
	private boolean tarifaUnitaria;	
	private String mensaje;
	
	public boolean isTarifaUnitaria() {
		return tarifaUnitaria;
	}

	public void setTarifaUnitaria(boolean tarifaUnitaria) {
		this.tarifaUnitaria = tarifaUnitaria;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id= ").append(id);		
		sb.append(", nombre=").append(nombre);
		sb.append(", clave=").append(clave);
		sb.append(", costo=").append(costo);
		sb.append(", cantidad=").append(cantidad);
		sb.append(", mensaje=").append(mensaje);
		return sb.toString();
	}

}
