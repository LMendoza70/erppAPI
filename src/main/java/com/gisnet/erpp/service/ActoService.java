package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.*;
import com.gisnet.erpp.security.SecurityUtils;
//import com.gisnet.erpp.service.excepciones.ForbiddenException;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.SHACheckSum;
import com.gisnet.erpp.util.SignerUtilBouncyCastle;
import com.gisnet.erpp.util.UtilFecha;
import com.gisnet.erpp.util.CommonUtil;
import com.gisnet.erpp.vo.ActoVO;
import com.gisnet.erpp.vo.ArchivoFirmaVO;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ActoService{
	
	private static final Logger log = LoggerFactory.getLogger(ActoService.class);
	
	@Autowired
	ActoRepository actoRepository;

	@Autowired
	PredioPersonaService predioPersonaService;
	@Autowired
	TipoActoService tipoActoService;
	
	@Autowired
	AreaClasifActoRepository areaClasifActoRepository;
	
	@Autowired
	AreaRepository areaRepository;

	@Autowired
	PjPersonaRepository pjPersonaRepository;
	
	@Autowired
	TipoActoRepository tipoActoRepository;

	@Autowired
	StatusActoRepository statusActoRepository;


	@Autowired
	PrelacionRepository prelacionRepository;


	@Autowired
	TipoEntradaRepository tipoEntradaRepository;


	@Autowired
	ActoPredioRepository actoPredioRepository;

	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;

	@Autowired
	ActoDocumentoRepository actoDocumentoRepository;


	@Autowired
	ActoRequisitoRepository actoRequisitoRepository;

	@Autowired
	MotivoRepository motivoRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	PrelacionService prelacionService;
	
	@Autowired 
	ActoFirmaRepository actoFirmaRepository;

	@Autowired
	TipoRechazoRepository tipoRechazoRepository;

	@Autowired	
	BitacoraRepository bitacoraRepository;
	
	@Autowired
	PredioPersonaRepository predioPersonaRepository;

	@Autowired
	PrelacionContratanteRepository prelacionContratanteRepository;

	@Autowired
	ActoRelRepository actoRelRepository;

	@Autowired
	MaterializacionActoService materializacionActoService;

	@Autowired
	ActoPredioMontoRepository actoPredioMontoRepository;

	@Autowired
	FoliosFracCondRepository foliosFracCondRepository;
/*
	@Autowired
	private CoordClasifActoService coordClasifActoService;

	@Autowired
	RechazoRepository rechazoRepository;

	@Autowired
	RechazoMotivoTipoRechazoRepository rechazoMotivoTipoRechazoRepository;
*/
	@Autowired
	ActoDocumentoService actoDocumentoService;

	@Autowired
	PrelacionPredioRepository prelacionPredioRepository;
	
	@Autowired
	BitacoraMantenimientoRepository bitacoraMantenimientoRepository;
	
	@Autowired
	ActoFolioReciboRepository actoFolioReciboRepository;

	@Autowired
	ClasifActoRepository clasifActoRepository;
	
	@Autowired
	ActoControlHeredaRepository actoControlHeredaRepository;
	
	@Autowired
	AvisoRepository avisoRepository;
	
	@Autowired
	PredioService predioService;
	
	@Autowired
	BitacoraMantenimientoService bitacoraMantenimientoService;
	
	@Autowired
	RecursoInconformidadRepository recursoInconformidadRepository; 
	
	@Autowired
	BusquedaResultadoService busquedaResultadoService;
	
	@Autowired
	BitacoraActoRechazoService bitacoraActoRechazoService;
	
	public Acto findOne(Long id) {
		return actoRepository.findOne(id);
	}

	public List<Acto> findAll() {
		return actoRepository.findAll();
	}

	public List<Acto> findByPrelacionId(Long id) {
			return actoRepository.findAllByPrelacionIdAndVfFalseOrderByOrdenAsc(id, false);
	}

	public List<Acto> findAllByPrelacionOrderByOrdenAsc(Long id) {
		return actoRepository.findAllByPrelacionIdOrderByOrdenAsc(id);
	}
	

	
	public Set<ActoPredio> getActoPrediosByActoId(Long id) {
			Acto acto = actoRepository.findOneById(id);
			Set<PredioPersona> predioPersonas = new HashSet<PredioPersona>();
			Set<ActoPredio> actosPrediosIds = new LinkedHashSet<ActoPredio>();
			SortedSet<ActoPredio> actoPrediosParaActos;
			try {
				actoPrediosParaActos = acto.getActoPrediosParaActosOrderedByVersion();	
			} 
			catch (NullPointerException ex) {
				return actosPrediosIds;
			}
			
			for(ActoPredio actoPredio : actoPrediosParaActos) {
				predioPersonas = actoPredio.getPredioPersonasParaActoPredios();
				if (!predioPersonas.isEmpty()) {
					actoPredio.setPredioPersonasParaActoPredios(predioPersonas);	
				}
				actosPrediosIds.add(actoPredio);	
			}
			actosPrediosIds.forEach(System.out::println);
			
			return actosPrediosIds;
	}

	public List<ClasifActo> findAllClasifActoByArea(Long id) {
		List<ClasifActo> clasificaciones= new ArrayList<ClasifActo>();
		List<AreaClasifActo> areaClasif= areaClasifActoRepository.findAllByAreaId(id);
		for(AreaClasifActo ac:  areaClasif){
				clasificaciones.add(ac.getClasifActo());
		}
		return clasificaciones;
	}
	
	public List<TipoActo> findAllTipoActoByClasif(Long id) {
				return  tipoActoRepository.findByClasifActoIdOrderByNombreAsc(id);
			
	}


	public Acto saveAndUpdate( Acto acto )
	{
		
		if(acto.getId()==null){

			StatusActo statusActo = statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo());
			acto.setStatusActo(statusActo);
			acto.setFechaRegistro(new Date());
			Integer max= actoRepository.maxByPrelacion(acto.getPrelacion().getId());	
			if(max!=null){
					max=max+1;

			}else{
				max=1;
			}
			acto.setOrden(max);
			acto.setVersion(1);
			acto.setModificable(true);
			acto.setVf(false);
			actoRepository.saveAndFlush(acto);		
			ActoPredio actoPredio= new ActoPredio();

			actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
			actoPredio.setActo(acto);
			actoPredio.setVersion(1);

			actoPredioRepository.saveAndFlush(actoPredio);

			Long tmpTipoActoId = acto.getTipoActo().getId();
			if (tmpTipoActoId.longValue() == Constantes.TIPO_ACTO_CERTIFICADO_LG_CAUTELAR_INSERTO.longValue()) {
				Acto actoAviso = new Acto();
				actoAviso.tipoActo( tipoActoService.findOne(Constantes.TIPO_ACTO_AVISO_CAUTELAR) );
				actoAviso.setPrelacion(acto.getPrelacion());
				actoAviso.setProcesado(false);
				actoAviso = this.saveAndUpdate(actoAviso);
				//actos.add(actoAviso);
			}

		}
	

		return acto;
	}


	

	public Acto getActoByVO(ActoVO actoVO){

			Acto acto = new Acto();

				acto.setId(actoVO.getId());
                
                acto.setHashFila(actoVO.getHashFila());
                acto.setOrden(actoVO.getOrden());
				//if(actoVO.getActoPredioEntrada()!=null)
					//TODO: remplazar actopredioentrada
                	//acto.setActoPredioEntrada(getActoByVO(actoVO.getActoPredioEntrada()));

                if(actoVO.getStatusActo()!=null)  acto.setStatusActo(actoVO.getStatusActo()) ;
                if(actoVO.getTipoActo()!=null)  acto.setTipoActo(actoVO.getTipoActo()) ;
			
				return acto;
	}

	@Transactional
	public Boolean deleteActosPrelacion(Long prelacionId){
		try{
			List<Acto>  actos = findByPrelacionId(prelacionId);
			for(Acto a : actos){
				this.deleteActo(a.getId());
			}
			return true;
		}catch (Exception err) {

			err.printStackTrace();
			return false;
		}
	}

	@Transactional
	public void  deleteActoPrevioDesmaterializacion(Long actoId){
		Acto acto = actoRepository.findOne(actoId);
		materializacionActoService.deMaterializar(acto);
		this.deleteActo(actoId);
	}

	public void delete(Long actoId) {
		this.actoRepository.delete(actoId);
	}

	@Transactional
	public void hardDelete(Long actoId){
		Acto acto=null;
		try {
			acto= actoRepository.findOneById(actoId);
			System.out.println(acto.getId());
			if(acto!=null) {
				List<ActoRequisito> reqActo =actoRequisitoRepository.findAllByActo(acto);
				//List<ActoDocumento> documentos = actoDocumentoRepository.getAllByActoId(acto.getId());//master
				List<ActoDocumento> documentos = actoDocumentoRepository.getAllActoDocumentoByActoId(acto.getId());
				List<ActoPredio> actPredios =actoPredioRepository.findAllByActo(acto);
				actoRequisitoRepository.delete(reqActo);
				actoDocumentoRepository.delete(documentos);
				actoPredioMontoRepository.deleteByActoPredioActoId(acto.getId());

				for(ActoPredio ap : actPredios) {
					predioPersonaRepository.deleteByActoPredioId(ap.getId());
					List<ActoPredioCampo> actosPredioCampo = actoPredioCampoRepository.findByActoPredioId(ap.getId());
					actoPredioCampoRepository.delete(actosPredioCampo);
				}

				actoPredioRepository.delete(actPredios);
				actoFirmaRepository.deleteByActoId(acto.getId());
				prelacionContratanteRepository.deleteByActoId(acto.getId());
				foliosFracCondRepository.deleteByActoId(acto.getId());

				actoRequisitoRepository.flush();
				actoDocumentoRepository.flush();
				actoPredioCampoRepository.flush();
				actoPredioRepository.flush();
				actoFirmaRepository.flush();
				prelacionContratanteRepository.flush();
				foliosFracCondRepository.flush();
				System.out.println("aqui");
				actoRepository.hardDelete(acto);
				actoRepository.flush();
		    }
		}
		catch (Exception err) {
			err.printStackTrace();
		}
	}

	public Acto rechazarActo (Long id, Long motivoId, Long fundamentoId ,String observaciones) {
		Acto acto = null;
		Motivo motivo = null;
		TipoRechazo tRechazo = null;
		StatusActo status = new StatusActo();
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		try {
			status.setId((Constantes.StatusActo.RECHAZADO).getIdStatusActo());
			acto = actoRepository.findOneById(id);
			if (acto==null)
				return null;
			motivo = motivoRepository.findOne(motivoId);
			if (motivo == null)
				return null;

			tRechazo=tipoRechazoRepository.findById(fundamentoId);
			if (tRechazo == null)
				return null;

			

			acto.setFechaRechazo(new Date());
			acto.setMotivo(motivo);
			acto.setTipoRechazo(tRechazo);
			acto.setObservacionesMotivo(CommonUtil.encodeValue(observaciones));
			acto.setStatusActo(status);

			bitacoraRepository.save(prelacionService.createBitacoraCompleta(acto.getPrelacion(),motivo,tRechazo,null,null,observaciones));
			//-----
			bitacoraActoRechazoService.saveBitacora(acto,usuario,motivo.getNombre(),tRechazo.getNombre(), observaciones);

			actoRepository.saveAndFlush(acto);


		}
		catch (Exception except) {
			throw except;
		}

		return acto;
	}
	
	public Acto rechazarActo(Long id, Long motivo, String tipoRechazo, String observaciones,Long fundamento) {
		StatusActo status = new StatusActo();
		Acto acto = null;
		Motivo mRechazo = null;
		TipoRechazo tRechazo = null;
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		if(motivo != null) {
			mRechazo = motivoRepository.findOne(motivo);
		}
		if(fundamento != null) {
			tRechazo=tipoRechazoRepository.findById(fundamento);
		}
		
		try {

			status.setId((Constantes.StatusActo.RECHAZADO).getIdStatusActo());
			acto = actoRepository.findOneById(id);
			if (acto == null)
				return null;

			acto.setFechaRechazo(new Date());
			if(observaciones!=null) {
				acto.setObservacionesMotivo(CommonUtil.encodeValue(observaciones)); 
			} 
			acto.setStatusActo(status);
			acto.setMotivo(mRechazo);
			acto.setTipoRechazo(tRechazo);
			bitacoraRepository.save(prelacionService.createBitacoraCompleta(acto.getPrelacion(), null, null, null, null,
					observaciones));
			//-----
			bitacoraActoRechazoService.saveBitacora(acto,usuario,mRechazo.getNombre(),tRechazo.getNombre(), observaciones);
			actoRepository.saveAndFlush(acto);
		} catch (Exception except) {
			throw except;
		}

		return acto;
	}
	
	@Transactional
	public Acto rechazarActos (Long id, Long motivoId, Long fundamentoId ,String observaciones) {
		try {
			Acto actos[] = actoRepository.findActosByPrelacionId(id);
			if(actos!= null && actos.length>0) {
				for (Acto act : actos) {
					this.rechazarActo(act.getId(), motivoId, "rechazo", observaciones,fundamentoId);
				}
			}
			
			return actos[0];
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional
	public Prelacion rechazarAntecedente(Long idPrelacion, Long motivo, String observaciones,Long fundamento) {
		Prelacion prelacion = prelacionService.findOne(idPrelacion);
		try {
			Acto actos[] = actoRepository.findActosByPrelacionId(idPrelacion);
			for (Acto act : actos) {
				if (!act.getVf())
					this.rechazarActo(act.getId(), motivo, "rechazo", observaciones,fundamento);
			}

			prelacion.setUsuarioCapVal(null);
			prelacionService.saveAndUpdate(prelacion);
			
			return prelacion;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	public Boolean removerRechazo(Long idActo) {
		Acto acto = actoRepository.findOne(idActo);	
	
		try{
			StatusActo status = new StatusActo();
			status.setId(Constantes.StatusActo.TEMPORAL.getIdStatusActo());
			acto.setFechaRechazo(null);
			acto.setMotivo(null);
			acto.setTipoRechazo(null);
			acto.setObservacionesMotivo(null);
			acto.setStatusActo(status);

			actoRepository.saveAndFlush(acto);
			return true;

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	
	
	}


	public String getCadenaFirmado(Long idActo) {

		Acto acto = actoRepository.findOne(idActo);


		return  acto.getCadenaHash();
	}

	
	public String getHashActo(Long idActo) {
		Acto acto = actoRepository.findOne(idActo);	
		String hash=null;
		try{
			hash= SHACheckSum.getHashSHA256(acto.getCadenaHash());
		}catch(Exception e){
			e.printStackTrace();
		}	
		return hash;
	}
	
	@Transactional
	public ActoFirma firmaActo(ArchivoFirmaVO  firma ) {
		
		log.debug("Objetco firma : {}",firma);
		log.debug("ID : {}",firma.getId());
		
		Acto acto = actoRepository.findOne(firma.getId());
		
		log.debug("acto buscado : {}",acto);
		
		Usuario user= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		ActoFirma actoFirma=null;
		if(firma!=null){
					 actoFirma= new ActoFirma();
					actoFirma.setActo(acto);
					actoFirma.setArchivo(null);
					//Calendar calendar = Calendar.getInstance();
					//calendar.setTimeInMillis();
					actoFirma.setPkcs7( firma.getPkcs7());
					actoFirma.setFirma(SignerUtilBouncyCastle.getEncryptedDigest(firma.getPkcs7()));
					actoFirma.setSecuencia( firma.getSecuencia()); 
					actoFirma.setPolitica( firma.getPolitica());
					actoFirma.setDigestion( firma.getDigestion()) ;
					actoFirma.setSecuenciaTS( firma.getSecuenciaTS());
					actoFirma.setEstampilla(firma.getEstampilla()); 
					actoFirma.setUsuario(user);
					actoFirma.setEsActo(true);
					
					if (acto.getTipoActo().getId().equals(Constantes.FUSION_TIPO_ACTO_ID)) {
						this.firmaActosFusion(actoFirma, acto.getPrelacion().getId());
					} else {
						actoFirmaRepository.save(actoFirma);					
					}

					
				   if(acto.getVf()==null || acto.getVf()==false)
						materializacionActoService.materializaActo(acto);

				
		}
		return actoFirma;
		
	}

	public ActoFirma getFirmaActo(Long idActo ) {
		ActoFirma actoFirma=null;	
		
		actoFirma=  actoFirmaRepository.findByActoIdAndEsActo(idActo,true);
		
		return actoFirma;
		
	}

	/*public Boolean desfirmarActo(Long idActo) {
		List<ActoFirma> actoFirmas =actoFirmaRepository.findAllByActoIdAndEsActo(idActo, true);
		
		for(ActoFirma a :actoFirmas){
			actoFirmaRepository.delete(a);
		}

		return true;
		
	}*/
	
	@Transactional
	public Boolean desfirmarActo(Long idActo) throws Exception {
		try 
		{
			Acto acto =  actoRepository.findOne(idActo);
			if(acto.getTipoActo().getId().equals(Constantes.FUSION_TIPO_ACTO_ID)) 
			{
				List<ActoPredio> actos  =  prelacionService.findAllActosFusionByPrelacionAndVfFalseSalida(acto.getPrelacion().getId());
				for(ActoPredio a : actos) {
					List<ActoFirma> actoFirmas = actoFirmaRepository.findAllByActoIdAndEsActo(a.getActo().getId(), true);
					for (ActoFirma af : actoFirmas) {
						actoFirmaRepository.delete(af);
					}	
				}
			}else 
			{
				List<ActoFirma> actoFirmas = actoFirmaRepository.findAllByActoIdAndEsActo(idActo, true);
				for (ActoFirma a : actoFirmas) {
					actoFirmaRepository.delete(a);
				}
			}
		} catch (Exception e) {
			throw new Exception("NO DESFIRMADO");
		}
		return true;
	}


	public List<ActoFirma> getActosFirma (Long actoId) {
		return actoFirmaRepository.findAllByActoIdAndEsActo(actoId, true);
	}
	
	public List<Acto> getActosVigentesTraslativosParaPredio(long predioId){
		List<ClasifActo> clasifActoList = new ArrayList<>(2);

		clasifActoList.add(clasifActoRepository.getOne(
			Constantes.ClasifActo.TRASLATIVOS.getIdClasifActo())
			);
		clasifActoList.add(clasifActoRepository.getOne(
			Constantes.ClasifActo.ACTOS_MODIFICATORIOS_DEL_INMUEBLE.getIdClasifActo())
			);

		return actoRepository.findDistinctByStatusActoIdAndActoPrediosParaActosPredioIdAndTipoActoClasifActoIn(Constantes.StatusActo.ACTIVO.getIdStatusActo(), predioId, clasifActoList);
	}
	
	public List<Acto> getActosCancelacionesVigentesParaActo(long actoId){
		return actoRepository.findDistinctByStatusActoIdAndActoPrediosParaActosActoPredioMontosParaActoPrediosActoIdAndTipoActoClasifActoId(Constantes.StatusActo.ACTIVO.getIdStatusActo(), actoId, Constantes.ClasifActo.CANCELACIONES.getIdClasifActo());
	}	
		
	
	public List<Acto> findByActoPrediosParaActosPredioIdAndPrelacionId(Long predioId, Long prelacionId){
		List<Acto> lstActos = actoRepository.findDistinctByActoPrediosParaActosPredioIdAndPrelacionId(predioId, prelacionId);
		if(lstActos != null){
			for(Acto acto:lstActos){
				if(acto.getActoFirmasParaActos() != null && !acto.getActoFirmasParaActos().isEmpty()){
					acto.setModificable(false);
				}
			}
		}
		return lstActos;
	}

	public List<Acto> findByActoPrediosParaActosPredioId(Long predioId){
		List<Acto> lstActos = actoRepository.findByActoPrediosParaActosPredioId(predioId);
		if(lstActos != null){
			for(Acto acto:lstActos){
				if(acto.getActoFirmasParaActos() != null && !acto.getActoFirmasParaActos().isEmpty()){
					acto.setModificable(false);
				}
			}
		}
		return lstActos;
	}
	
	public List<Acto> findByActoPrediosParaActosMuebleId(Long idMueble){
		List<Acto> lstActos =actoRepository.findByActoPrediosParaActosMuebleId(idMueble);
		if(lstActos != null){
			for(Acto acto:lstActos){
				if(acto.getActoFirmasParaActos() != null && !acto.getActoFirmasParaActos().isEmpty()){
					acto.setModificable(false);
				}
			}
		}
		return lstActos; 
	}
	
	public List<Acto> findByActoPrediosParaActosPersonaJuridicaId(Long idPj){
		List<Acto> lstActos = actoRepository.findByActoPrediosParaActosPersonaJuridicaId(idPj);
		if(lstActos != null){
			for(Acto acto:lstActos){
				if(acto.getActoFirmasParaActos() != null && !acto.getActoFirmasParaActos().isEmpty()){
					acto.setModificable(false);
				}
			}
		}
		return lstActos; 
	}
	
	public List<Acto> findByActoPrediosParaActosFolioSeccionAuxiliarId(Long idSeccionAuxiliar){
		List<Acto> lstActos =  actoRepository.findByActoPrediosParaActosFolioSeccionAuxiliarId(idSeccionAuxiliar);
		if(lstActos != null){
			for(Acto acto:lstActos){
				if(acto.getActoFirmasParaActos() != null && !acto.getActoFirmasParaActos().isEmpty()){
					acto.setModificable(false);
				}
			}
		}
		return lstActos; 
	}
	
	public List<Acto> getActosParaHeredarByPredio(Long idPredio) {
		
		return 	actoRepository.findActivosByStatusActoAndPredioId(Constantes.StatusActo.ACTIVO.getIdStatusActo(), idPredio);		
		
		
	}

	public List<Documento> getDocumentosParaActo (Acto acto) {
		final List<Documento> documentos = new ArrayList<>();
		if (!org.springframework.util.ObjectUtils.isEmpty (acto) && !isEmpty(acto.getActoDocumentosParaActos()) ) {
			acto.getActoDocumentosParaActos().stream().forEach(actoDocumento -> documentos.add(actoDocumento.getDocumento()));
		}
		return  documentos;

	}
		
	public List<Acto> findActosByTipoActoPadreEnTipoActoTipoActo(Long tipoActo, Long folioId,Long tipoFolioId) {
		return actoRepository.findActosByTipoActoPadreEnTipoActoTipoActo(tipoActo, folioId, Constantes.ETipoFolio.fromLong(tipoFolioId), Constantes.StatusActo.ACTIVO);
	}
	
	@Transactional
	public void updateFechaRegistro(Long actoId, Date fechaRegistro) {
		actoRepository.updateFechaRegistro(actoId, fechaRegistro);
	}

	public String nombreTipoActo(Acto acto) {
		String nombre="";
		if( acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_COPIA_CERTIFICADA) ) {
    		nombre ="EXPEDICIÓN DE COPIA CERTIFICADA DE TÍTULO DE PROPIEDAD O DE CUALQUIER OTRO DOCUMENTO EXISTENTE EN LOS LIBROS O ARCHIVO DEL REGISTRO";
    	} else if( acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_COPIA_SIMPLE) ) {
    		nombre ="EXPEDICIÓN DE COPIA SIMPLE DE TÍTULO DE PROPIEDAD O DE CUALQUIER OTRO DOCUMENTO EXISTENTE EN LOS LIBROS O ARCHIVO DEL REGISTRO";
    	} else if( acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_APORTACIONES_DE_BIENES) ) {
    		nombre ="APORTACIONES DE BIENES INMUEBLES A ASOCIACIONES O SOCIEDADES MERCANTILES O CIVILES Y LA ADJUDICACIÓN O FUSIÓN DE ESTAS, SOBRE EL CAPITAL ADJUDICADO O FUSIONADO";
    	} else if( acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_FIDEICOMISO) ) {
    		nombre ="FIDEICOMISO DE AFECTACIÓN O ADMINISTRACIÓN EN GARANTÍA Y EN EL QUE EL O LOS FIDEICOMITENTES, SE RESERVEN EXPRESAMENTE LA PROPIEDAD";
    	} else if( acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_EMISIONES_DE_ACCIONES) ) {
    		nombre ="REGISTRO DE EMISIONES DE ACCIONES, CÉDULAS, OBLIGACIONES O CERTIFICADOS DE PARTICIPACIÓN, DEPÓSITO O DE COPIAS AUTORIZADAS POR BALANCE, TRANSFORMACIÓN DE SOCIEDADES Y EN GENERAL OTROS ACTOS INSCRIBIBLES";
    	} else if( acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_AUTO_DE_DESIGNACIÓN) ) {
    		nombre ="AUTO DE DESIGNACIÓN, ACEPTACIÓN, DISCERNIMIENTO O SUSTITUCIÓN DEL CARGO DE ALBACEA; DECLARACIÓN Y RECONOCIMIENTO DE HEREDEROS, SI CONSTA EN EL MISMO DOCUMENTO; Y REPUDIO DE LA HERENCIA";
    	} else {
    		nombre =acto.getTipoActo().getNombre();
    	}
		return nombre;
	}


	public Set<ActoPredioCampo> getActoPredioCampoByActo(Long actoId ){

		Set<ActoPredioCampo> detalles =  new HashSet<ActoPredioCampo>();
		Acto acto =  actoRepository.findOne(actoId);
							int version=0;		
							for(ActoPredio ap : acto.getActoPrediosParaActos()) {
									
									if(ap.getVersion()>version && ap.getActoPredioCamposParaActoPredios()!=null  && ap.getActoPredioCamposParaActoPredios().size()>0){

										detalles.addAll(ap.getActoPredioCamposParaActoPredios());
										version=ap.getVersion();
									}
									
							}

		return detalles;					

	}
	public void cancelarActos(List<Acto> actosCaducados) {
		if (actosCaducados == null || actosCaducados.isEmpty()) {
			return;
		}

		StatusActo statusCancelado = statusActoRepository
				.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo());

		for (Acto acto : actosCaducados) {
			acto.setStatusActo(statusCancelado);
			System.out.println("Cancelando: " + acto.getId());
			actoRepository.save(acto);
		}
	}
	
		public void cancelarActo(Acto acto) {
		if (acto == null) {
			return;
		}

		StatusActo statusCancelado = statusActoRepository
				.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo());

		acto.setStatusActo(statusCancelado);
		System.out.println("Cancelando: " + acto.getId());
		actoRepository.save(acto);
	}
		/**
		 * checa si los predios tiene exactamente los mismos propietarios y con los mismos porcentajes
		 */
		public Pair<Boolean, List<PredioPersona>> findTitularesFusionByActoId(Long actoId){
			List<PrelacionPredio> pps = prelacionPredioRepository.findByActoId(actoId);
			boolean iguales=true;
			Hashtable<Long, PredioPersona> prop = new Hashtable<Long, PredioPersona>();

	 		if (pps != null && pps.size()>0) {
				List<PredioPersona> prediopresonas = predioPersonaService.findPropietariosActuales(pps.get(0).getPredio().getId(), false);
				for (PredioPersona predioPersona: prediopresonas) {
					if (predioPersona!=null && predioPersona.getPersona()!=null) {
						log.debug("predioPersona = {}, {}", predioPersona, predioPersona.getPersona());
						prop.put(predioPersona.getPersona().getId(), predioPersona);
					}
				}
			}

	 		for (int i = 1; i < pps.size(); i++) {
			    PrelacionPredio pp = pps.get(i);
			    List<PredioPersona> prediopresonas = predioPersonaService.findPropietariosActuales(pp.getPredio().getId(), false);
			    for (PredioPersona predioPersona: prediopresonas) {
			    	PredioPersona encontrada = prop.get(predioPersona.getPersona().getId());
			    	if (encontrada == null || !encontrada.getPorcentajeDd().equals(predioPersona.getPorcentajeDd()) || !encontrada.getPorcentajeUv().equals(predioPersona.getPorcentajeUv())  ) {
			    		iguales=false;
			    		if (encontrada == null) {
			    			prop.put(predioPersona.getPersona().getId(), predioPersona);
			    		}
			    	}
			    }
			}


	 		return Pair.of(iguales, new ArrayList<PredioPersona>(prop.values()));

	 	}


	 	public List<Acto> findActosByActoIdActivosIsReplicaPorFusion(Long actoId) {
	 		log.debug("aqui si entra "+actoId);
			List<PrelacionPredio> pps = prelacionPredioRepository.findByActoId(actoId);
			List<Long> ids = new ArrayList<Long>();

	 		for (int i = 0; i < pps.size(); i++) {
			    PrelacionPredio pp = pps.get(i);
			    ids.add(pp.getPredio().getId());
			}

	 		return actoRepository.findActos(ids.toArray(new Long[ids.size()]), Constantes.ETipoFolio.PREDIO, Constantes.StatusActo.ACTIVO, true);
		}

		
	@Transactional
	public Acto  deleteActo(Long actoId){
			Acto acto=null;
		try {
			 acto= actoRepository.findOneById(actoId);

			 if(acto!=null) {

			//deleteActoRel(acto);
			
			List<ActoRequisito> reqActo =actoRequisitoRepository.findAllByActo(acto);
			
	
			for(ActoRequisito ar : reqActo){
					actoRequisitoRepository.delete(ar);
			}
			
//			List<ActoDocumento> documentos = actoDocumentoRepository.getAllActoDocumentoByActoId(acto.getId());
			List<ActoDocumento> documentos = actoDocumentoRepository.getAllByActoId(acto.getId());

			for (ActoDocumento ad : documentos) {
				actoDocumentoRepository.delete(ad);
			}

			
			List<ActoPredio> actPredios =actoPredioRepository.findAllByActo(acto);

			actoPredioMontoRepository.deleteByActoPredioActoId(acto.getId());
			actoPredioMontoRepository.deleteByActoId(acto.getId());
//		 	for(ActoPredio ap : actPredios) {
//			 List<PjPersona> pjs =pjPersonaRepository.findAllByActoPredio(ap);
//			 for(PjPersona pj:pjs){
//				 pjPersonaRepository.delete(pj.getId());
//			 }
//			 List<BitacoraMantenimiento> registros = bitacoraMantenimientoRepository.findAllByActoPredioId(ap.getId());
//				for (BitacoraMantenimiento registro : registros) {
//					bitacoraMantenimientoRepository.delete(registro);
//					
//				}
//				
//			} 
			
			for (ActoPredio ap : actPredios) {
				predioPersonaRepository.deleteByActoPredioId(ap.getId());
				actoFolioReciboRepository.deleteByActoPredioId(ap.getId());
				List<ActoPredioCampo> actosPredioCampo = actoPredioCampoRepository.findByActoPredioId(ap.getId());
				for (ActoPredioCampo apc : actosPredioCampo) {
					actoPredioCampoRepository.delete(apc);
				}
				bitacoraMantenimientoService.deleteByActoPredio(ap.getId());
				actoPredioRepository.delete(ap);

			}

			actoFirmaRepository.deleteByActoId(acto.getId());

			prelacionContratanteRepository.deleteByActoId(acto.getId());

			foliosFracCondRepository.deleteByActoId(acto.getId());
			actoControlHeredaRepository.deleteByActoId(actoId);
			actoControlHeredaRepository.deleteByActoHeredadoId(actoId);
			actoRelRepository.deleteByActoId(actoId);
			actoRelRepository.deleteByActoSigId(actoId);
			actoRelRepository.deleteByActoAntId(actoId);
			avisoRepository.deleteByActoId(actoId);
			
			bitacoraActoRechazoService.deleteByActo(actoId);
			
			recursoInconformidadRepository.deleteByActoId(actoId);
			actoRepository.delete(acto);

		     }
			return acto;
		}
		catch (Exception err) {

			err.printStackTrace();
			return null;
		}

	}


	public void deleteActoRel(Acto acto) {
		
		actoRelRepository.deleteByActoAntId(acto.getId());
		actoRelRepository.deleteByActoSigId(acto.getId());
		actoRelRepository.deleteByActoId(acto.getId());
	}
	
	public boolean activaActo( Acto acto )
	{
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		String comentario = "ACTIVA ACTO "+acto.getTipoActo().getNombre() + 
				" DE FECHA "+UtilFecha.formatToDatePattern(acto.getFechaRegistro());
		bitacoraMantenimientoService.create(usuario,acto,comentario,"ACTIVA_ACTO");
		StatusActo statusActo = statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo());
		acto.setStatusActo(statusActo);
		actoRepository.saveAndFlush(acto);

		return true;
	}
	
	public boolean invalidarActo( Acto acto )
	{
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		String comentario = "EXTINGUE ACTO "+acto.getTipoActo().getNombre() + 
				" DE FECHA "+UtilFecha.formatToDatePattern(acto.getFechaRegistro());
		bitacoraMantenimientoService.create(usuario,acto,comentario,"EXTINGUE_ACTO");
		StatusActo statusActo = statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo());
		acto.setStatusActo(statusActo);
		actoRepository.saveAndFlush(acto);

		return true;
	}
	

	@Transactional
	public void desmaterializar(Long actoId) throws Exception {
		try {
			Acto acto = actoRepository.findOne(actoId);
			if (acto.getTipoActo().getId().equals(Constantes.FUSION_TIPO_ACTO_ID)) {
				List<ActoPredio> actos = prelacionService
						.findAllActosFusionByPrelacionAndVfFalseSalida(acto.getPrelacion().getId());
				desmaterializar(actos);
			} else {
				materializacionActoService.deMaterializarNew(acto);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	@Transactional
	public void desmaterializar(List<ActoPredio> actos) throws Exception {
		try {
			for (ActoPredio ap : actos) {
				materializacionActoService.deMaterializarNew(ap.getActo());
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public Acto update(Acto acto) {
		actoRepository.saveAndFlush(acto);
		return acto;
	}

	public List<PrelacionPredio> findPrelacionPredioByActoId(Long actoId) {
		return prelacionPredioRepository.findByActoId(actoId);
	}
	
	public List<Acto> findActosByActoIdActivosIsReplica(Long actoId) {
		Optional<ActoPredio> ap =  actoPredioRepository.findFirstByActoIdOrderByVersionDesc(actoId);
		List<Long> ids = new ArrayList<Long>();
		
		if(ap.isPresent())
			ids.add(ap.get().getPredio().getId());
		
		return actoRepository.findActos(ids.toArray(new Long[ids.size()]), Constantes.ETipoFolio.PREDIO,
				Constantes.StatusActo.ACTIVO, true);
	}
	
	
	public List<Acto> findActosByPredioIdReplica(Long actoId) {
		Optional<ActoPredio> ap = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(actoId);
		Integer[] folio = {0};
		Predio predio = null;
		
		if(ap.isPresent()) {
			Set<ActoPredioCampo> actosPrediosCampos =  ap.get().getActoPredioCamposParaActoPredios();
			actosPrediosCampos.forEach(x->{
				if(x.getModuloCampo().getCampo().getTipoCampo().getId().equals(Constantes.TipoCampo.FOLIO_MATRIZ.getIdTipoCampo())) {
					
					folio[0] = x.getValor()!=null && !x.getValor().trim().isEmpty() ? Integer.parseInt(x.getValor()) : null;
				}
			});
		}
		
		if(folio[0]!=null) {
			try {
				predio  = predioService.findPredioByNoFolioOficina(folio[0]);	
			}catch(Exception e) {	
			}	
		}
		
		if(predio!=null) 
		{
			List<Long> ids = new ArrayList<Long>();
			ids.add(predio.getId());
			return actoRepository.findActos(ids.toArray(new Long[ids.size()]), Constantes.ETipoFolio.PREDIO,
					Constantes.StatusActo.ACTIVO, true);	
		}else
			return null;
		
	}
	
	
	
	@Transactional
	private void firmaActosFusion(ActoFirma actoFirmaSource, Long prelacionId) {
		List<ActoPredio> actos = prelacionService.findAllActosFusionByPrelacionAndVfFalse(prelacionId);
		for (ActoPredio ap : actos) {
			ActoFirma actoFirma = new ActoFirma();
			actoFirma.setActo(ap.getActo());
			actoFirma.setArchivo(null);
			//Calendar calendar = Calendar.getInstance();
			//calendar.setTimeInMillis();
			actoFirma.setPkcs7( actoFirmaSource.getPkcs7());
			actoFirma.setFirma(actoFirmaSource.getFirma());
			actoFirma.setSecuencia( actoFirmaSource.getSecuencia()); 
			actoFirma.setPolitica( actoFirmaSource.getPolitica());
			actoFirma.setDigestion( actoFirmaSource.getDigestion()) ;
			actoFirma.setSecuenciaTS( actoFirmaSource.getSecuenciaTS());
			actoFirma.setEstampilla(actoFirmaSource.getEstampilla()); 
			actoFirma.setEsActo(true);
			actoFirma.setUsuario(actoFirmaSource.getUsuario());
			
			actoFirmaRepository.save(actoFirma);
		}
		
	}
	
	/**
	 * OBTIENE TITULARES DE ACTO PREDIO
	 */
	public List<PredioPersona> findTitularesByActoId(Long actoId){
		Optional<ActoPredio> ap =  actoPredioRepository.findFirstByActoIdOrderByVersionDesc(actoId);
		List<PredioPersona> prop = new ArrayList<PredioPersona>();

		if(ap.isPresent() && ap.get().getPredio() != null) {
			 prop = predioPersonaService.findPropietariosActuales(ap.get().getPredio().getId(), false);	
		}
 		return prop;
 	}
	
	public Acto findById(Long actoId) {
		return actoRepository.findOneById(actoId);
	}
	
	public Page<Acto> getActosPageReport(Long prelacionId){
		Pageable pageable =  new PageRequest(0,Constantes.PAGE_SIZE_REPORT);
		Page<Acto> actos = actoRepository.findAllByPrelacionIdAndVfFalse(prelacionId,pageable);
		return actos;
	}
	
	
	public Page<Acto> getActosPageReport(Long prelacionId,Integer pageSize){
		Pageable pageable =  new PageRequest(0,pageSize);
		Page<Acto> actos = actoRepository.findAllByPrelacionIdAndVfFalse(prelacionId,pageable);
		return actos;
	}
	
	public Optional<Acto> findPrimerAvisolByPrelacion(Long prelacionid,List<Long>tiposActo) {
		return actoRepository.findAllByPrelacionIdAndTipoActoIdIn(prelacionid,tiposActo);
	}
	
	@Transactional
	public Acto  deleteActoServ(Long actoId){
		Acto acto=null;
		try {
			acto= actoRepository.findOneById(actoId);
			
			busquedaResultadoService.clearBusquedaFromPrelacion(acto.getPrelacion().getId());
			
			if(acto!= null) {
				if(acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_PRIMER_AVISO) ||
						acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_CERTIFICADO_LG_PRIMER_AVISO)) {
					deleteActosPrelacion(acto.getPrelacion().getId());
				}else {
					deleteActo(actoId);
				}
			}
		}catch (Exception err) {

			err.printStackTrace();
			return null;
		}
		return acto;
	}
	
	public BitacoraActoRechazo actoRechazo(Long id) {
		List<BitacoraActoRechazo> bitacora = new ArrayList<BitacoraActoRechazo>();
		BitacoraActoRechazo rechazado = null;
		List<Acto> actosRechazados =  actoRepository.findActosByPrelacionIdAndRechazados(id);
		if(actosRechazados != null && actosRechazados.size()>0) {
			
			bitacora = bitacoraActoRechazoService.findBitacoraByActo(actosRechazados.get(0).getId());
			if(bitacora != null && bitacora.size()>0) {
				rechazado = new BitacoraActoRechazo();
				rechazado= bitacora.get(0);
			}
			
		}
		return rechazado;
				
	}
	
}
