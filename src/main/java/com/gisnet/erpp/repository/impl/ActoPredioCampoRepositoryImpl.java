package com.gisnet.erpp.repository.impl;



import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.repository.ActoPredioCampoRepositoryCustom;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;

@Transactional(readOnly = true)
public class ActoPredioCampoRepositoryImpl implements ActoPredioCampoRepositoryCustom {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    ActoPredioCampoRepositoryImpl() {
       // super(ActoPredioCampo.class);
    }
    
    @Autowired 
    private ActoPredioCampoRepository actoPredioCampoRepository;

	public Map<Long, Set<ActoPredioCampo>> getActoPredioCampoValuesByActoPredioIdModuloId(Long actoPredioId, Long moduloId) {

		Set<ActoPredioCampo> actoPredioCampos = actoPredioCampoRepository.findActosPredioCamposByCustomQuery(actoPredioId, moduloId);
		Map<Long, Set<ActoPredioCampo>> result = new HashMap<Long, Set<ActoPredioCampo>>();
		Set<ActoPredioCampo> temporalSet =  new HashSet<ActoPredioCampo>();
		Long key =0L;
		if (actoPredioCampos.size() <1){
			return result;
		}


		for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
			key = Long.parseLong(actoPredioCampo.getOrden().toString());
			if(result.containsKey(key)){
				temporalSet = result.get(key);
				temporalSet.add(actoPredioCampo);
				result.replace(key, temporalSet);
			}
			else {
				temporalSet = new HashSet<ActoPredioCampo>();
				temporalSet.add(actoPredioCampo);
				result.put(key, temporalSet);
			}
		}
		return result;
	}
}