package com.gisnet.erpp.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.TipoAsent;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.vo.caratula.ColindanciaVO;
import com.gisnet.erpp.vo.fichaRegistral.FichaRegistralVO;
import com.gisnet.erpp.vo.fichaRegistral.MedidasVO;
import com.gisnet.erpp.vo.fichaRegistral.PaseFichaVO;
import com.gisnet.erpp.vo.fichaRegistral.TitularesVO;

@Service
public class FichaRegistralService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PredioRepository predioRepository;

	@Autowired
	PasesService pasesService;

	@Autowired
	private PredioPersonaService predioPersonaService;

	@Autowired
	private ColindanciaService colindanciaService;

	
	public String validaPredio(List<Predio> predio,boolean isCatastral) {
		String cadena = "OK";
		if(predio != null && !predio.isEmpty()) {
			if(predio.size()>1) {
				if(!isCatastral) {
					cadena = "{\"mensaje\":\"Se encontró más de una Ficha Registral\"}";
				}
			}
		}else {
			cadena = "{\"mensaje\":\"No se encontró Ficha Registral\"}";
		}
		return cadena;
		
	}
	
	@Transactional(readOnly = true)
	public FichaRegistralVO getFichaRegistral(Predio predio) {		
		return this.getLlenado(predio);
	}
	
	@Transactional(readOnly = true)
	private FichaRegistralVO getLlenado(Predio predio) {
		FichaRegistralVO fichaVO = new FichaRegistralVO();
		
		if (predio.getOficina() != null) {
			fichaVO.setOficina(predio.getOficina().getNombre());
		}
		fichaVO.setNoFolio(predio.getNoFolio());
		fichaVO.setEstatus(predio.getStatusActo().getNombre());
		if (predio.getNumeroFolioReal() != null) {
			fichaVO.setFolioAnterior(predio.getNumeroFolioReal());
		}
		if (predio.getAuxiliar() != null) {
			fichaVO.setAuxiliar(predio.getAuxiliar());
		}
		
		
		fichaVO.setTipoInmueble(predio.getTipoInmueble() != null ? predio.getTipoInmueble().getNombre() : "");
		fichaVO.setNumeroInterior(predio.getNumInt());
		fichaVO.setNivel(predio.getNivel() != null ? predio.getNivel().getNombre() : "");
		fichaVO.setEdificio(predio.getEdificio());
		fichaVO.setNoLote(predio.getNoLote());
		fichaVO.setLocalidadSector(predio.getLocalidadSector());
		fichaVO.setFraccion(predio.getFraccion());
		fichaVO.setManzana(predio.getManzana());
		
		
		fichaVO.setTipoVialidad(predio.getTipoVialidad() != null ? predio.getTipoVialidad().getNombre() : "");
		fichaVO.setVialidad(predio.getVialidad());
		fichaVO.setNumExt(predio.getNumExt());
		fichaVO.setEnlaceVialidad(predio.getEnlaceVialidad() != null ? predio.getEnlaceVialidad().getNombre() : "");
		fichaVO.setTipoVialidad2(predio.getTipoVialidad2() != null ? predio.getTipoVialidad2().getNombre() : "");
		fichaVO.setVialidad2(predio.getVialidad2());
		fichaVO.setNumExt2(predio.getNumExt2());
		
		
		TipoAsent tipoAsent = predio.getTipoAsent();
		if (tipoAsent != null) {
			fichaVO.setTipoAsentamiento(tipoAsent.getNombre());
		}
		fichaVO.setAsentamiento(predio.getAsentamiento());
		//fichaVO.setNombreFracOCondo(predio.getFracOCondo() != null ? predio.getFracOCondo().getNombre() : "");
		fichaVO.setNombreFracOCondo(predio.getNombreFracOCondo());
		fichaVO.setEtapaOSeccion(predio.getEtapaOSeccion() != null ? predio.getEtapaOSeccion().getNombre() : "");
		
		
		fichaVO.setZona(predio.getZona());
		fichaVO.setClaveCatastral(predio.getClaveCatastral());
		fichaVO.setCuentaCatastral(predio.getCuentaCatastral());
		fichaVO.setClaveCatastralEstandard(predio.getClaveCatastralEstandard());
		
		if (predio.getMunicipio() != null) {
			fichaVO.setEstado(predio.getMunicipio().getEstado().getNombre().trim());
			fichaVO.setMunicipio(predio.getMunicipio().getNombre());
		}
		fichaVO.setCp(predio.getCodigoPostal());
		fichaVO.setSuperficie(predio.getSuperficie());
		fichaVO.setUnidadMedida(predio.getUnidadMedida().getNombre());
		// fichaVO.setSuperficieM2(predio.getSuperficieM2() );
		fichaVO.setSuperficieM2(Optional.ofNullable(predio.getSuperficieM2()).orElse(0.0));
		fichaVO.setUsoSuelo(predio.getUsoSuelo().getNombre());
		
		/*
		 * Colindancias
		 */
		Set<Colindancia> col = colindanciaService.findColindancias(predio.getId());

		List<MedidasVO> medidas = new ArrayList<MedidasVO>();

		if (col != null) {
			for(Colindancia colindancia: col) {
				medidas.add(new MedidasVO( colindancia.getOrientacion().getNombre(),
						colindancia.getNombre()));
			}
		}
		fichaVO.setColindancias(medidas);
		
		/*
		 * Pases
		 */
		List<PaseFichaVO> listaPases = pasesService.findPasesFicha(predio.getId());
		fichaVO.setPases(listaPases);
		
		/*
		 * Titulares
		 */
		List<TitularesVO> titulares = new ArrayList<TitularesVO>();

		List<PredioPersona> predioPersonas = predioPersonaService.findPropietariosActuales(predio.getId(), false);

		predioPersonas.forEach((predioPersona) -> {
			TitularesVO titularesVO = new TitularesVO();

			titularesVO.setNombre(predioPersona.getPersona().getNombre());
			;
			titularesVO.setPaterno(predioPersona.getPersona().getPaterno());
			;
			titularesVO.setMaterno(predioPersona.getPersona().getMaterno());
			;
			titularesVO.setFechaNac(predioPersona.getPersona().getFechaNac());
			;
			titularesVO.setRfc(predioPersona.getPersona().getRfc());
			;
			titularesVO.setCurp(predioPersona.getPersona().getCurp());
			;
			titularesVO.setEstadoCivil(
					predioPersona.getEstadoCivil() != null ? predioPersona.getEstadoCivil().getNombre() : "");
			;
			titularesVO.setRegimen(predioPersona.getRegimen() != null ? predioPersona.getRegimen().getNombre() : "");
			;
			titularesVO.setNacionalidad(
					predioPersona.getNacionalidad() != null ? predioPersona.getNacionalidad().getNombre() : "");
			;
			titularesVO.setPorcentajeDd(predioPersona.getPorcentajeDd());
			;
			titularesVO.setPorcentajeUv(predioPersona.getPorcentajeUv());
			;
			titulares.add(titularesVO);

		});
		fichaVO.setTitulares(titulares);
		
		
		
		
		return fichaVO;
		
	}


}
