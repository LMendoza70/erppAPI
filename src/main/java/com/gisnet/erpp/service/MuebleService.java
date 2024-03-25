package com.gisnet.erpp.service;


import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.MuebleAnte;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.TipoFolio;
import com.gisnet.erpp.repository.MuebleAnteRepository;
import com.gisnet.erpp.repository.MuebleRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.MuebleAnteVO;

@Service
public class MuebleService {

	@Autowired
	MuebleRepository muebleRepository;

	@Autowired
	MuebleAnteRepository muebleAnteRepository;

	@Autowired
	PrelacionPredioRepository prelacionPredioRepository;


	@Autowired
	TipoFolioRepository tipoFolioRepository;
	
	@Autowired
	OficinaRepository oficinaRepository;


	public Mueble findMuebleByNoFolio(int numFolio) {
		return muebleRepository.findByNoFolio (numFolio);
	}

	public MuebleAnte findMuebleAnteByMuebleId(long id) {
		return muebleAnteRepository.findMuebleAnteByMuebleId(id);
	}

	public MuebleAnte findMuebleAnteByAntecedenteId(long antecedenetId) {
		return muebleAnteRepository.findMuebleAnteByAntecedenteId(antecedenetId);
	}

	public PrelacionPredio saveMueblePrelacion (Long muebleID, Prelacion prelacion) {

		PrelacionPredio pp = new PrelacionPredio();


		Mueble mueble = muebleRepository.findOne(muebleID);
		TipoFolio tipoFolio = tipoFolioRepository.findOne((long)Constantes.ETipoFolio.MUEBLE.getTipoFolio());


		pp.prelacion(prelacion);
		pp.setMueble(mueble);

		pp.setVersion(1);
		pp.setEstatus(1);
		prelacionPredioRepository.saveAndFlush(pp);

		pp.setTipoFolio(tipoFolio);


		pp.setIdVersion(pp.getId().toString()+pp.getPrelacion().getId().toString());
		prelacionPredioRepository.saveAndFlush(pp);
		return pp;
	}

	public MuebleAnteVO findMuebleByFolio(Integer idFolio){
		MuebleAnteVO result = null;
		Mueble mueble = muebleRepository.findByNoFolio(idFolio);

		if(mueble!=null){
			result=new MuebleAnteVO();
			MuebleAnte muebleAnte = muebleAnteRepository.findMuebleAnteByMuebleId(mueble.getId());

			result.setAntecedente(	AntecedenteVO.antecedenteTransform( muebleAnte.getAntecedente()) );
			result.setMueble(mueble);
		}


		return result;
	}
	
	public Mueble findMuebleByAntecedente(Long idAntecedente){
		return muebleRepository.findByMuebleParaMueblesAntecedenteId(idAntecedente);
	}
	
	public Mueble saveMueble(Mueble mueble){
		
		Mueble oldMueble = null;
		if(mueble.getId() != null){
			oldMueble = muebleRepository.findOne(mueble.getId());
		}else{
			oldMueble = new Mueble();
		}
		
		copyProperties(mueble, oldMueble);
		
		muebleRepository.save(oldMueble);

		return oldMueble;
	}
	
	private void copyProperties(Mueble from,Mueble to){
		to.setObjeto(from.getObjeto());
		to.setEspecificacion(from.getEspecificacion());
		to.setMarca(from.getMarca());
		to.setModel(from.getModelo());
		to.setNumSerie(from.getNumSerie());
		to.setTipoDocIdentif(from.getTipoDocIdentif());
		to.setFechaDocIdentif(from.getFechaDocIdentif());
		to.setCaratulaActualizada(from.getCaratulaActualizada());
		to.setNumDocIdentif(from.getNumDocIdentif());
	}
	
	@Transactional
	public Long createFolioMueble(Long idMueble, Long oficinaId){
		String message = null;
		Long folio = null;		
		Optional<Mueble> mueble = Optional.of(muebleRepository.getOne(idMueble));
		if(mueble.isPresent()){
			message = validacionCreacionFolio(mueble.get());
			if(message == null){
				folio = muebleRepository.getFolioFromMuebleSequence();
				mueble.get().setNoFolio(Integer.valueOf(folio.intValue()));
				mueble.get().setOficina(oficinaRepository.findOne(oficinaId));
				muebleRepository.save(mueble.get());
			}else{
				throw new RuntimeException(message);
			}
		}
		return folio;
	}
	
	private String validacionCreacionFolio(Mueble mueble){
		String message = null;
		
		if(mueble.getActoPrediosParaMuebles() == null || mueble.getActoPrediosParaMuebles().isEmpty()){
			message = "El mueble no tiene actos capturados";
		}
		if(message == null){
			for(ActoPredio actoPredio:mueble.getActoPrediosParaMuebles()){
				if(actoPredio.getActoPredioCamposParaActoPredios() == null || actoPredio.getActoPredioCamposParaActoPredios().isEmpty()){
					message = "El mueble no tiene actos predios campos capturados";
					break;
				}
			}
			if(message ==null){
				for(ActoPredio actoPredio:mueble.getActoPrediosParaMuebles()){
					if(actoPredio.getActo().getActoDocumentosParaActos() == null || actoPredio.getActo().getActoDocumentosParaActos().isEmpty()){
						message = "El mueble tiene actos sin documentos";
						break;
					}
				}
			}
			if(message ==null){
				for(ActoPredio actoPredio:mueble.getActoPrediosParaMuebles()){
					if(actoPredio.getActo().getActoFirmasParaActos() == null || actoPredio.getActo().getActoFirmasParaActos().isEmpty()){
						message = "El mueble tiene actos sin firmar";
						break;
					}
				}
			}
		}
		return message;
	}
	
	public Mueble findMuebleByPrelacion(Long idPrelacion){
		Mueble mueble = null;
		List<PrelacionPredio> lstPrelacionesPredio = prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(idPrelacion);
		if(lstPrelacionesPredio != null && !lstPrelacionesPredio.isEmpty()){
			mueble = lstPrelacionesPredio.get(0).getMueble();
		}
		return mueble;
	}

	@Transactional
	public Long createFolioMueble(Long muebleId, Long oficinaId, Boolean validarCreacion) throws Exception{
		if (validarCreacion) {
			return this.createFolioMueble(muebleId, oficinaId);
		}
		Optional<Mueble> mueble = Optional.of(muebleRepository.getOne(muebleId));
		return saveFolio(mueble.orElseThrow(() -> new Exception("No existe el Mueble")), oficinaId);
	}
	
	private Long saveFolio(Mueble mueble, Long oficinaId) {
		mueble.setNoFolio(muebleRepository.getFolioFromMuebleSequence().intValue());
		mueble.setOficina(oficinaRepository.findOne(oficinaId));
		return muebleRepository.save(mueble).getNoFolio().longValue();
	}
	
}
