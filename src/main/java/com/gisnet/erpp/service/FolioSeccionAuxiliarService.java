package com.gisnet.erpp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.AuxiliarAnte;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.TipoFolio;
import com.gisnet.erpp.repository.AuxiliarAnteRepository;
import com.gisnet.erpp.repository.FolioSeccionAuxiliarRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.FolioSeccionAuxiliarAnteVO;



@Service
public class FolioSeccionAuxiliarService {

	@Autowired
	private FolioSeccionAuxiliarRepository folioSeccionAuxiliarRepository;

	@Autowired
	private PrelacionPredioRepository prelacionPredioRepository;

	@Autowired
	private TipoFolioRepository tipoFolioRepository;
	
	@Autowired
	private OficinaRepository oficinaRepository; 
	

  @Autowired
  private AuxiliarAnteRepository auxiliarAnteRepository;

	public FolioSeccionAuxiliarAnteVO findFolioSeccionAuxiliarByFolio(Integer idFolio, long oficinaId){
		FolioSeccionAuxiliarAnteVO result = null;
		FolioSeccionAuxiliar folioSeccionAuxiliar = folioSeccionAuxiliarRepository.findOneByNoFolioAndOficinaId(idFolio,oficinaId);

		if(folioSeccionAuxiliar!=null){
			result=new FolioSeccionAuxiliarAnteVO();
			AuxiliarAnte auxiliarAnte = auxiliarAnteRepository.findOneByFolioSeccionAuxiliarId(folioSeccionAuxiliar.getId());

			result.setAntecedente(	AntecedenteVO.antecedenteTransform( auxiliarAnte.getAntecedente()) );
			result.setFolioSeccionAuxiliar(folioSeccionAuxiliar);
		}


		return result;
	}
	
	public FolioSeccionAuxiliar findFolioSeccionAuxByAntecedente(Long idAntecedente){
		return folioSeccionAuxiliarRepository.findByAuxiliarAnteParaFolioSeccionAuxiliaresAntecedenteId(idAntecedente);
	}
	
	public FolioSeccionAuxiliar saveFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSecAux){
		FolioSeccionAuxiliar oldFolioSeccionAuxiliar = null;
		if(folioSecAux.getId() != null){
			oldFolioSeccionAuxiliar = folioSeccionAuxiliarRepository.findOne(folioSecAux.getId());
		}else{
			oldFolioSeccionAuxiliar = new FolioSeccionAuxiliar();
		}
		
		copyProperties(folioSecAux, oldFolioSeccionAuxiliar);
		folioSeccionAuxiliarRepository.save(oldFolioSeccionAuxiliar);
		
		return oldFolioSeccionAuxiliar;
	}
	
	private void copyProperties(FolioSeccionAuxiliar from,FolioSeccionAuxiliar to){
		to.setFechaApertura(from.getFechaApertura());
		to.setCaratulaActualizada(from.getCaratulaActualizada());
	}
	
	@Transactional
	public Long createFolioSeccionAux(Long idSeccionAuxiliar, Long oficinaId){
		String message = null;
		Long folio = null;		
		Optional<FolioSeccionAuxiliar> folioSecAux = Optional.of(folioSeccionAuxiliarRepository.getOne(idSeccionAuxiliar));
		if(folioSecAux.isPresent()){
			message = validacionCreacionFolio(folioSecAux.get());
			if(message == null){
				folio = folioSeccionAuxiliarRepository.getFolioFromSecAuxSequence();
				folioSecAux.get().setNoFolio(Integer.valueOf(folio.intValue()));
				folioSecAux.get().setOficina(oficinaRepository.findOne(oficinaId));
				folioSeccionAuxiliarRepository.save(folioSecAux.get());
			}else{
				throw new RuntimeException(message);
			}
		}
		return folio;
	}
	
	private String validacionCreacionFolio(FolioSeccionAuxiliar folioSecAux){
		String message = null;
		
		if(folioSecAux.getActoPrediosParaFolioSeccionAuxiliares() == null || folioSecAux.getActoPrediosParaFolioSeccionAuxiliares().isEmpty()){
			message = "El folio seccion auxiliar no tiene actos capturados";
		}
		if(message == null){
			for(ActoPredio actoPredio:folioSecAux.getActoPrediosParaFolioSeccionAuxiliares()){
				if(actoPredio.getActoPredioCamposParaActoPredios() == null || actoPredio.getActoPredioCamposParaActoPredios().isEmpty()){
					message = "El folio seccion auxiliar no tiene actos predio campos capturados";
					break;
				}
			}
			if(message ==null){
				for(ActoPredio actoPredio:folioSecAux.getActoPrediosParaFolioSeccionAuxiliares()){
					if(actoPredio.getActo().getActoDocumentosParaActos() == null || actoPredio.getActo().getActoDocumentosParaActos().isEmpty()){
						message = "El folio seccion auxiliar tiene actos sin documentos";
						break;
					}
				}
			}
			if(message ==null){
				for(ActoPredio actoPredio:folioSecAux.getActoPrediosParaFolioSeccionAuxiliares()){
					if(actoPredio.getActo().getActoFirmasParaActos() == null || actoPredio.getActo().getActoFirmasParaActos().isEmpty()){
						message = "El folio seccion auxiliar tiene actos sin firmar";
						break;
					}
				}
			}
		}
		return message;
	}
	
	public FolioSeccionAuxiliar findFolioSeccionAuxByPrelacion(Long idPrelacion){
		FolioSeccionAuxiliar folioSeccionAux = null;
		List<PrelacionPredio> lstPrelacionesPredio = prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(idPrelacion);
		if(lstPrelacionesPredio != null && !lstPrelacionesPredio.isEmpty()){
			folioSeccionAux = lstPrelacionesPredio.get(0).getFolioSeccionAuxiliar();
		}
		return folioSeccionAux;
	}

	@Transactional
	public Long createFolioSeccionAux(Long seccionAuxiliarId, Long oficinaId, Boolean validarCreacion) throws Exception {
		if (validarCreacion) {
			return this.createFolioSeccionAux(seccionAuxiliarId, oficinaId);
		}
		Optional<FolioSeccionAuxiliar> folioSeccionAuxiliar = Optional.of(folioSeccionAuxiliarRepository.getOne(seccionAuxiliarId));
		return saveFolio(folioSeccionAuxiliar.orElseThrow(() -> new Exception("No existe el Folio Auxiliar")), oficinaId);
	}

	private Long saveFolio(FolioSeccionAuxiliar folioSeccionAuxiliar, Long oficinaId) {
		folioSeccionAuxiliar.setNoFolio(folioSeccionAuxiliarRepository.getFolioFromSecAuxSequence().intValue());
		folioSeccionAuxiliar.setOficina(oficinaRepository.findOne(oficinaId));
		return folioSeccionAuxiliarRepository.save(folioSeccionAuxiliar).getNoFolio().longValue();
	}

}
