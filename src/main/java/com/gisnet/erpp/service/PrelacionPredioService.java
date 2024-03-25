package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.vo.prelacion.AntecedenteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.web.api.prelacionPredio.PrelacionPredioDTO;
import com.gisnet.erpp.domain.PrelacionAnteCapHist;
import com.gisnet.erpp.repository.PrelacionAnteCapHistRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrelacionPredioService {
    @Autowired
    PrelacionPredioRepository prelacionPredioRepository;
    
    @Autowired
    PrelacionAnteCapHistRepository prelacionAnteCapHistRepository;

    @Autowired
	AntecedenteService antecedenteService;

	@Autowired
	PredioService predioService;
    
    @Autowired
	PersonaJuridicaService personaJuridicaService;
    
    @Autowired
	MuebleService muebleService;
    
    @Autowired
	FolioSeccionAuxiliarService folioSeccionAuxiliarService;

	public PrelacionPredio save(PrelacionPredio prelacionPredio) throws Exception {
		this.setNewFolio(prelacionPredio);
		prelacionPredio = prelacionPredioRepository.saveAndFlush(prelacionPredio);
		prelacionPredio.setIdVersion(prelacionPredio.getId().toString()+prelacionPredio.getPrelacion().getId().toString());
		prelacionPredio = prelacionPredioRepository.saveAndFlush(prelacionPredio);
    	return prelacionPredio;
    }

    public void setPrelacionPredioStatus (Long prelacionId, Long predioId, Boolean status) throws Exception {

        if (status == null)
            throw new Exception("Status no válido");

        PrelacionPredio pre= prelacionPredioRepository.findOneByPrelacionIdAndPredioId(prelacionId, predioId);
        if (pre == null)
			throw new Exception("No se encontró predio asignado a la entrada");
		

		

        pre.setValido(status); 

        prelacionPredioRepository.save(pre);

        return;


	}
	


	public void setPrelacionPredioStatusByTipoFolio (Long prelacionId,Long tipoFolio ,Long predioId, Boolean status) throws Exception {

        if (status == null)
            throw new Exception("Status no válido");

		Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromLong(tipoFolio);

        PrelacionPredio pre= null;
       
		
		switch (tipo) {
			case PREDIO:
			 	pre= prelacionPredioRepository.findOneByPrelacionIdAndPredioId(prelacionId, predioId);
			break;
			case MUEBLE:
				pre=  prelacionPredioRepository.findByPrelacionIdAndMuebleId( prelacionId,  predioId);
			
				break;
			case PERSONAS_JURIDICAS:
				pre=  prelacionPredioRepository.findByPrelacionIdAndPersonaJuridicaId( prelacionId,  predioId);
				break;
			case PERSONAS_AUXILIARES:
				pre=  prelacionPredioRepository.findByPrelacionIdAndFolioSeccionAuxiliarId( prelacionId,  predioId);
				break;
			default:
				throw new IllegalArgumentException("Tipo de Folio no soportado");
		}
		
		if (pre == null)
			throw new Exception("No se encontró predio asignado a la entrada");

        pre.setValido(status); 

        prelacionPredioRepository.save(pre);

        return;


    }
    
	@Transactional(readOnly = true)
	public PrelacionPredioDTO findPrelacionPredioDTO(Long id) {
		PrelacionPredioDTO prelacionPredioDTO = new PrelacionPredioDTO();
		PrelacionPredio prelacionPredio = prelacionPredioRepository.findOne(id);
		
		Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromLong(prelacionPredio.getTipoFolio().getId());
		
		switch (tipo) {
		case PREDIO:
			if (prelacionPredio.getPredio().getPredioAntesParaPredios()!=null && prelacionPredio.getPredio().getPredioAntesParaPredios().size()>0){
				prelacionPredioDTO.setAntecedente(prelacionPredio.getPredio().getPredioAntesParaPredios().iterator().next().getAntecedente());
			}			
			break;
		case MUEBLE:
			if (prelacionPredio.getMueble().getMuebleAntesParaMuebles()!=null && prelacionPredio.getMueble().getMuebleAntesParaMuebles().size()>0){
				prelacionPredioDTO.setAntecedente(prelacionPredio.getMueble().getMuebleAntesParaMuebles().iterator().next().getAntecedente());
			}
			break;
		case PERSONAS_JURIDICAS:
			if (prelacionPredio.getPersonaJuridica().getPjAnteParaPersonasJuridicas()!=null && prelacionPredio.getPersonaJuridica().getPjAnteParaPersonasJuridicas().size()>0){
				prelacionPredioDTO.setAntecedente(prelacionPredio.getPersonaJuridica().getPjAnteParaPersonasJuridicas().iterator().next().getAntecedente());
			}
			break;
		case PERSONAS_AUXILIARES:
			if (prelacionPredio.getFolioSeccionAuxiliar().getAuxiliarAnteParaFolioSeccionAuxiliares()!=null && prelacionPredio.getFolioSeccionAuxiliar().getAuxiliarAnteParaFolioSeccionAuxiliares().size()>0){
				prelacionPredioDTO.setAntecedente(prelacionPredio.getFolioSeccionAuxiliar().getAuxiliarAnteParaFolioSeccionAuxiliares().iterator().next().getAntecedente());
			}
			break;
		default:
			throw new IllegalArgumentException("Tipo de Folio no soportado");
		}
		
		prelacionPredioDTO.setPrelacionPredio(prelacionPredio);
		
		return prelacionPredioDTO;
	}


	public Antecedente findAntecedentesByPrelacionPredio(PrelacionPredio pp, Constantes.ETipoFolio tipo) {

			return antecedenteService.getAntecedenteByTipo(pp, tipo);

	}

	public List <PrelacionPredio> findAllPrelacionPredioFromPrelacion (Prelacion prelacion){
    	return prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacion.getId());
	}

	private void setNewFolio(PrelacionPredio prelacionPredio) throws Exception, RuntimeException {
    	String switchString = "";
    	try{
    		switchString = prelacionPredio.getPrelacion().getArea().getTipoFolio().getNombre();
    		if (switchString ==  null || switchString.isEmpty()) {
        		throw new Exception("La prelacion debe contener un tipo de folio");
        	}
    	}catch(Exception e) {
    		throw new Exception(e.getMessage());
    	}
		switch(switchString) {
		case "JURIDICO":
			this.personaJuridicaService.createFolioPj(prelacionPredio.getPersonaJuridica().getId(), prelacionPredio.getPrelacion().getOficina().getId(), false);
		break;
		
		case "AUXILIAR":
			this.folioSeccionAuxiliarService.createFolioSeccionAux(prelacionPredio.getFolioSeccionAuxiliar().getId(), prelacionPredio.getPrelacion().getOficina().getId(), false);
    	break;
    	
		case "MOBILIARIO":
			this.muebleService.createFolioMueble(prelacionPredio.getMueble().getId(), prelacionPredio.getPrelacion().getOficina().getId(), false);
		break;
		
		case "INMOBILIARIO":
			this.predioService.createFolioPredio(prelacionPredio.getPredio().getId(), prelacionPredio.getPrelacion().getOficina().getId(), false);
        break;
        
        default:
        	throw new Exception("La prelación contiene un tipo de folio no válido");
		}
	}
	
	@Transactional(readOnly = true)
	public PrelacionPredioDTO findPrelacionPredioDTOByPredio(Long id,Long idTF) {
		PrelacionPredioDTO prelacionPredioDTO = new PrelacionPredioDTO();
		List<PrelacionPredio> prelacionPredios = prelacionPredioRepository.findPrelacionPredioByPredio(id,idTF);
		PrelacionAnteCapHist prelacionAnteCapHist = prelacionAnteCapHistRepository.findByPredioIdAndTipoFolioId(id,idTF);
		//validar mas de 1 o null
		System.out.println("prelacionPredios{}"+prelacionPredios);
		PrelacionPredio prelacionPredio = null;
		if(prelacionAnteCapHist!=null && prelacionPredios.size()==0) {			
			prelacionPredio = new PrelacionPredio();
			prelacionPredio.setTipoFolio(prelacionAnteCapHist.getTipoFolio());
			prelacionPredio.setPredio(prelacionAnteCapHist.getPredio());
		} else {
			prelacionPredio=prelacionPredios.get(0);
		}
		
		Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromLong(prelacionPredio.getTipoFolio().getId());
		
		switch (tipo) {
		case PREDIO:
			if (prelacionPredio.getPredio().getPredioAntesParaPredios()!=null && prelacionPredio.getPredio().getPredioAntesParaPredios().size()>0){
				prelacionPredioDTO.setAntecedente(prelacionPredio.getPredio().getPredioAntesParaPredios().iterator().next().getAntecedente());
			}			
			break;
		case MUEBLE:
			if (prelacionPredio.getMueble().getMuebleAntesParaMuebles()!=null && prelacionPredio.getMueble().getMuebleAntesParaMuebles().size()>0){
				prelacionPredioDTO.setAntecedente(prelacionPredio.getMueble().getMuebleAntesParaMuebles().iterator().next().getAntecedente());
			}
			break;
		case PERSONAS_JURIDICAS:
			if (prelacionPredio.getPersonaJuridica().getPjAnteParaPersonasJuridicas()!=null && prelacionPredio.getPersonaJuridica().getPjAnteParaPersonasJuridicas().size()>0){
				prelacionPredioDTO.setAntecedente(prelacionPredio.getPersonaJuridica().getPjAnteParaPersonasJuridicas().iterator().next().getAntecedente());
			}
			break;
		case PERSONAS_AUXILIARES:
			if (prelacionPredio.getFolioSeccionAuxiliar().getAuxiliarAnteParaFolioSeccionAuxiliares()!=null && prelacionPredio.getFolioSeccionAuxiliar().getAuxiliarAnteParaFolioSeccionAuxiliares().size()>0){
				prelacionPredioDTO.setAntecedente(prelacionPredio.getFolioSeccionAuxiliar().getAuxiliarAnteParaFolioSeccionAuxiliares().iterator().next().getAntecedente());
			}
			break;
		default:
			throw new IllegalArgumentException("Tipo de Folio no soportado");
		}
		
		prelacionPredioDTO.setPrelacionPredio(prelacionPredio);
		
		return prelacionPredioDTO;
	}
	
	public List<Prelacion> findPrelacionesEnProcesoByPredio(Long predioId,Long prelacionId){
		return this.prelacionPredioRepository.findPrelacionEnProcesoByPredioId(predioId, prelacionId);
	}


}
