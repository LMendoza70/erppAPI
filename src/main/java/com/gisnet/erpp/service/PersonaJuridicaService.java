package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.PjAnte;
import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.TipoFolio;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.PersonaJuridicaRepository;
import com.gisnet.erpp.repository.PjAnteRepository;
import com.gisnet.erpp.repository.PjPersonaRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.Constantes;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.PersonaJuridicaAnteVO;


@Service
public class PersonaJuridicaService {

	@Autowired
	PersonaJuridicaRepository personaJuridicaRepository;

	@Autowired
	PjAnteRepository pjAnteRepository;

	@Autowired
	PrelacionPredioRepository prelacionPredioRepository;

	@Autowired
	PrelacionService prelacionService;

	@Autowired TipoFolioRepository tipoFolioRepository;
	
	@Autowired
	OficinaRepository oficinaRepository;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	ActoPredioRepository actoPredioRepository;
	
	@Autowired
	PjPersonaRepository pjPersonaRepository;
	
	public PersonaJuridica findOneByNumFolio(Integer folio,Long oficinaId) {

		return personaJuridicaRepository.findByNoFolioAndOficinaId(folio, oficinaId);

	}
	
	public PersonaJuridica findByNumFolio(Integer folio) {
		return personaJuridicaRepository.findByNoFolio(folio);
	}
	
	public Optional<PersonaJuridica> findByAnteceneteId(Long antecedenteId) {
		return personaJuridicaRepository.findByPjAnteParaPersonasJuridicasAntecedenteId(antecedenteId);
	}

	public PjAnte findPjAnteByPersonaJuridica(PersonaJuridica pj) {
		return pjAnteRepository.findOneByPersonaJuridicaId(pj.getId());
	}

	public PjAnte findPjAnteByAntecedenteId( Long  antecedentetId) {
		return pjAnteRepository.findOneByAntecedenteId( antecedentetId);

	}

	public PrelacionPredio savePersonaJuridicaPrelacion (Long pjId, Prelacion prelacion) {

		PrelacionPredio pp = new PrelacionPredio();

		//Prelacion prelacion = prelacionService.findOne(prelacionId);
		PersonaJuridica personaJuridica = personaJuridicaRepository.findOne(pjId);
		TipoFolio tipoFolio = tipoFolioRepository.findOne((long)Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio());


		pp.prelacion(prelacion);
		pp.setPersonaJuridica(personaJuridica);
		pp.setTipoFolio(tipoFolio);

		pp.setVersion(1);
		pp.setEstatus(1);
		prelacionPredioRepository.saveAndFlush(pp);

		pp.setIdVersion(pp.getId().toString()+pp.getPrelacion().getId().toString());
		prelacionPredioRepository.saveAndFlush(pp);
		return pp;




	}

	public PersonaJuridicaAnteVO findPersonaJuridicaByFolio(Integer idFolio, Long oficinaId ){
		PersonaJuridicaAnteVO result = null;
		PersonaJuridica personaJuridica = personaJuridicaRepository.findByNoFolioAndOficinaId(idFolio, oficinaId);

		if(personaJuridica!=null){
			result=new PersonaJuridicaAnteVO();
			PjAnte pjAnte = pjAnteRepository.findOneByPersonaJuridicaId(personaJuridica.getId());
			if(pjAnte!=null) {
				result.setAntecedente(	AntecedenteVO.antecedenteTransform( pjAnte.getAntecedente()) );
			}
			
			result.setPersonaJuridica(personaJuridica);
		}


		return result;
	}

	
	public PersonaJuridica savePersonaJuridica(PersonaJuridica personaJuridica){
		PersonaJuridica oldPersonaJuridica = null;
		if(personaJuridica.getId() != null){
			oldPersonaJuridica = personaJuridicaRepository.findOne(personaJuridica.getId());
		}else{
			oldPersonaJuridica = new PersonaJuridica();
		}

		
		copyProperties(personaJuridica, oldPersonaJuridica);
		personaJuridicaRepository.save(oldPersonaJuridica);
		
		return oldPersonaJuridica;
	}
	
	private void copyProperties(PersonaJuridica from,PersonaJuridica to){
		to.setDenominacionNombre(from.getDenominacionNombre());
		to.setDuracion(from.getDuracion());
		to.setFechaApertura(from.getFechaApertura());
		to.setTipoSociedad(from.getTipoSociedad());
		to.setMunicipio(from.getMunicipio());
		to.setCaratulaActualizada(from.getCaratulaActualizada());
		to.setObjeto(from.getObjeto());
	}

	@Transactional
	public Long createFolioPj(Long idPj, Long oficinaId){
		String message = null;
		Long folio = null;		
		Optional<PersonaJuridica> pj = Optional.of(personaJuridicaRepository.getOne(idPj));
		if(pj.isPresent()){
			message = validacionCreacionFolio(pj.get());
			if(message == null){
				folio = personaJuridicaRepository.getFolioFromPjSequence();
				pj.get().setNoFolio(Integer.valueOf(folio.intValue()));
				pj.get().setOficina(oficinaRepository.findOne(oficinaId));
				pj.get().setCaratulaActualizada(true);
				pj.get().setBloqueado(false);
				personaJuridicaRepository.save(pj.get());
			}else{
				throw new RuntimeException(message);
			}
		}
		return folio;
	}
	
	private String validacionCreacionFolio(PersonaJuridica pj){
		String message = null;
		
		if(pj.getActoPrediosParaPersonasJuridicas() == null || pj.getActoPrediosParaPersonasJuridicas().isEmpty()){
			message = "La Persona Juridica no tiene actos capturados";
		}
		if(message == null){
			for(ActoPredio actoPredio:pj.getActoPrediosParaPersonasJuridicas()){
				if(actoPredio.getActoPredioCamposParaActoPredios() == null || actoPredio.getActoPredioCamposParaActoPredios().isEmpty()){
					message = "La Persona Juridica no tiene actos predio campos capturados";
					break;
				}
			}
			if(message ==null){
				for(ActoPredio actoPredio:pj.getActoPrediosParaPersonasJuridicas()){
					if(actoPredio.getActo().getActoDocumentosParaActos() == null || actoPredio.getActo().getActoDocumentosParaActos().isEmpty()){
						message = "La Persona Juridica tiene actos sin documentos";
						break;
					}
				}
			}
			if(message ==null){
				for(ActoPredio actoPredio:pj.getActoPrediosParaPersonasJuridicas()){
					if(actoPredio.getActo().getActoFirmasParaActos() == null || actoPredio.getActo().getActoFirmasParaActos().isEmpty()){
						message = "La Persona Juridica tiene actos sin firmar";
						break;
					}
				}
			}
		}
		return message;
	}
	
	public PersonaJuridica findPjByPrelacion(Long idPrelacion){
		PersonaJuridica pj = null;
		List<PrelacionPredio> lstPrelacionesPredio = prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(idPrelacion);
		if(lstPrelacionesPredio != null && !lstPrelacionesPredio.isEmpty()){
			pj = lstPrelacionesPredio.get(0).getPersonaJuridica();
		}
		return pj;
	}

	public PersonaJuridica findOne(Long id) {
		return personaJuridicaRepository.findOne(id);
	}

	@Transactional
	public Long createFolioPj(Long personaJuridicaId, Long oficinaId, Boolean validarCreacion) throws Exception{
		if (validarCreacion) {
			return this.createFolioPj(personaJuridicaId, oficinaId);
		}
		Optional<PersonaJuridica> personaJuridica = Optional.of(personaJuridicaRepository.getOne(personaJuridicaId));
		return saveFolio(personaJuridica.orElseThrow(() -> new Exception("No existe la persona Juridica")), oficinaId);
	}
	
	private Long saveFolio(PersonaJuridica personaJuridica, Long oficinaId) {
		personaJuridica.setNoFolio(personaJuridicaRepository.getFolioFromPjSequence().intValue());
		personaJuridica.setOficina(oficinaRepository.findOne(oficinaId));
		personaJuridica.setCaratulaActualizada(true);
		personaJuridica.setBloqueado(false);
		return personaJuridicaRepository.save(personaJuridica).getNoFolio().longValue();
	}

	public List<PersonaJuridica> findByDenominacion(Persona persona)
	{
		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		return personaJuridicaRepository.findByDenominacion(persona,usuario.getOficina().getId());
	}
	
	@Transactional (readOnly = true)
	public List<PjPersona> findSociosOrganosApoderadosActuales(Long pjId ){
		List<ActoPredio> actoPredios = actoPredioRepository.findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaId(pjId, Constantes.ETipoFolio.PERSONAS_JURIDICAS, Constantes.StatusActo.ACTIVO, Constantes.TipoEntrada.SALIDA  );
				
		List<Long> ids = new ArrayList<Long>();
		for(ActoPredio ap : actoPredios) {
			ids.add(ap.getId());
		}		
		List<PjPersona> predioPersonas = pjPersonaRepository.findAllByActoPredioIdIn(ids);
		return   this.filtraExtintos(predioPersonas);
	}
	
	
	public List<PjPersona> findSociosOrganosApoderadosActuales(Long pjId,Constantes.TipoRelPersona tipoRel){
		List<PjPersona> allPjPersonas =  findSociosOrganosApoderadosActuales(pjId);
		return allPjPersonas.stream().filter(x->x.getTipoRelPersona().getId().equals(tipoRel.getIdTipoRelPersona())).collect(Collectors.toList());
	}
	
		
	public int findTotalSociosOrganosApoderadoActuales(Long pjId,Constantes.TipoRelPersona tipoRel) {
		List<PjPersona> all =  findSociosOrganosApoderadosActuales(pjId);
		Long total =  all.stream().filter(x->x.getTipoRelPersona().getId().equals(tipoRel.getIdTipoRelPersona())).count();
		return	total.intValue();
	}
	
	
	public List<PjPersona> filtraExtintos(List<PjPersona> pjPersonas){
		return pjPersonas.stream().filter(x->x.getExtinto()==null || !x.getExtinto()).collect(Collectors.toList());
	}
	
	public PersonaJuridica findByNumeroFolioRealAndOficinaId(Integer folioAnterior,Long oficinaId) {
	 return personaJuridicaRepository.findByNumeroFolioRealAndOficinaId(folioAnterior, oficinaId);	
	}
}
