package com.gisnet.erpp.web.api.recibooficial;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.gisnet.erpp.util.UtilFecha;

public class ReciboOficialDTO {
	private Date fecha;
	private String total;
	private String nombre;
	private String caja;
	private String operacion;
	private String rec;
	private String municipio;
	private String estado;

	java.util.List<ReciboOficialDetalleDTO> detalles;

	public ReciboOficialDTO() {

	}

	public ReciboOficialDTO(String header, String detalle, String dateFormat) {
		String[] tokens = header.split(Pattern.quote("|"));

		this.fecha = UtilFecha.toDateTime(tokens[0], dateFormat);
		this.total = tokens[1];
		this.nombre = tokens[2];
		this.caja = tokens[3];
		this.operacion = tokens[4];
		this.rec = tokens[5];
		this.municipio = tokens[6];
		this.estado = tokens[7];
		
		
		this.detalles = new ArrayList<ReciboOficialDetalleDTO>();

		String[] det = detalle.split("@");
		for (int i = 0; i < det.length; i++) {
			detalles.add(new ReciboOficialDetalleDTO(det[i], dateFormat));
		}

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ReciboOficalDTO{");
		sb.append(" fecha=").append(fecha);
		sb.append(", total=").append(total);
		sb.append(", nombre=").append(nombre);
		sb.append(", caja=").append(caja);
		sb.append(", operacion=").append(operacion);
		sb.append(", rec=").append(rec);
		sb.append(", municipio=").append(municipio);
		sb.append(", estado=").append(estado);
		if (detalles != null)
			for (Iterator iterator = detalles.iterator(); iterator.hasNext();) {
				ReciboOficialDetalleDTO reciboOficalDetalleDTO = (ReciboOficialDetalleDTO) iterator.next();
				sb.append(reciboOficalDetalleDTO);
			}
		sb.append("}");
		return sb.toString();
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCaja() {
		return caja;
	}

	public void setCaja(String caja) {
		this.caja = caja;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getRec() {
		return rec;
	}

	public void setRec(String rec) {
		this.rec = rec;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public java.util.List<ReciboOficialDetalleDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(java.util.List<ReciboOficialDetalleDTO> detalles) {
		this.detalles = detalles;
	}

}
