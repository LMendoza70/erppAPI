package com.gisnet.erpp.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManagerFactory;

import java.util.HashSet;

import com.gisnet.erpp.domain.*;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gisnet.erpp.vo.caratula.PropietarioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.StatusActo;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.domain.AuxiliarPersona;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.MueblePersona;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.DocumentoRepository;
import com.gisnet.erpp.repository.TipoEntradaRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.security.SecurityUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.stream.Collectors;

import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.web.api.predio.PredioDTO;

import javax.validation.constraints.NotNull;

import static org.springframework.util.ObjectUtils.isEmpty;

@Transactional
@Service
public class ActoPredioService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ActoPredioRepository actoPredioRepository;

	@Autowired
	ActoRepository actoRepository;

	@Autowired
	TipoEntradaRepository tipoEntradaRepository;

	@Autowired
	StatusActoRepository statusActoRepository;
	
	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;
	
	@Autowired
	ActoPredioCampoService actoPredioCampoService;

	@Autowired
	DocumentoRepository documentoRepository;

	@Autowired
	private DocumentoService documentoService;

	@Autowired
	private ActoDocumentoRepository actoDocumentoRepository;

	@Transactional
	public ActoPredio creaActoParaActoPredio(ActoPredio actoPredio){
		int orden= 1;
		// Si trae orden, se debe reordenar
		log.debug("actoPredio.getActo().getOrden()={}",  actoPredio.getActo().getOrden());
		
		Integer max= actoRepository.maxByPrelacionAndVf(actoPredio.getActo().getPrelacion().getId(), true);
		if(max!=null){
			max=max+1;
			orden=max;
		} else {
			max=1;
		}
		
		if (actoPredio.getActo().getOrden()!=null && actoPredio.getActo().getOrden()>0) {
			// mover subsecuentes
			List<Acto> actos = actoRepository.findAllByPrelacionIdAndVfTrueOrderByOrdenAsc(actoPredio.getActo().getPrelacion().getId());
			for (Acto acto: actos) {
				if (acto.getOrden()>= actoPredio.getActo().getOrden()) {
					acto.setOrden(acto.getOrden()+1);
					actoRepository.save(acto);
				}
			}
			orden = actoPredio.getActo().getOrden();
		} 	
		
		if (orden>max) {
			orden=max;
		}
		
		Acto acto = actoPredio.getActo();
		acto.setOrden(orden);
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		acto.setVf(true);
		acto.setVersion(1);
		acto.setModificable(true);
		
		actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
		actoPredio.setVersion(1);
		actoRepository.save(acto);
		
		actoPredioRepository.save(actoPredio);
		
		return actoPredio;
		
	}

	@Transactional(readOnly = true)
	public Set<PredioPersona> findPersonasbyPredioId(Long id) {
		return actoPredioRepository.findPersonasbyPredioIdQuery(id);
	}
	
	 @Transactional(readOnly = true)
	 public Set<Acto> findActosbyPredioId(Long id, Long[] tiposActo) {
		 return actoPredioRepository.findActosPredioByPredioQuery(id, tiposActo);
	 }

	 @Transactional(readOnly = true)
	 public Set<Acto> findAllActosbyPredioAndStatusId(Long id, Long[] status) {
		 return actoPredioRepository.findActosPredioByPredioAndStatusQuery(id, status);
	 }

	 @Transactional(readOnly = true)
	 public Set<Acto> findAllActosbyPredioAndStatusId(Long id, Long[] tiposActo, Long[] status) {
		 return actoPredioRepository.findAllActosPredioByPredioAndStatusQuery(id, tiposActo, status);
	 }
							 

	@Transactional(readOnly = true)
	public Set<PjPersona> findPersonasbyPjPersonaId(Long id) {
		return actoPredioRepository.findPersonasbyPersonaJuridicaId(id);
	}

	@Transactional(readOnly = true)
	public Set<AuxiliarPersona> findPersonasbyFolioSeccionAuxiliarId(Long id) {
		return actoPredioRepository.findPersonasbyFolioSeccionAuxiliarId(id);
	}


	@Transactional(readOnly = true)
	public Set<MueblePersona> findPersonasbyMuebleId(Long id) {
		return actoPredioRepository.findPersonasbyMuebleId(id);
	}


	public Set<ActoPredio> getActosPrediosByPredioId(Long id) {
		Set<ActoPredio> result = new HashSet();
		ActoPredio tmp = null;
		Predio ptmp = null;
		for(ActoPredio ap :actoPredioRepository.findByPredioId(id)) {
			tmp = new ActoPredio();
			ptmp = new Predio();
			tmp.setId(ap.getId());
			ptmp.setId(ap.getPredio().getId());
			tmp.setPredio(ptmp);
			result.add(tmp);
		}
		return result;
	}

	public ActoPredio  getAP(Long id){
		return actoPredioRepository.findOneById(id);

	}



	public List<ActoPredio> getActosPrediosByPersonaJuridicaIdAndPrelacionId(Long idPredio, Long idPrelacion ){

			return actoPredioRepository.findAllByPersonaJuridicaAndPrelacion(idPredio,idPrelacion);

	}

	public List<ActoPredio> getActosPrediosByFolioSeccionAuxiliarIdAndPrelacionId(Long idPredio, Long idPrelacion ){

			return actoPredioRepository.findAllByFolioSeccionAuxiliarAndPrelacion(idPredio,idPrelacion);

	}

	public List<ActoPredio> getActosPrediosByMuebleIdAndPrelacionId(Long idPredio, Long idPrelacion ){

			return actoPredioRepository.findAllByMuebleAndPrelacion(idPredio,idPrelacion);

	}

	public List<ActoPredio> getActosPrediosByPredioIdAndPrelacionId(Long idPredio, Long idPrelacion ){

			return actoPredioRepository.findAllByPredioAndPrelacion(idPredio,idPrelacion);

	}

	public List<ActoPredio> getActosPrediosByPrelacionAndConcepto(Long idPrelacion, Long concepto ){

			return actoPredioRepository.findAllByPrelacionAndConcepto(idPrelacion,concepto);

	}



	public ActoPredio save(ActoPredio actoPredio) {
		ActoPredio temAP=null;
		if(actoPredio!=null) {

			temAP= actoPredioRepository.findByActoAndActoPrelacion(actoPredio.getActo().getId(),actoPredio.getActo().getPrelacion().getId());
			temAP.setPredio(actoPredio.getPredio());
			temAP.setFolioSeccionAuxiliar(actoPredio.getFolioSeccionAuxiliar());
			temAP.setMueble(actoPredio.getMueble());
			temAP.setPersonaJuridica(actoPredio.getPersonaJuridica());
			temAP.setTipoFolio(actoPredio.getTipoFolio());
		}
		actoPredioRepository.saveAndFlush(temAP);
		return temAP;
	}

	public void replicaActoPrediosCampos(ActoPredio actoPredio, Long actoId) {
		ActoPredio ac = this.findMaxVersion(actoId);
		if(ac!=null) {
			Set<ActoPredioCampo> actoPredioCampo= ac.getActoPredioCamposParaActoPredios();
			for (ActoPredioCampo apc : actoPredioCampo) {
				ActoPredioCampo newApc=new ActoPredioCampo();
				newApc.setValor(apc.getValor());
				newApc.setOrden(apc.getOrden());
				newApc.setActoPredio(actoPredio);
				newApc.setModuloCampo(apc.getModuloCampo());
				actoPredioCampoRepository.saveAndFlush(newApc);
			}
		}
	}

	public ActoPredio replicaActoPredio(ActoPredio actoPredio,PrelacionPredio predio) {
		ActoPredio actoPredioNew = null;
		if (actoPredio != null) {
			log.debug("FOLIO REPLICA:"+predio.getPredio().getNoFolio());
			Acto newActo = new Acto();
			StatusActo statusActo = statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo());
			newActo.setStatusActo(statusActo);
			newActo.setTipoActo(actoPredio.getActo().getTipoActo());
			newActo.setFechaRegistro(new Date());
			newActo.setVersion(1);
			//newActo.setVf(actoPredio.getActo().getVf());
			newActo.setProcesado(actoPredio.getActo().isProcesado());
			newActo.setPrelacion(actoPredio.getActo().getPrelacion());
			newActo.setModificable(actoPredio.getActo().getModificable());
			Integer max = actoRepository.maxByPrelacion(actoPredio.getActo().getPrelacion().getId());
			if (max != null) {
				max = max + 1;

			} else {
				max = 1;
			}
			newActo.setOrden(max);
			newActo.setModificable(actoPredio.getActo().getModificable());
			actoRepository.saveAndFlush(newActo);

			actoPredioNew = new ActoPredio();

			actoPredioNew.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
			actoPredioNew.setActo(newActo);
			actoPredioNew.setVersion(1);
			// actoPredioNew.setPredio(actoPredio.getPredio());

			actoPredioNew.setTipoFolio(actoPredio.getTipoFolio());
			int tipoFolioId = (int) (long) actoPredio.getTipoFolio().getId();
			Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromInt(tipoFolioId);
			switch (tipo) {

			case PERSONAS_JURIDICAS:
				actoPredioNew.setPersonaJuridica(predio.getPersonaJuridica());
				break;
			case PERSONAS_AUXILIARES:
				actoPredioNew.setFolioSeccionAuxiliar(predio.getFolioSeccionAuxiliar());
				break;
			case MUEBLE:
				actoPredioNew.setMueble(predio.getMueble());
				break;

			case PREDIO:
				actoPredioNew.setPredio(predio.getPredio());
				break;
			}

			actoPredioRepository.saveAndFlush(actoPredioNew);

			//Set<ActoDocumento> documentos = actoDocumentoRepository.getAllActoAssignableDocuments(actoPredio.getActo().getId());
			 Optional<ActoDocumento> acOp =  actoDocumentoRepository.findFirstByActoIdOrderByVersionDesc(actoPredio.getActo().getId());
			 if(acOp.isPresent()) {
				 	ActoDocumento ac =  acOp.get();
					log.debug("Documento a replicar : " + ac.getDocumento().getId());
					Documento temDoc = new Documento();
					temDoc.setArchivo(ac.getDocumento().getArchivo());
					temDoc.setAsunto(ac.getDocumento().getAsunto());
					temDoc.setAutoridadExhortante(ac.getDocumento().getAutoridadExhortante());
					temDoc.setAutoridadLocal(ac.getDocumento().getAutoridadLocal());
					temDoc.setBis(ac.getDocumento().isBis());
					temDoc.setCargoFirmante(ac.getDocumento().getCargoFirmante());
					temDoc.setCausaUtilidad(ac.getDocumento().getCausaUtilidad());
					temDoc.setDerivadoDe(ac.getDocumento().getDerivadoDe());
					temDoc.setEnCalidadDe(ac.getDocumento().getEnCalidadDe());
					temDoc.setExhorto(ac.getDocumento().isExhorto());
					temDoc.setExpropiante(ac.getDocumento().getExpropiante());
					temDoc.setFecha(ac.getDocumento().getFecha());
					temDoc.setJuez(ac.getDocumento().getJuez());
					temDoc.setJuzgado(ac.getDocumento().getJuzgado());
					temDoc.setMaterno(ac.getDocumento().getMaterno());
					temDoc.setMunicipio(ac.getDocumento().getMunicipio());
					temDoc.setNombre(ac.getDocumento().getNombre());
					temDoc.setNotario(ac.getDocumento().getNotario());
					temDoc.setNumero(ac.getDocumento().getNumero());
					temDoc.setNumero2(ac.getDocumento().getNumero2());
					temDoc.setObjeto(ac.getDocumento().getObjeto());
					temDoc.setObservaciones(ac.getDocumento().getObservaciones());
					temDoc.setPaterno(ac.getDocumento().getPaterno());
					temDoc.setRan(ac.getDocumento().isRan());
					temDoc.setRatificado(ac.getDocumento().isRatificado());
					temDoc.setRequeridoPor(ac.getDocumento().getRequeridoPor());
					temDoc.setTipoAutoridad(ac.getDocumento().getTipoAutoridad());
					temDoc.setTipoCert(ac.getDocumento().getTipoCert());
					temDoc.setTipoDocumento(ac.getDocumento().getTipoDocumento());

					documentoRepository.saveAndFlush(temDoc);

					ActoDocumento temAC = new ActoDocumento();

					log.debug("EL ACTO QUE SE FIJARA AL DOCUMENTO " + newActo.getId() + "  "
							+ newActo.getTipoActo().getNombre());
					temAC.setActo(newActo);
					temAC.setDocumento(temDoc);
					temAC.setVersion(ac.getVersion());

					actoDocumentoRepository.saveAndFlush(temAC);
				

			} else {
				log.debug("EL ACTO NO TIENE DOCUMENTOS");

			}

		}

		return actoPredioNew;
	}

	public ActoPredio replicaActoPredio(ActoPredio actoPredio) {
		ActoPredio actoPredioNew = null;
		if (actoPredio != null) {
			Acto newActo = new Acto();
			StatusActo statusActo = statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo());
			newActo.setStatusActo(statusActo);
			newActo.setTipoActo(actoPredio.getActo().getTipoActo());
			newActo.setFechaRegistro(new Date());
			newActo.setVersion(1);
			//newActo.setVf(actoPredio.getActo().getVf());
			newActo.setProcesado(actoPredio.getActo().isProcesado());
			newActo.setPrelacion(actoPredio.getActo().getPrelacion());
			newActo.setModificable(actoPredio.getActo().getModificable());
			Integer max = actoRepository.maxByPrelacion(actoPredio.getActo().getPrelacion().getId());
			if (max != null) {
				max = max + 1;

			} else {
				max = 1;
			}
			newActo.setOrden(max);
			newActo.setModificable(actoPredio.getActo().getModificable());
			actoRepository.saveAndFlush(newActo);

			actoPredioNew = new ActoPredio();

			actoPredioNew.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
			actoPredioNew.setActo(newActo);
			actoPredioNew.setVersion(1);
			// actoPredioNew.setPredio(actoPredio.getPredio());

			actoPredioNew.setTipoFolio(actoPredio.getTipoFolio());
			int tipoFolioId = (int) (long) actoPredio.getTipoFolio().getId();
			Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromInt(tipoFolioId);
			switch (tipo) {

			case PERSONAS_JURIDICAS:
				actoPredioNew.setPersonaJuridica(actoPredio.getPersonaJuridica());
				break;
			case PERSONAS_AUXILIARES:
				actoPredioNew.setFolioSeccionAuxiliar(actoPredio.getFolioSeccionAuxiliar());
				break;
			case MUEBLE:
				actoPredioNew.setMueble(actoPredio.getMueble());
				break;

			case PREDIO:
				actoPredioNew.setPredio(actoPredio.getPredio());
				break;
			}

			actoPredioRepository.saveAndFlush(actoPredioNew);

			//Set<ActoDocumento> documentos = actoDocumentoRepository.getAllActoAssignableDocuments(actoPredio.getActo().getId());
			 Optional<ActoDocumento> acOp =  actoDocumentoRepository.findFirstByActoIdOrderByVersionDesc(actoPredio.getActo().getId());
			 if(acOp.isPresent()) {
				 	ActoDocumento ac =  acOp.get();
					log.debug("Documento a replicar : " + ac.getDocumento().getId());
					Documento temDoc = new Documento();
					temDoc.setArchivo(ac.getDocumento().getArchivo());
					temDoc.setAsunto(ac.getDocumento().getAsunto());
					temDoc.setAutoridadExhortante(ac.getDocumento().getAutoridadExhortante());
					temDoc.setAutoridadLocal(ac.getDocumento().getAutoridadLocal());
					temDoc.setBis(ac.getDocumento().isBis());
					temDoc.setCargoFirmante(ac.getDocumento().getCargoFirmante());
					temDoc.setCausaUtilidad(ac.getDocumento().getCausaUtilidad());
					temDoc.setDerivadoDe(ac.getDocumento().getDerivadoDe());
					temDoc.setEnCalidadDe(ac.getDocumento().getEnCalidadDe());
					temDoc.setExhorto(ac.getDocumento().isExhorto());
					temDoc.setExpropiante(ac.getDocumento().getExpropiante());
					temDoc.setFecha(ac.getDocumento().getFecha());
					temDoc.setJuez(ac.getDocumento().getJuez());
					temDoc.setJuzgado(ac.getDocumento().getJuzgado());
					temDoc.setMaterno(ac.getDocumento().getMaterno());
					temDoc.setMunicipio(ac.getDocumento().getMunicipio());
					temDoc.setNombre(ac.getDocumento().getNombre());
					temDoc.setNotario(ac.getDocumento().getNotario());
					temDoc.setNumero(ac.getDocumento().getNumero());
					temDoc.setNumero2(ac.getDocumento().getNumero2());
					temDoc.setObjeto(ac.getDocumento().getObjeto());
					temDoc.setObservaciones(ac.getDocumento().getObservaciones());
					temDoc.setPaterno(ac.getDocumento().getPaterno());
					temDoc.setRan(ac.getDocumento().isRan());
					temDoc.setRatificado(ac.getDocumento().isRatificado());
					temDoc.setRequeridoPor(ac.getDocumento().getRequeridoPor());
					temDoc.setTipoAutoridad(ac.getDocumento().getTipoAutoridad());
					temDoc.setTipoCert(ac.getDocumento().getTipoCert());
					temDoc.setTipoDocumento(ac.getDocumento().getTipoDocumento());

					documentoRepository.saveAndFlush(temDoc);

					ActoDocumento temAC = new ActoDocumento();

					log.debug("EL ACTO QUE SE FIJARA AL DOCUMENTO " + newActo.getId() + "  "
							+ newActo.getTipoActo().getNombre());
					temAC.setActo(newActo);
					temAC.setDocumento(temDoc);
					temAC.setVersion(ac.getVersion());

					actoDocumentoRepository.saveAndFlush(temAC);
				

			} else {
				log.debug("EL ACTO NO TIENE DOCUMENTOS");

			}

		}

		return actoPredioNew;
	}
	public ActoPredio findOneByActoIdAndVersion(Long  actoId, Integer version) {
		
		Optional<ActoPredio> optionalActoPredio = actoPredioRepository.findOneByActoIdAndVersion(actoId, version);
		
		return optionalActoPredio.get();
	}

	public Long findByLastVersion(Long  actoId, Long tipoEntradaId) {
		Constantes.TipoEntrada tipoEntrada =  Constantes.TipoEntrada.getTipoEntrada(tipoEntradaId);
		Long result = actoPredioRepository.findByLastVersion(actoId);
		
		return result;
	}

	public ActoPredio findOneByActoIdAndVersionUno(Long  actoId) {
		return actoPredioRepository.findOneByActoIdAndVersionAndTipoEntradaId(actoId, 1, Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()).get();
	}


	public Boolean regresarUltimaVersion(Long id) {
		
		Boolean valido=false;
		try {

			Long version =findByLastVersion(id,  Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada());


			ActoPredio ac = findOneByActoIdAndVersion(id,version.intValue());
			if(ac!=null) {
				
				System.out.println("ACTO VERSION  : "+ac.getActo().getVersion());
				
				
				actoPredioCampoService.getActoPredioCampoByActoPredio(ac);
				
				
				for(ActoPredioCampo apc : actoPredioCampoService.getActoPredioCampoByActoPredio(ac)) {
					
					//ac.removeActoPredioCamposParaActoPredio(apc);
					actoPredioCampoRepository.delete(apc);
					
				}
				
				/*
				for(PredioPersona pp : ac.getPredioPersonasParaActoPredios()) {
					
					ac.removePredioPersonasParaActoPredio(pp);
					
				}
				*/
				//actoPredioRepository.saveAndFlush(ac);
				actoPredioRepository.delete(ac);
				valido=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			valido =false;
			// TODO: handle exception
		}
		return valido;
	}
	
	public List<ActoPredioCampo> findAllActoPredioCamposByActoPredioId(Long id){
		return actoPredioCampoRepository.findByActoPredioId(id);
	}

	@Transactional(readOnly = true)
	public Set<ActoDocumento> findEscrituraByPredioId(Long id) {
		return actoPredioRepository.findEscrituraByPredioIdQuery(id);
	}

	@Transactional(readOnly = true)
	public Set<PropietarioVO> getPropietarioVOByPredioId ( Long id) {
		Set<PredioPersona> pp = this.findPersonasbyPredioId(id);
		final Set<PropietarioVO> pvo = pp.stream()
				//.filter(predioPersona -> predioPersona.getTipoRelPersona().)  // Limitar a titulares o todos los propietarios
			.map( PropietarioVO::converToPropietarioVO)
				.collect(Collectors.toSet());
		return pvo;
	}

	public Set<Documento> findDocumentoByPredioAndEscritura (Predio predio, Integer escritura) {
		Set<ActoDocumento> actoDocs = this.findEscrituraByPredioId(predio.getId());

		try {
			if (!isEmpty(actoDocs) ) {
				return actoDocs.stream()
						.map(ActoDocumento::getDocumento)
						.filter(doc -> true == true
								&& Constantes.ID_TIPO_DOCUMENTO_ESCRITURA == doc.getTipoDocumento().getId().longValue()
								&& doc.getNumero() == escritura )
						.collect(Collectors.toSet());
			}
		}
		catch (Exception except) {
			Log.info(except);
		}
		return null;
	}

	public Set<Documento> findDocumentoByMuebleAndEscritura (Mueble predio, Integer escritura) {
		/*Set<ActoDocumento> actoDocs = this.findEscrituraByMuebleId(predio.getId());
		if (!isEmpty(actoDocs) ) {
			return actoDocs.stream()
					.map(ActoDocumento::getDocumento)
					.filter(doc ->
							doc.getTipoDocumento().getId().longValue() == Constantes.ID_TIPO_DOCUMENTO_ESCRITURA &&
									doc.getNumero() == escritura )
					.collect(Collectors.toSet());
		}*/
		return null;
	}
	
	public ActoPredio findMaxVersion(Long actoId) {
		Optional<ActoPredio> ap  =  actoPredioRepository.findFirstByActoIdOrderByVersionDesc(actoId);
		return ap.isPresent() ?  ap.get() : null;
	}
	
	@Transactional
	public ActoPredio desligarActo(Long actoId) {
		 List<ActoPredio> actosPredios =  actoPredioRepository.findByActoId(actoId);
	     ActoPredio[] result = {null}; //new ActoPredio();
		 //OBTIENE LA VERSION MENOR (INGRESO EN VENTANILLA)
		 Integer minVersion = actosPredios
			      .stream()
			      .mapToInt(v -> v.getVersion())
			      .min().orElseThrow(NoSuchElementException::new);
		 
			actosPredios.forEach(x->{
				  actoPredioCampoRepository.delete(x.getActoPredioCamposParaActoPredios());
				if(x.getVersion().equals(minVersion)) {
					x.setTipoFolio(null);
					x.setPredio(null);
					x.setPersonaJuridica(null);
					x.setMueble(null);
					x.setFolioSeccionAuxiliar(null);
					actoPredioRepository.save(x);
					result[0] =  x;
				}else {
					actoPredioRepository.delete(x);	
				}
			});

			return result[0];
	}
}
