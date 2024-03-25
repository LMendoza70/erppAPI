package com.gisnet.erpp.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.repository.ColindanciaRepository;

@Service
public class ColindanciaService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
		
	@Autowired
	ActoService actoService;
	
	@Autowired
	ColindanciaRepository colindanciaRepository;

	@Transactional (readOnly = true)
	public Set<Colindancia> findColindanciasActuales(long predioId){
		List<Acto> actoTraslativo = actoService.getActosVigentesTraslativosParaPredio(predioId);
		
		ActoPredio actoPredio = null;
		if( actoTraslativo.size() > 0 ) {
			actoPredio = actoTraslativo.get(actoTraslativo.size()-1).getActoPrediosParaActos().iterator().next();
		}
		
		if( actoPredio != null ) {	
			return actoPredio.getPredio().getColindanciasParaPredios();
		} else {
			return null;
		}
	}
	
	public Set<Colindancia> findColindancias(Long predioId){
		return  colindanciaRepository.findByPredioIdOrderByIdAsc(predioId);
	}

}
