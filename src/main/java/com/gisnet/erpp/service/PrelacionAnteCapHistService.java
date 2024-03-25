package com.gisnet.erpp.service;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.repository.AntecedenteRepository;
import com.gisnet.erpp.repository.LibroRepository;
import com.gisnet.erpp.repository.PrelacionAnteCapHistRepository;
import com.gisnet.erpp.repository.PrelacionAnteRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.PrioridadRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.repository.ActoFirmaRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.StatusAntecedenteRepository;
import com.gisnet.erpp.repository.StatusRepository;
import com.gisnet.erpp.repository.TipoEntradaRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoFirma;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionAnte;
import com.gisnet.erpp.domain.PrelacionAnteCapHist;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.SeccionPorOficina;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.Usuario;

import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.MessageObject;
import com.gisnet.erpp.web.api.prelacionAnte.PrelacionAnteDTO;

@Service
public class PrelacionAnteCapHistService{
	
	@Autowired
	PrelacionAnteCapHistRepository prelacionAnteCapHistRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	StatusAntecedenteRepository statusAntecedenteRepository;
	
	@Autowired
	PrelacionAnteRepository prelacionAnteRepository;
	
	@Autowired
	private PrioridadRepository prioridadRepository;
	
	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	private EntityManagerFactory managerFactory;
	
	@Autowired
	private LibroRepository libroRepository;
	
	@Autowired
	private PrelacionRepository prelacionRepository;
	
	@Autowired
	private AntecedenteRepository antecedenteRepository;
	
	@Autowired
	private TipoFolioRepository tipoFolioRepository;
	
	@Autowired
	private PredioRepository predioRepository;
	@Autowired
	private ActoFirmaRepository actoFirmaRepository;
	
	@Autowired
	private PrelacionPredioRepository prelacionPredioRespository;
	
	@Autowired
	private StatusActoRepository statusActoRepository;
	
	@Autowired
	private ActoRepository actoRepository;
	
	
	@Autowired
	private TipoEntradaRepository tipoEntradaRepository;
	
	
	@Autowired
	private ActoPredioRepository actoPredioRepository;
	//SERVICIOS
		@Autowired
		private PrelacionAnteService prelacionAnteService;
	
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Transactional
	public Long setCapturado(PrelacionAnteCapHist prelacionAnteCapHist){
		prelacionAnteCapHist= prelacionAnteCapHistRepository.findOne(prelacionAnteCapHist.getId());
		prelacionAnteCapHist.setStatusAntecedente(statusAntecedenteRepository.findOne(2L));
		prelacionAnteCapHistRepository.save(prelacionAnteCapHist);
		return prelacionAnteCapHist.getId();
	}
	
	public Page<PrelacionAnteCapHist> findAllCapturableBy(String numLibro, String libroBis, Long seccionPorOficinaId, Integer anio, String volumen, String carga, Pageable pageable, String tipoBusqueda) {
		Usuario usuarioCaptura = null;
		if ("CAPTURISTAS".equals(tipoBusqueda)){
			usuarioCaptura = usuarioRepository.findOne(SecurityUtils.getCurrentUserId());
		}		 
		List<Long> statusAnteIds = this.setStatusByTipoBusqueda(tipoBusqueda);		
		return prelacionAnteCapHistRepository.findAllBy(numLibro, libroBis, seccionPorOficinaId, anio, volumen, usuarioCaptura, statusAnteIds, carga, pageable);
	}
	public Boolean save(PrelacionAnteCapHist prelacionAnteCapHist)throws Exception {
		this.prelacionAnteCapHistRepository.saveAndFlush(prelacionAnteCapHist);
		return true;
	}
	
	
	public PrelacionAnteCapHist findFirstCarga(String numLibro, Long seccionPorOficinaId, Integer anio, String volumen, String carga) 
	{
	  	return this.prelacionAnteCapHistRepository.findFirstCarga(numLibro, seccionPorOficinaId, anio, volumen, carga);
	}
	
	@Transactional
	public Boolean actualizaValidador(PrelacionAnteCapHist carga) 
	{
	  this.prelacionAnteCapHistRepository.updateValidador(carga.getUsuarioValidacion().getId(),carga.getCargaTrabajo());
	  return true;
	}
	
	public Boolean generarCargas(List<PrelacionAnteCapHist> cargas)throws Exception {
		Predio predio;
		PrelacionAnte prelacionAnte = new PrelacionAnte();
		ActoFirma actoFirma = new ActoFirma();
		for(PrelacionAnteCapHist carga: cargas){
			if(!carga.getIndVigencia())
			{
				prelacionAnte.setAnio(carga.getLibro().getAnio());
				prelacionAnte.setDocumento(carga.getDocumento());
				prelacionAnte.setDocumentoBis(carga.getDocumentoBis());
				prelacionAnte.setFolioSeccionAuxiliar(carga.getFolioSeccionAuxiliar());
				prelacionAnte.setLibro(carga.getLibro().getNumLibro());
				prelacionAnte.setLibroBis(carga.getLibro().getLibroBis());
				prelacionAnte.setMueble(carga.getMueble());
				prelacionAnte.setNombreLibro(carga.getLibro().getNombreLibro());
				prelacionAnte.setOficina(carga.getLibro().getSeccionesPorOficina().getOficina());
				prelacionAnte.setPersonaJuridica(carga.getPersonaJuridica());
				prelacionAnte.setPredio(carga.getPredio());
				prelacionAnte.setPrelacion(carga.getPrelacion());
				prelacionAnte.setSeccion(carga.getLibro().getSeccionesPorOficina().getSeccion());
				prelacionAnte.setTipoDoc(carga.getLibro().getTipoDoc());
				prelacionAnte.setTipoFolio(carga.getTipoFolio());
				prelacionAnte.setTipoLibro(carga.getLibro().getTipoLibro());
				prelacionAnte.setValidadoCyv(true);
				prelacionAnte.setValido(true);
				prelacionAnte.setVolumen(carga.getLibro().getVolumen());
				
				this.prelacionAnteRepository.saveAndFlush(prelacionAnte);
				carga.setStatusAntecedente(statusAntecedenteRepository.findOne(4L));
				this.prelacionAnteCapHistRepository.saveAndFlush(carga);
				
				predio = predioRepository.findOne(prelacionAnte.getPredio().getId());
				if(carga.getMatriz() != null &&  carga.getMatriz()) 
				{
				predio.setNoFolio(predioRepository.getFolioFromPredioMatrizSequentce().intValue());			
				}
				else
				{
				predio.setNoFolio(predioRepository.getFolioFromPredioSequence().intValue());
				}
				predio.setBloqueado(false);
				predio.setCaratulaActualizada(true);
				predio.setOficina(prelacionAnte.getOficina());
				predio.setStatusActo(statusActoRepository.findOne(1L));
				
				for (ActoPredio ap : predio.getActoPrediosParaPredios()) {
					actoFirma.setActo(ap.getActo());
					actoFirma.setPkcs7("CAPTURAMASIVA ESPECIAL");
					actoFirma.setUsuario(carga.getUsuarioCaptura());
					this.actoFirmaRepository.saveAndFlush(actoFirma);
					actoFirma = new ActoFirma();
				}
				
				predioRepository.saveAndFlush(predio);
				
				prelacionAnte.getPredio().getPredioAntesParaPredios().forEach(x -> {
					Antecedente ant  = x.getAntecedente();
					ant.setDocumento(carga.getDocumento());
					antecedenteRepository.saveAndFlush(ant);
				});
				
				prelacionAnte = new PrelacionAnte();
			}
		}	
		return true;
	}
	
	public PrelacionAnteCapHist findOne(Long id) {
		return prelacionAnteCapHistRepository.findOne(id);
	}
	private List<Long> setStatusByTipoBusqueda(String tipoBusqueda) {
		List<Long> statusAnteIds = new ArrayList<>();
		switch(tipoBusqueda) {
			
		case "CAPTURISTAS":
			statusAnteIds.add(1L);
			statusAnteIds.add(2L);
		break;
		case "VALIDADORES":
			statusAnteIds.add(1L);
			statusAnteIds.add(2L);
			statusAnteIds.add(3L);
			statusAnteIds.add(4L);
		break;
		}
		
		return statusAnteIds;
	}
	
	@Transactional
	public Boolean validaCapturadas(List<PrelacionAnteCapHist> cargas) 
	{
		
		cargas.forEach((carga)->{
			if(!carga.getIndVigencia() && carga.getStatusAntecedente().getId() == 2) 
			{ //SI ES VIGENTE Y ESTATUS CAPTURADO
			  PrelacionAnteDTO prelacion = new 	PrelacionAnteDTO();
			  prelacion.setPrelacionAnteCapHist(carga);
			  prelacionAnteService.validado(prelacion);	
			}
		});
		
		return true;
	}
	
	
	@Transactional
	public MessageObject createWorkLoad (
		String numLibro,
		String libroBis,
		SeccionPorOficina seccionPorOficina, 
		Integer anio,	
		Usuario usuario,
		Usuario usuarioValidador,
		Integer dInsFin,
		Integer dInsInicio,
		String cargaTrabajo,
		String documentoBis,
		String volumen,
		Boolean matriz
		) throws Exception {
		
		Libro libro = libroRepository.findOneByNumLibroAndLibroBisAndSeccionesPorOficinaIdAndAnioAndActivoAndVolumen(numLibro, libroBis, seccionPorOficina.getId(), anio, true, volumen).orElse(null);
		if (libro == null) {
			throw new Exception ("El libro no existe");
		}

		//creacion de la nueva prelación
		PrelacionAnteCapHist nuevo = new PrelacionAnteCapHist();
		Prelacion prelacion = new Prelacion();
		prelacion.setAnio(Year.now().getValue());
		prelacion.setUsuarioSolicitan(usuarioRepository.findOne(SecurityUtils.getCurrentUserId()));
		prelacion.setConsecutivo(prelacionRepository.getConsecutivoNeg());
		prelacion.setFechaIngreso(new Date());
		prelacion.setPrioridad(prioridadRepository.findOne(1L));
		prelacion.setStatus(statusRepository.findOne(4L));
		prelacion.setOficina(seccionPorOficina.getOficina());
		prelacionRepository.saveAndFlush(prelacion);
		for(int i = dInsInicio.intValue(); i<=dInsFin.intValue(); i ++){
			if(this.antecedenteRepository.findOneBylibroAndDocumentoAndDocumentoBis(libro, String.valueOf(i), documentoBis).isPresent()) {
				throw new Exception ("Ya existe un antecedente con estos datos");
			}
			nuevo.setLibro(libro);
			nuevo.setDocumentoBis(documentoBis);
			nuevo.setDocumento(String.valueOf(i));
			nuevo.setPrelacion(prelacion);
			nuevo.setCargaTrabajo(cargaTrabajo);
			nuevo.setIndVigencia(false);
			nuevo.setUsuarioCaptura(usuario);
			nuevo.setUsuarioValidacion(usuarioValidador);
			nuevo.setStatusAntecedente(statusAntecedenteRepository.findOne(1L));
			nuevo.setTipoFolio(tipoFolioRepository.findOne(4L));
			nuevo.setNumeroPredio(1);
			nuevo.setMatriz(matriz);
			System.out.println(nuevo);
			prelacionAnteCapHistRepository.save(nuevo);
			nuevo = new PrelacionAnteCapHist();
		}
		
		prelacionAnteCapHistRepository.flush();	
		return new MessageObject("La Carga Se Ha Completado Correctamente");
	}
	
	@Transactional
	public MessageObject cloneInscripcion(PrelacionAnteCapHist carga) throws Exception 
	{
		
		//NUEVO PREDIO
		Integer numeroPredio =prelacionAnteCapHistRepository.findFirstByLibroIdAndDocumentoAndDocumentoBisOrderByNumeroPredioDesc(carga.getLibro().getId(),carga.getDocumento(),carga.getDocumentoBis()).getNumeroPredio();
		numeroPredio += 1;
		
		//creacion de la nueva prelación
		PrelacionAnteCapHist nuevo = new PrelacionAnteCapHist();
			nuevo.setPrelacion(carga.getPrelacion());
			nuevo.setLibro(carga.getLibro());
			nuevo.setDocumentoBis(carga.getDocumentoBis()!= null 
								  && !carga.getDocumentoBis().isEmpty() ? 
										 carga.getDocumentoBis() : null );
			nuevo.setDocumento(carga.getDocumento());
			nuevo.setCargaTrabajo(carga.getCargaTrabajo());
			nuevo.setIndVigencia(false);
			nuevo.setUsuarioCaptura(carga.getUsuarioCaptura());
			nuevo.setUsuarioValidacion(carga.getUsuarioValidacion());
			nuevo.setStatusAntecedente(statusAntecedenteRepository.findOne(1L));
			nuevo.setTipoFolio(tipoFolioRepository.findOne(4L));
			nuevo.setMatriz(carga.getMatriz());
			//nuevo.setFechaRegistro(new Date());
			nuevo.setNumeroPredio(numeroPredio);
			
		prelacionAnteCapHistRepository.save(nuevo);
		prelacionAnteCapHistRepository.flush();	
		return new MessageObject("Se creo nueva inscripción para nuevo predio");
	}
	@Transactional
	public MessageObject createPrelacion(Integer folio,Oficina oficina,TipoActo tipoActo,Usuario captura,Usuario validador)  throws Exception
	{
		Predio predio =  predioRepository.findByNoFolio(folio);
		if(predio== null )
			throw new Exception("El folio no fue identificado"); 
		if(predio.getOficina().getId() != oficina.getId())
			throw new Exception("El folio pertenece a la oficina "+ predio.getOficina().getNombre());
		if(predio.getBloqueado() != null && predio.getBloqueado())
			throw new Exception("El folio está bloqueado");
		
			Prelacion prelacion  = new Prelacion();
			prelacion.setConsecutivo(prelacionRepository.getConsecutivoNeg());
			
			prelacion.setFechaIngreso(new Date());
			prelacion.setOficina(oficina);
			prelacion.setPrioridad(prioridadRepository.findOne(Constantes.Prioridad.NORMAL.getIdPrioridad()));
			prelacion.setStatus(statusRepository.findOne(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus()));
		    prelacion.setUsuarioSolicitan(captura);
		    prelacion.setUsuarioAnalista(captura);
		    prelacion.setUsuarioCalificador(validador);
		    prelacion.setUsuarioCoordinador(validador);
		   
		    prelacion.setEmailEnviado(false);
		    prelacion.setExentoPago(true);
		    prelacion.setTipoEntrega(1);
		    prelacionRepository.saveAndFlush(prelacion);
		    
		    //PRELACION PREDIO
		    PrelacionPredio prelPredio = new  PrelacionPredio();
		    prelPredio.setEstatus(1);
		    prelPredio.setValido(false);
		    prelPredio.setVersion(1);
		    prelPredio.setPredio(predio);
		    prelPredio.setPrelacion(prelacion);
		    prelPredio.setTipoFolio(tipoFolioRepository.findOne(4l)); // INMUEBLE
		    prelacionPredioRespository.saveAndFlush(prelPredio);
		    prelPredio.setIdVersion(prelPredio.getId().toString()+prelPredio.getPrelacion().getId().toString());
		    prelacionPredioRespository.saveAndFlush(prelPredio);
		    
		    
		    //ACTO
		    Acto acto = new Acto();
		    acto.setFechaRegistro(new Date());
		    acto.setModificable(true);
		    acto.setOrden(1);
		    acto.setProcesado(false);
		    acto.setVersion(1);
		    acto.setPrelacion(prelacion);
		    acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		    acto.setTipoActo(tipoActo);
		    actoRepository.saveAndFlush(acto);
	    
	    
		    //ACTO PREDIO
		    ActoPredio actoPredio = new ActoPredio();
		    actoPredio.setActo(acto);
		    actoPredio.setVersion(1);
		    actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
		    actoPredioRepository.saveAndFlush(actoPredio);
	    
	    
		     return new MessageObject("Entrada asignada!");
		
	}
}
