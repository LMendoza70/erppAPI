package com.gisnet.erpp.service.materializacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPredioMonto;
import com.gisnet.erpp.domain.ActoRel;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRelRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.DireccionRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.PredioPersonaRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.TipoEntradaRepository;
import com.gisnet.erpp.repository.TipoRelPersonaRepository;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.PredioPersonaService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.util.Constantes;

@Service
public class MaterializacionActoTraslativoService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private long MODULO_ID_ADQUIRIENTES_PERMUTANTES = 767L;
	private long MODULO_ID_ADQUIRIENTES_PERMUTATARIOS = 768L;

	@Autowired
	PersonaRepository personaRepository;
	
	@Autowired
	StatusActoRepository statusActoRepository;
	
	@Autowired
	PredioPersonaService predioPersonaService;
	
	@Autowired
	ActoPredioRepository actoPredioRepository;
	
	@Autowired
	ActoRepository actoRepository;
	
	@Autowired
	TipoEntradaRepository tipoEntradaRepository;
	
	@Autowired
	PredioPersonaRepository predioPersonaRepository;
	
	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;
	
	@Autowired
	ActoRelRepository actoRelRepository;
	
	@Autowired
	TipoRelPersonaRepository tipoRelPersonaRepository;
	
	@Autowired
	DireccionRepository direccionRepository;
	
	@Autowired
	ActoService actoService;
	
	@Autowired
	ActoUtilService actoUtilService;
	
	@Autowired
	PrelacionService prelacionService;
	

	public List<ActoRel> materializaTraslativo(Acto acto, boolean commit) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		if (actoPredio.getPredio() == null) {
			throw new IllegalArgumentException("El acto no tiene un acto_predio");
		}

		log.debug("actoPredio= {}", actoPredio);

		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, PredioPersona> adquitientes = actoUtilService.parseAdquirientes(actoPredioCampos, acto, null);
		HashMap<Integer, PredioPersona> titulares = actoUtilService.parseTitulares(actoPredioCampos);
		
		return 	trasladar(actoPredio, adquitientes, titulares, commit);	
	}
	

	private List<ActoRel> trasladar(ActoPredio actoPredio, HashMap<Integer, PredioPersona> adquitientes, HashMap<Integer, PredioPersona> titulares, boolean commit) {
		Acto acto = actoPredio.getActo();
		
		boolean tieneSuperfice= false;

		for (Map.Entry<Integer, PredioPersona> entry : adquitientes.entrySet()) {
			PredioPersona pp = (PredioPersona) entry.getValue();
			if (pp.getSuperficie()!=null) {
				tieneSuperfice=true;
			}
		}
	    		
		double superficiePrevia = 0;
		if (tieneSuperfice) {
			List<PredioPersona> actuales = predioPersonaService.findPropietariosActuales(actoPredio.getPredio().getId(), false);
			for (Iterator iterator = actuales.iterator(); iterator.hasNext();) {
				PredioPersona pp = (PredioPersona) iterator.next();
				superficiePrevia+= pp.getSuperficie();
			}
			
		}
		
		//checar si se esta enajenando el porcentaje completo
		List<ActoPredio> actoPrediosTitulares = actoPredioRepository.findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaId(actoPredio.getPredio().getId(), Constantes.ETipoFolio.PREDIO, Constantes.StatusActo.ACTIVO, Constantes.TipoEntrada.SALIDA  );
		List<ActoRel> relaciones = new ArrayList<ActoRel>();
		
		boolean completo = false;
		//Si no vienen titulares, significa que no estan en la forma y que el traslado debe ser completo
		if (titulares == null || titulares.size()==0) {
			if (titulares == null) {
				titulares = new HashMap<Integer, PredioPersona>();
			}
			if (titulares.size()==0) {
				log.debug("No hay valores para titulares se trasladara todo el dominio completo");
				for (ActoPredio actoPredioTitular : actoPrediosTitulares) {
					for (PredioPersona oldPredioPersona : actoPredioTitular.getPredioPersonasParaActoPredios()) {
						int index = 1;
						PredioPersona predioPersona = new PredioPersona();
						predioPersona.setPersona(oldPredioPersona.getPersona());
						predioPersona.setPorcentajeDd(oldPredioPersona.getPorcentajeDd());
						predioPersona.setPorcentajeUv(oldPredioPersona.getPorcentajeUv());
						predioPersona.setSuperficie(oldPredioPersona.getSuperficie());
						titulares.put(index++, predioPersona);					
					}				
				}
			}
		}
		
		List<Acto> nuevosActos = new ArrayList<Acto>();
		for (ActoPredio actoPredioTitular : actoPrediosTitulares) {
			Acto actoTitular = actoPredioTitular.getActo();
			actoTitular.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
			if (commit) {
				actoRepository.save(actoTitular);
			}
					
			Acto newActo = new Acto();
			actoUtilService.copyActo(actoTitular, newActo);
			newActo.setCopiadoModificado(true);

			nuevosActos.add(newActo);
	

			ActoPredio newActoPredio = new ActoPredio();

			actoUtilService.copyActoPredio(actoPredioTitular, newActoPredio);
			newActoPredio.setActo(newActo);
			newActoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
				
			SortedSet<ActoPredio> nuevosActosPredios = new TreeSet<ActoPredio>();
			nuevosActosPredios.add(newActoPredio);
			newActo.setActoPredios(nuevosActosPredios);

			ActoRel actoRel = new ActoRel();
			actoRel.setActo(acto);
			actoRel.setActoAnt(actoTitular);
			actoRel.setActoSig(newActo);

			relaciones.add(actoRel);

			Set<PredioPersona> nuevosPredioPersona = new HashSet<PredioPersona>();
			newActoPredio.setPredioPersonasParaActoPredios(nuevosPredioPersona);

			for (PredioPersona oldPredioPersona : actoPredioTitular.getPredioPersonasParaActoPredios()) {
				PredioPersona newPredioPersona = new PredioPersona();
				actoUtilService.copyPredioPersona(oldPredioPersona, newPredioPersona);

				for (Map.Entry<Integer, PredioPersona> entry : titulares.entrySet()) {
					PredioPersona pp = entry.getValue();
					if (oldPredioPersona.getPersona().getId() == pp.getPersona().getId()) {
						if (pp.getPorcentajeDd()!=null) {
							newPredioPersona.setPorcentajeDd(oldPredioPersona.getPorcentajeDd() - pp.getPorcentajeDd());
						}
						if (pp.getPorcentajeUv()!=null) {
							newPredioPersona.setPorcentajeUv(oldPredioPersona.getPorcentajeUv() - pp.getPorcentajeUv());
						}
						if (pp.getSuperficie()!=null) {
							newPredioPersona.setSuperficie(oldPredioPersona.getSuperficie() - pp.getSuperficie());
						}
						
						newPredioPersona.setActoPredio(newActoPredio);
						nuevosPredioPersona.add(newPredioPersona);

					}
				}

			}						
		}

		// remover 0s
		for (ActoRel actoRel : relaciones) {
			Acto newActo = actoRel.getActoSig();
			if (newActo != null) {
				for (ActoPredio newActoPredio : newActo.getActoPrediosParaActos()) {
					for (Iterator<PredioPersona> iterator = newActoPredio.getPredioPersonasParaActoPredios().iterator(); iterator.hasNext(); ) {
						PredioPersona newPredioPersona = iterator.next();
						if ((newPredioPersona.getPorcentajeDd()==null || newPredioPersona.getPorcentajeDd() == 0) && (newPredioPersona.getPorcentajeUv()==null || newPredioPersona.getPorcentajeUv() == 0) && (newPredioPersona.getSuperficie()==null || newPredioPersona.getSuperficie() == 0)) {
							iterator.remove();
						}
					}
				}
			}
		}
		
		// remueve listas vacias despues de remover 0s
		for (ActoRel actoRel : relaciones) {
			Acto newActo = actoRel.getActoSig();
			if (newActo != null) {
				for (ActoPredio newActoPredio : newActo.getActoPrediosParaActos()) {
					if (newActoPredio.getPredioPersonasParaActoPredios().size() == 0) {
						newActo.getActoPrediosParaActos().remove(newActoPredio);
					}
				}
			}
		}

		for (ActoRel actoRel : relaciones) {
			Acto newActo = actoRel.getActoSig();
			if (newActo != null && newActo.getActoPrediosParaActos().size() == 0 ) {
				actoRel.setActoSig(null);
			}			
		}
		
		//insertar resultado
		if (commit) {
			for (ActoRel actoRel : relaciones) {
				Acto newActo = actoRel.getActoSig();
				if (newActo!=null) {
					actoRepository.save(newActo);
					
					Acto actoAnt = actoRel.getActoAnt();
					actoAnt.setRemplazado(true);
					actoRepository.save(actoAnt);
	
					for (ActoPredio newActoPredio : newActo.getActoPrediosParaActos()) {
						actoPredioRepository.save(newActoPredio);
						for (PredioPersona newPredioPersona : newActoPredio.getPredioPersonasParaActoPredios()) {
							log.debug("newPredioPersona={}", newPredioPersona);
							predioPersonaRepository.save(newPredioPersona);
						}
						
						if ( newActoPredio.getActoPredioCamposParaActoPredios() != null) {
							actoPredioCampoRepository.save(newActoPredio.getActoPredioCamposParaActoPredios());
						}
					}	
				}			
				actoRelRepository.save(actoRel);			
			}

		// crear nuevos registros para adquirientes
		acto.statusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		acto.procesado(true);
		actoRepository.save(acto);
		
		actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
		actoPredioRepository.save(actoPredio);
		}
		

		ActoRel actoRel = new ActoRel();
		actoRel.setActo(acto);
		actoRel.setActoAnt(null);
		actoRel.setActoSig(acto);
			
		if (commit) {
			actoRelRepository.save(actoRel);

			for (Map.Entry<Integer, PredioPersona> entry : adquitientes.entrySet()) {
				PredioPersona predioPersona = entry.getValue();
				
				log.debug("predioPersona.getPersona().getId() = {}", predioPersona.getPersona().getId());
				if (predioPersona.getPersona().getId() == null) {
					// crear persona si no se encuentra por curp o rfc si es persona moral
					log.debug("Buscando una persona tipo persona= {}, con curp={} o rfc={} ", predioPersona.getPersona().getTipoPersona().getId(), predioPersona.getPersona().getCurp(), predioPersona.getPersona().getRfc());
		
					Optional<Persona> persona2 = null;
					if (predioPersona.getPersona().getTipoPersona().getId().intValue()==Constantes.ETipoPersona.FISICA.getAsInteger().intValue() && predioPersona.getPersona().getCurp()!=null && predioPersona.getPersona().getCurp().length()>=18){
						persona2 = personaRepository.findByCurp(predioPersona.getPersona().getCurp());
					} else if (predioPersona.getPersona().getTipoPersona().getId().intValue()==Constantes.ETipoPersona.MORAL.getAsInteger().intValue() && predioPersona.getPersona().getRfc()!=null && predioPersona.getPersona().getRfc().length()>=12) {
						persona2 = personaRepository.findByCurp(predioPersona.getPersona().getRfc());
					}
					
					if (persona2!= null && persona2.isPresent()) {
						predioPersona.setPersona(persona2.get());
					} else {
						predioPersona.getPersona().setActivo(true);
						personaRepository.save(predioPersona.getPersona());
					}
					predioPersona.setDireccion(direccionRepository.findOne(1L));
					
				}
	
				predioPersona.setTipoRelPersona(tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.PROPIETARIO.getIdTipoRelPersona()));				
				predioPersona.setActoPredio(actoPredio);
				predioPersonaRepository.save(predioPersona);
			}
			
		}
		
		relaciones.add(actoRel);
		Set<ActoPredio> actoPredios = new HashSet<ActoPredio>();
		actoPredios.add(actoPredio);
		actoPredio.setPredioPersonasParaActoPredios(new HashSet<PredioPersona>(adquitientes.values()));
		acto.setActoPrediosParaActos(actoPredios);
		
		return relaciones;

	}

	
	@Transactional
	public void deMaterializarTraslativo(Acto acto) {		
		log.debug("Iniciando dematerializacion del acto = {}", acto);
	
		List<ActoRel> relaciones = actoRelRepository.findByActoId(acto.getId());
		
		for (ActoRel actoRel : relaciones) {
			Acto actoAnt = actoRel.getActoAnt();
			Acto actoSig = actoRel.getActoSig();
			
			if (actoAnt != null) {
				log.debug("cambiando a status activo el acto = {}", actoAnt);
				actoAnt.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
				actoAnt.setRemplazado(null);
				actoRepository.save(actoAnt);
			} else {
				actoSig.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
				actoRepository.save(actoSig);
				List<ActoPredio> salidas = actoPredioRepository.findAllByActoIdAndTipoEntrada(actoSig.getId(), Constantes.TipoEntrada.SALIDA);
				for (ActoPredio actoPredioSalida : salidas) {
					actoPredioSalida.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
					predioPersonaRepository.deleteByActoPredioId(actoPredioSalida.getId());
					actoPredioRepository.save(actoPredioSalida);
				}
				
			}
			
			// si actoAnt==null indica que no tiene acto anterior por lo tanto solo se actualiza el status y el tipo de entrada en el if anterior
			if (actoSig != null && actoAnt != null) {
				actoService.delete(actoSig.getId());
			}
			
			actoRelRepository.delete(actoRel);
			
		}		
	}
	
	@Transactional
	public void materializaPermuta(Long prelacionId, List<ActoPredio> actos) {
		log.debug("materializa permuta de {} ", prelacionId);
		
		Prelacion prelacion = prelacionService.findOne(prelacionId);
		
		// obtener campos dinamicos
		Set<ActoPredioCampo> actoPredioCampos = actos.get(0).getActoPredioCamposParaActoPredios();

		String[] permutaValor = actoUtilService.parsePrediosPermuta(actoPredioCampos).split("\\|");
		String permuta = permutaValor[0];
		String permutatario = permutaValor[1];
			
		HashMap<Integer, PredioPersona> adquitientes1 = actoUtilService.parseAdquirientes(actoPredioCampos, actos.get(0).getActo(), MODULO_ID_ADQUIRIENTES_PERMUTATARIOS);
		HashMap<Integer, PredioPersona> adquitientes2 = actoUtilService.parseAdquirientes(actoPredioCampos, actos.get(0).getActo(), MODULO_ID_ADQUIRIENTES_PERMUTANTES);
		
		for (ActoPredio actoPredio: actos) {
			Predio predio = actoPredio.getPredio();
			
			log.debug(" predio = {}", predio);
			
			if (permuta.indexOf(String.valueOf(predio.getId())) > -1) {
				this.trasladar(actoPredio, adquitientes2, null, true);
			} else {
				this.trasladar(actoPredio, adquitientes1, null, true);
			}
		
		}
	}
	

}
