package com.gisnet.erpp.web.api.imprimeprelacion.caratula;

import java.util.ArrayList;
import java.util.List;

import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.vo.caratula.AntecedenteVO;
import com.gisnet.erpp.vo.caratula.PredioVO;

public class CaratulaVO extends PredioVO {

	private List<AntecedenteVO> antecedentes;
	private List<PasesSuperficieVO> pasesSuperficie;
	private List<EstatusInmuebleVO> estatusInmueble;
	private Boolean tienePasesSuperficie = false;
	private String ubicacion;
	private List<ValoresActo> valoresActos;

	/**
	 * @param predioVO
	 * @param predio
	 */
	public CaratulaVO(PredioVO predioVO, Predio predio) {
		this.setAntecedente(predioVO.getAntecedente());
		this.setAsentamiento(predioVO.getAsentamiento());
		this.setBloqueado(predioVO.getBloqueado());
		this.setClaveCatastral(predioVO.getClaveCatastral());
		this.setClaveCatastralEstandard(predioVO.getClaveCatastralEstandard());
		this.setColindancias(predioVO.getColindancias());
		this.setCp(predioVO.getCp());
		this.setCuentaCatastral(predioVO.getCuentaCatastral());
		this.setDevoluciones(predioVO.getDevoluciones());
		this.setEdificio(predioVO.getEdificio());
		this.setEstado(predioVO.getEstado());
		this.setEtapaOSeccion(predioVO.getEtapaOSeccion());
		this.setFraccion(predioVO.getFraccion());
		this.setFracOCondo(predioVO.getFracOCondo());
		this.setMacroManzana(predioVO.getMacroManzana());
		this.setManzana(predioVO.getManzana());
		this.setMunicipio(predioVO.getMunicipio());
		this.setNivel(predioVO.getNivel());
		this.setNoFolio(predioVO.getNoFolio());
		this.setNoLote(predioVO.getNoLote());
		this.setNombreFracOCondo(predioVO.getNombreFracOCondo());
		this.setNumeroInterior(predioVO.getNumeroInterior());
		this.setNumExt(predioVO.getNumExt());
		this.setNumExt2(predioVO.getNumExt2());
		this.setNumInt(predioVO.getNumInt());
		this.setOficinaId(predioVO.getOficinaId());
		this.setPases(predioVO.getPases());
		this.setPredioId(predioVO.getPredioId());
		this.setPrimerRegistro(predioVO.isPrimerRegistro());
		this.setProcedeFolio(predioVO.getProcedeFolio());
		this.setPropietarios(predioVO.getPropietarios());
		this.setSumatoriaActosPorClasifActo(predioVO.getSumatoriaActosPorClasifActo());
		this.setSuperficie(predioVO.getSuperficie());
		this.setSuperficieM2(predioVO.getSuperficieM2());
		this.setSuperficieRestante(predioVO.getSuperficieRestante());
		this.setTipoAsentamiento(predioVO.getTipoAsentamiento());
		this.setTipoInmueble(predioVO.getTipoInmueble());
		this.setTotalSuperficieSegregada(predioVO.getTotalSuperficieSegregada());
		this.setTramites(predioVO.getTramites());
		this.setUnidadMedida(predioVO.getUnidadMedida());
		this.setUsoSuelo(predioVO.getUsoSuelo());
		this.setVialidad(predioVO.getVialidad());
		this.setVialidad2(predioVO.getVialidad2());
		this.setZona(predioVO.getZona());

		this.setUbicacion(predio.getUbicacion());

		this.antecedentes = new ArrayList<>();
		this.antecedentes.add(this.getAntecedente());

		if (this.getPases() != null) {
			this.tienePasesSuperficie = true;
			this.pasesSuperficie = new ArrayList<>();
			this.pasesSuperficie.add(new PasesSuperficieVO(predioVO));
		}

		this.estatusInmueble = new ArrayList<>();
		this.estatusInmueble.add(new EstatusInmuebleVO(predioVO));
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion == null ? null : ubicacion.toUpperCase();
	}

	public List<AntecedenteVO> getAntecedentes() {
		return antecedentes;
	}

	public void setAntecedentes(List<AntecedenteVO> antecedentes) {
		this.antecedentes = antecedentes;
	}

	public List<PasesSuperficieVO> getPasesSuperficie() {
		return pasesSuperficie;
	}

	public void setPasesSuperficie(List<PasesSuperficieVO> pasesSuperficie) {
		this.pasesSuperficie = pasesSuperficie;
	}

	public Boolean getTienePasesSuperficie() {
		return tienePasesSuperficie;
	}

	public void setTienePasesSuperficie(Boolean tienePasesSuperficie) {
		this.tienePasesSuperficie = tienePasesSuperficie;
	}

	public List<EstatusInmuebleVO> getEstatusInmueble() {
		return estatusInmueble;
	}

	public void setEstatusInmueble(List<EstatusInmuebleVO> estatusInmueble) {
		this.estatusInmueble = estatusInmueble;
	}

	public List<ValoresActo> getValoresActos() {
		return valoresActos;
	}

	public void setValoresActos(List<ValoresActo> valoresActos) {
		this.valoresActos = valoresActos;
	}

}
