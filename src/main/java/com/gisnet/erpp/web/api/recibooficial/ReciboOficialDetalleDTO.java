package com.gisnet.erpp.web.api.recibooficial;

import java.util.Date;
import java.util.regex.Pattern;

import com.gisnet.erpp.util.UtilFecha;


public class ReciboOficialDetalleDTO {
	private Long id;
	private String descripcion;
	private Date fecha;
	private String total;
	//se repite nombre
	private String concepto;
	//se repite caja
	//se repite operacion
	    

    public ReciboOficialDetalleDTO() {
        
    }

	public ReciboOficialDetalleDTO(String detalle, String dateFormat) {
    	String[] tokens = detalle.split(Pattern.quote("|"));
    	 
    	this.descripcion = tokens[0];
    	this.fecha = UtilFecha.toDateTime(tokens[1], dateFormat);
        this.total = tokens[2];
        //se repite nombre
        this.concepto = tokens[4];        
        
    }
    
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("ReciboOficalDetalleDTO{");
    	sb.append(" descripcion=").append(descripcion);
    	sb.append(", fecha=").append(fecha);
    	sb.append(", total=").append(total);
    	sb.append(", concepto=").append(concepto);
        sb.append("}");
        return sb.toString();
    }

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    
    

}
