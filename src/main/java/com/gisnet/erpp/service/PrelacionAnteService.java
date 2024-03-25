package com.gisnet.erpp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

import com.gisnet.erpp.domain.*;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.repository.AntecedenteRepository;
import com.gisnet.erpp.repository.AuxiliarAnteRepository;
import com.gisnet.erpp.repository.LibroRepository;
import com.gisnet.erpp.repository.MuebleAnteRepository;
import com.gisnet.erpp.repository.PjAnteRepository;
import com.gisnet.erpp.repository.PredioAnteRepository;
import com.gisnet.erpp.repository.PrelacionAnteCapHistRepository;
import com.gisnet.erpp.repository.PrelacionAnteRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.StatusAntecedenteRepository;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.web.api.prelacionAnte.PrelacionAnteDTO;
import com.gisnet.erpp.web.api.prelacionAnte.PrelacionAnteRestController;

@Service
public class PrelacionAnteService {
	
	private static final Logger log = LoggerFactory.getLogger(PrelacionAnteRestController.class);
	
	@Autowired
	private PrelacionAnteRepository prelacionAnteRepository;
	
	@Autowired
	private LibroRepository libroRepository;

	@Autowired
	private PredioService predioService;

	@Autowired
	private MuebleService muebleService;
	
	@Autowired
	private PersonaJuridicaService personaJuridicaService;
	
	@Autowired
	private FolioSeccionAuxiliarService folioSeccionAuxiliarService;
	
	@Autowired
	private AntecedenteRepository antecedenteRepository;
	
	@Autowired
	private PredioAnteRepository predioAnteRepository;
	
	@Autowired
	private MuebleAnteRepository muebleAnteRepository;
	
	@Autowired
	private PjAnteRepository pjAnteRepository;
	
	@Autowired
	private AuxiliarAnteRepository auxiliarAnteRepository;
	
	@Autowired
	private PrelacionPredioRepository prelacionPredioRepository;
	
	@Autowired
	private MaterializacionActoValidacionFoliosService materializacionActoValidacionFoliosService;
	
	@Autowired
	private PrelacionRepository prelacionRepository;

	@Autowired
	private StatusAntecedenteRepository statusAntecedenteRepository;
	
	@Autowired
	private PrelacionAnteCapHistRepository prelacionAnteCapHistRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public PrelacionAnte findOne(Long prelacionAnteId){
		return prelacionAnteRepository.findOne(prelacionAnteId);
	}

	public List<PrelacionAnte> findPrelacionAnteByPrelacion(Long idPrelacion){
		return prelacionAnteRepository.findByPrelacionId(idPrelacion);
	}
	
	public PrelacionAnte createFromAntecedente(Antecedente antecedente, Long prelacionId) {
		PrelacionAnte prelacionAnte = new PrelacionAnte();
		Prelacion prelacion = prelacionRepository.findOne(prelacionId);
		
		Libro libro = libroRepository.findOne(antecedente.getLibro().getId());
		
		prelacionAnte.setLibro(libro.getNumLibro());
		prelacionAnte.setLibroBis(libro.getLibroBis());
		prelacionAnte.setOficina(libro.getSeccionesPorOficina().getOficina());
		prelacionAnte.setSeccion(libro.getSeccionesPorOficina().getSeccion());
		prelacionAnte.setTipoDoc(libro.getTipoDoc());
		
		prelacionAnte.setDocumento(antecedente.getDocumento());
		prelacionAnte.setDocumentoBis(antecedente.getDocumentoBis());
		
		prelacionAnte.setPrelacion(prelacion);
		
		prelacionAnteRepository.save(prelacionAnte);
		
		return prelacionAnte;		
	}
	
	
	public PrelacionAnteDTO findPredioAnteDTOByPrelacionAnteId(long prelacionAnteId){
		PrelacionAnteDTO dto = new PrelacionAnteDTO();
		PrelacionAnte prelacionAnte = prelacionAnteRepository.findOne(prelacionAnteId);
		
		//setea la bandera de validado a false para que obligar al usuario a materializar el predio
		prelacionAnte.setValidadoCyv(false);
		prelacionAnteRepository.save(prelacionAnte);
		
		dto.setPrelacionAnte(prelacionAnte);		
		Antecedente antecedente = new Antecedente();
		
		Libro libro = libroRepository.findLibroBy(prelacionAnte.getLibro(), prelacionAnte.getLibroBis(), prelacionAnte.getSeccion().getId(), prelacionAnte.getOficina().getId(), prelacionAnte.getAnio(), prelacionAnte.getVolumen());
		
		antecedente.setLibro(libro);

		antecedente.setDocumento(prelacionAnte.getDocumento());
		antecedente.setDocumentoBis(prelacionAnte.getDocumentoBis());
		
		dto.setAntecedente(antecedente);
		
		return dto;
	}
	
	@Transactional
	public Long updatePredio(PrelacionAnteDTO prelacionAnteDTO){
		PrelacionAnte prelacionAnte =null;
		Constantes.ETipoFolio tipo = null;
		Long id;
		
		tipo = Constantes.ETipoFolio.fromLong(prelacionAnteDTO. getPrelacionPredio().getTipoFolio().getId());
		
		
		switch (tipo) {
		case PREDIO:
			Predio predio = predioService.savePredio(prelacionAnteDTO.getPredioDTO());
			
			id = predio.getId();
			break;
		case MUEBLE:
			Mueble 	mueble = muebleService.saveMueble(prelacionAnteDTO.getMueble());
			
			id= mueble.getId();
			break;
		case PERSONAS_JURIDICAS:
			PersonaJuridica personaJuridica = personaJuridicaService.savePersonaJuridica(prelacionAnteDTO.getPersonaJuridica());
			
			id= personaJuridica.getId();
			break;
		case PERSONAS_AUXILIARES:
			FolioSeccionAuxiliar folioSeccionAuxiliar =  folioSeccionAuxiliarService.saveFolioSeccionAuxiliar(prelacionAnteDTO.getFolioSeccionAuxiliar());
			
			id= folioSeccionAuxiliar.getId();
			break;
		default:
			throw new IllegalArgumentException("Tipo de Folio no soportado");
		}
				
		return id;		
	}


	@Transactional
	public Long save(PrelacionAnteDTO prelacionAnteDTO){
		PrelacionAnte prelacionAnte =null;
		Constantes.ETipoFolio tipo = null;
		Long id;
		
		if (prelacionAnteDTO.getPrelacionPredio()==null){
			if (prelacionAnteDTO.getPrelacionAnte()==null) {
				return savePrelacionAnteCapHist(prelacionAnteDTO);
			}
			else {
				prelacionAnte = prelacionAnteRepository.findOne(prelacionAnteDTO.getPrelacionAnte().getId());
				tipo = Constantes.ETipoFolio.fromLong(prelacionAnte.getTipoFolio().getId());
			} 
		}
		else{
			tipo = Constantes.ETipoFolio.fromLong(prelacionAnteDTO. getPrelacionPredio().getTipoFolio().getId());
		}
		Antecedente antecedente = prelacionAnteDTO.getAntecedente();
		
		switch (tipo) {
		case PREDIO:
			Predio predio = predioService.savePredio(prelacionAnteDTO.getPredioDTO());
			if (prelacionAnteDTO.getPredioDTO().getId()==null && prelacionAnteDTO.getPrelacionPredio()==null){
				prelacionAnte.setPredio(predio);	
				prelacionAnteRepository.save(prelacionAnte);
				
				antecedenteRepository.save(antecedente);
				
				PredioAnte predioAnte = new PredioAnte();
				predioAnte.setAntecedente(antecedente);
				predioAnte.setPredio(predio);
				
				predioAnteRepository.save(predioAnte);
			}
			id = predio.getId();
			break;
		case MUEBLE:
			Mueble 	mueble = muebleService.saveMueble(prelacionAnteDTO.getMueble());
			if (prelacionAnteDTO.getMueble().getId()==null  && prelacionAnteDTO.getPrelacionPredio()==null){
				prelacionAnte.setMueble(mueble);	
				prelacionAnteRepository.save(prelacionAnte);
				
				antecedenteRepository.save(antecedente);
				
				MuebleAnte muebleAnte = new MuebleAnte();
				muebleAnte.setAntecedente(antecedente);
				muebleAnte.setMueble(mueble);
				
				muebleAnteRepository.save(muebleAnte);

			}
			id= mueble.getId();
			break;
		case PERSONAS_JURIDICAS:
			PersonaJuridica personaJuridica = personaJuridicaService.savePersonaJuridica(prelacionAnteDTO.getPersonaJuridica());
			if (prelacionAnteDTO.getPersonaJuridica().getId()==null  && prelacionAnteDTO.getPrelacionPredio()==null){
				prelacionAnte.setPersonaJuridica(personaJuridica);	
				prelacionAnteRepository.save(prelacionAnte);
				
				antecedenteRepository.save(antecedente);
				
				PjAnte personaJuridicaAnte = new PjAnte();
				personaJuridicaAnte.setAntecedente(antecedente);
				personaJuridicaAnte.setPersonaJuridica(personaJuridica);
				
				pjAnteRepository.save(personaJuridicaAnte);

			}
			id= personaJuridica.getId();
			break;
		case PERSONAS_AUXILIARES:
			FolioSeccionAuxiliar folioSeccionAuxiliar =  folioSeccionAuxiliarService.saveFolioSeccionAuxiliar(prelacionAnteDTO.getFolioSeccionAuxiliar());
			if (prelacionAnteDTO.getFolioSeccionAuxiliar().getId()==null  && prelacionAnteDTO.getPrelacionPredio()==null){
				prelacionAnte.setFolioSeccionAuxiliar(folioSeccionAuxiliar);	
				prelacionAnteRepository.save(prelacionAnte);
				
				antecedenteRepository.save(antecedente);
				
				AuxiliarAnte folioSeccionAuxiliarAnte = new AuxiliarAnte();
				folioSeccionAuxiliarAnte.setAntecedente(antecedente);
				folioSeccionAuxiliarAnte.setFolioSeccionAuxiliar(folioSeccionAuxiliar);
				
				auxiliarAnteRepository.save(folioSeccionAuxiliarAnte);
			}
			id= folioSeccionAuxiliar.getId();
			break;
		default:
			throw new IllegalArgumentException("Tipo de Folio no soportado");
		}
				
		return id;		
	}	

	@Transactional
	public Long validado(PrelacionAnteDTO prelacionAnteDTO){
		PrelacionAnte prelacionAnte =null;
		PrelacionPredio prelacionPredio =null;
		PrelacionAnteCapHist prelacionAnteCapHist =null;
		Long id;
		
		if(prelacionAnteDTO.getPrelacionAnteCapHist() != null) {
			prelacionAnteCapHist= prelacionAnteCapHistRepository.findOne(prelacionAnteDTO.getPrelacionAnteCapHist().getId());
			id = prelacionAnteCapHist.getId();
			prelacionAnteCapHist.setStatusAntecedente(statusAntecedenteRepository.findOne(3L));
			
			//prelacionAnteCapHist.setUsuarioValidacion(usuarioRepository.findOne(SecurityUtils.getCurrentUserId()));
			//materializacionActoValidacionFoliosService.desMaterializarPredio(prelacionAnteCapHist.getPrelacion().getId() , prelacionAnteCapHist.getPredio().getId());
		    //materializacionActoValidacionFoliosService.materializarPredio(prelacionAnteCapHist.getPrelacion().getId() , prelacionAnteCapHist.getPredio().getId());
			prelacionAnteCapHistRepository.save(prelacionAnteCapHist);
			return id;
		}
		
		if (prelacionAnteDTO.getPrelacionPredio()==null){
			prelacionAnte = prelacionAnteRepository.findOne(prelacionAnteDTO.getPrelacionAnte().getId());
			id = prelacionAnte.getId();
			prelacionAnte.setValidadoCyv(true);
			prelacionAnteRepository.save(prelacionAnte);
			System.out.println("prelacionAnteDTO.getPrelacionAnte().getId() "+ prelacionAnteDTO.getPrelacionAnte().getId());
			System.out.println("prelacionAnte.getPrelacion().getId()"+ prelacionAnte.getPrelacion().getId());
			if( prelacionAnte.getPredio()!=null) {
				 //materializacionActoValidacionFoliosService.desMaterializarPredio(prelacionAnte.getPrelacion().getId() , prelacionAnte.getPredio().getId());
			    // materializacionActoValidacionFoliosService.materializarPredio(prelacionAnte.getPrelacion().getId() , prelacionAnte.getPredio().getId());
			    }
		} else {
			prelacionPredio = prelacionPredioRepository.findOne(prelacionAnteDTO.getPrelacionPredio().getId());
			id = prelacionPredio.getId();
			prelacionPredio.setValidadoCyv(true);
			prelacionPredioRepository.save(prelacionPredio);
		} 
		return id;
	}
	

	public PrelacionAnte deletePrelacionAnteById(Long prelacionAnteId) {
		PrelacionAnte preAnte = prelacionAnteRepository.findOne(prelacionAnteId);
		prelacionAnteRepository.delete(preAnte);

		return preAnte;
	}
	
	private Long savePrelacionAnteCapHist(PrelacionAnteDTO prelacionAnteDTO) {
		PrelacionAnteCapHist prelacionAnteCapHist =null;
		Constantes.ETipoFolio tipo = null;
		Long id;
		Antecedente antecedente = prelacionAnteDTO.getAntecedente();
		prelacionAnteCapHist = prelacionAnteCapHistRepository.findOne(prelacionAnteDTO.getPrelacionAnteCapHist().getId());
		final PrelacionAnteCapHist tmp = prelacionAnteCapHist;		
		antecedente.setId(prelacionAnteCapHist.getLibro().getAntecedentesParaLibros().stream()
			.filter(x-> {
				try {
					return (x.getDocumento().equals(tmp.getDocumento()) && x.getDocumentoBis().equals(tmp.getDocumentoBis()));	
				} catch (NullPointerException e) {
					return false;
				}
			})
			.max(Comparator.comparingLong(y -> y.getId()))
			.orElse(new Antecedente()).getId()
		);
		log.debug("///////////\\\\\\\\\\\\//////////");
		log.debug(antecedente.toString());
		tipo = Constantes.ETipoFolio.fromLong(prelacionAnteCapHist.getTipoFolio().getId());
		
		switch (tipo) {
		case PREDIO:
			Predio predio = predioService.savePredio(prelacionAnteDTO.getPredioDTO());
			prelacionAnteCapHist.setPredio(predio);	
			prelacionAnteCapHistRepository.save(prelacionAnteCapHist);
			antecedenteRepository.save(antecedente);
			PredioAnte predioAnte = predio.getPredioAntesParaPredios().stream()
					.filter(x-> x.getAntecedente().getId().equals(antecedente.getId()))
					.findFirst().orElse(new PredioAnte());
				log.debug(predioAnte.toString());
			predioAnte.setAntecedente(antecedente);
			predioAnte.setPredio(predio);
			predioAnteRepository.save(predioAnte);
			id = predio.getId();
			break;
		case MUEBLE:
			Mueble 	mueble = muebleService.saveMueble(prelacionAnteDTO.getMueble());
			
			prelacionAnteCapHist.setMueble(mueble);	
			prelacionAnteCapHistRepository.save(prelacionAnteCapHist);
			antecedenteRepository.save(antecedente);
			MuebleAnte muebleAnte = mueble.getMuebleAntesParaMuebles().stream()
					.filter(x-> x.getAntecedente().getId().equals(antecedente.getId()))
					.findFirst().orElse(new MuebleAnte());
				log.debug(muebleAnte.toString());
			muebleAnte.setAntecedente(antecedente);
			muebleAnte.setMueble(mueble);
			muebleAnteRepository.save(muebleAnte);
			id= mueble.getId();
			break;
		case PERSONAS_JURIDICAS:
			PersonaJuridica personaJuridica = personaJuridicaService.savePersonaJuridica(prelacionAnteDTO.getPersonaJuridica());
				prelacionAnteCapHist.setPersonaJuridica(personaJuridica);	
				prelacionAnteCapHistRepository.save(prelacionAnteCapHist);
				antecedenteRepository.save(antecedente);
				PjAnte personaJuridicaAnte = personaJuridica.getPjAnteParaPersonasJuridicas().stream()
						.filter(x-> x.getAntecedente().getId().equals(antecedente.getId()))
						.findFirst().orElse(new PjAnte());
					log.debug(personaJuridicaAnte.toString());
				personaJuridicaAnte.setAntecedente(antecedente);
				personaJuridicaAnte.setPersonaJuridica(personaJuridica);
				pjAnteRepository.save(personaJuridicaAnte);
			id= personaJuridica.getId();
			break;
		case PERSONAS_AUXILIARES:
			FolioSeccionAuxiliar folioSeccionAuxiliar =  folioSeccionAuxiliarService.saveFolioSeccionAuxiliar(prelacionAnteDTO.getFolioSeccionAuxiliar());
				prelacionAnteCapHist.setFolioSeccionAuxiliar(folioSeccionAuxiliar);	
				prelacionAnteCapHistRepository.save(prelacionAnteCapHist);
				antecedenteRepository.save(antecedente);
				AuxiliarAnte folioSeccionAuxiliarAnte = folioSeccionAuxiliar.getAuxiliarAnteParaFolioSeccionAuxiliares().stream()
						.filter(x-> x.getAntecedente().getId().equals(antecedente.getId()))
						.findFirst().orElse(new AuxiliarAnte());
					log.debug(folioSeccionAuxiliarAnte.toString());
				folioSeccionAuxiliarAnte.setAntecedente(antecedente);
				folioSeccionAuxiliarAnte.setFolioSeccionAuxiliar(folioSeccionAuxiliar);
				auxiliarAnteRepository.save(folioSeccionAuxiliarAnte);
			id= folioSeccionAuxiliar.getId();
			break;
		default:
			throw new IllegalArgumentException("Tipo de Folio no soportado");
		}
		return id;
	}
	
	public Long updatePrelacionAnte(PrelacionAnte prelAnte) {
		
		prelacionAnteRepository.saveAndFlush(prelAnte);
		
		return prelAnte.getId();
	}
}
