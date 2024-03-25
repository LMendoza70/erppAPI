package com.gisnet.erpp.web.api.cotizador;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import com.gisnet.erpp.domain.CampoCotizador;
import com.gisnet.erpp.domain.ClasificacionConcepto;
import com.gisnet.erpp.domain.ServiciosCotizador;
import com.gisnet.erpp.domain.TipoActo;

public class ConceptoPagoDTO {

	private Long id;
	private CampoCotizador[] campos;
	private TipoActo tipoActo;
	private String nombre;
	private String descParametros;
	private ServiciosCotizador serviciosCotizador;
	private String clasificacionConceptoNombre;
	private String servicioCotizadorNombre;	
	private Boolean tarifaUnitaria;
	private List<ConceptoPagoDetalleDTO> detalle;
	
	public BigDecimal getTotal(){
		BigDecimal total = BigDecimal.ZERO;
		if (detalle!=null){
			for (Iterator iterator = detalle.iterator(); iterator.hasNext();) {
				ConceptoPagoDetalleDTO conceptoPagoDetalleVO = (ConceptoPagoDetalleDTO) iterator.next();
				total = total.add(conceptoPagoDetalleVO.getCosto());
			}
		}
		return total;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getServicioCotizadorNombre() {
		return servicioCotizadorNombre;
	}

	public void setServicioCotizadorNombre(String servicioCotizadorNombre) {
		this.servicioCotizadorNombre = servicioCotizadorNombre;
	}

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

	public String getDescParametros() {
		return descParametros;
	}

	public void setDescParametros(String descParametros) {
		this.descParametros = descParametros;
	}
	
	public ServiciosCotizador getServiciosCotizador() {
		return serviciosCotizador;
	}

	public void setServiciosCotizador(ServiciosCotizador serviciosCotizador) {
		this.serviciosCotizador = serviciosCotizador;
	}


	public Boolean getTarifaUnitaria() {
		return tarifaUnitaria==null?false:tarifaUnitaria;
	}

	public void setTarifaUnitaria(Boolean tarifaUnitaria) {
		this.tarifaUnitaria = tarifaUnitaria;
	}
	
	public List<ConceptoPagoDetalleDTO> getDetalle() {
		return detalle;
	}


	public void setDetalle(List<ConceptoPagoDetalleDTO> detalle) {
		this.detalle = detalle;
	}
		
	public String getClasificacionConceptoNombre() {
		return clasificacionConceptoNombre;
	}


	public void setClasificacionConceptoNombre(String clasificacionConceptoNombre) {
		this.clasificacionConceptoNombre = clasificacionConceptoNombre;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id= ").append(id);
		if (campos != null && campos.length > 0) {
			for (int i = 0; i < campos.length; i++) {
				CampoCotizador campoCotizador = campos[i];
				sb.append(", campo =").append(campoCotizador);
			}
		}
		
		if (detalle != null && detalle.size() > 0) {
			for (ConceptoPagoDetalleDTO conceptoPagoDetalleDTO : detalle ) {				
				sb.append(", conceptoPagoDetalleDTO =").append(conceptoPagoDetalleDTO);
			}
		}
		sb.append(", tipoActo=").append(tipoActo);
		sb.append(", descParametros=").append(descParametros);
		sb.append(", serviciosCotizador=").append(serviciosCotizador);
		sb.append(", clasificacionConceptoNombre=").append(clasificacionConceptoNombre);
		return sb.toString();
	}

}
