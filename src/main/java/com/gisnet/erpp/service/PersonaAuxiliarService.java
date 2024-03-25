package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.AuxiliarAnte;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.PjAnte;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.TipoFolio;
import com.gisnet.erpp.repository.AuxiliarAnteRepository;
import com.gisnet.erpp.repository.FolioSeccionAuxiliarRepository;
import com.gisnet.erpp.repository.PersonaAuxiliarRepository;
import com.gisnet.erpp.repository.PersonaJuridicaRepository;
import com.gisnet.erpp.repository.PjAnteRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonaAuxiliarService {

	@Autowired
	PersonaAuxiliarRepository personaAuxiliarRepository;
	
	@Autowired
	FolioSeccionAuxiliarRepository folioSeccionAuxiliarRepository;
	
	@Autowired
	AuxiliarAnteRepository auxiliarAnteRepository;
	
	
	@Autowired
	PrelacionPredioRepository prelacionPredioRepository;
	
	@Autowired TipoFolioRepository tipoFolioRepository;


	public FolioSeccionAuxiliar findOneByNoFolio(Integer numLibro) {
		return folioSeccionAuxiliarRepository.findOneByNoFolio(numLibro);
	}


	public AuxiliarAnte findAuxiliarAnteByPersonaAuxiliar(FolioSeccionAuxiliar pj) {
		return auxiliarAnteRepository.findOneByFolioSeccionAuxiliarId(pj.getId());
	}
	
	public AuxiliarAnte findAuxiliarAnteByAntecedenteId(long id) {
		return auxiliarAnteRepository.findOneByAntecedenteId( id );
	}
	
	
	
	public PrelacionPredio saveFolioSeccionAuxPrelacion (Long folSecAux, Prelacion prelacion) {
		
		PrelacionPredio pp = new PrelacionPredio();
		
		
		FolioSeccionAuxiliar folioSeccionAuxiliar = folioSeccionAuxiliarRepository.findOne(folSecAux);
		TipoFolio tipoFolio = tipoFolioRepository.findOne((long)Constantes.ETipoFolio.PERSONAS_AUXILIARES.getTipoFolio());

			
		pp.prelacion(prelacion);
		pp.setFolioSeccionAuxiliar(folioSeccionAuxiliar);
		pp.setTipoFolio(tipoFolio);

		pp.setVersion(1);
		pp.setEstatus(1);
		prelacionPredioRepository.saveAndFlush(pp);

		pp.setIdVersion(pp.getId().toString()+pp.getPrelacion().getId().toString());
		prelacionPredioRepository.saveAndFlush(pp);
		return pp;

	}
}
