package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoFolio;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.util.Constantes;

@Service
public class TipoFolioService {

	@Autowired
	private TipoFolioRepository tipoFolioRepository;
	
	public List<TipoFolio> findAll(){
		return tipoFolioRepository.findAllByOrderByNombreAsc();
	}
	
	public TipoFolio create(TipoFolio tipoFolio)
	{
		tipoFolioRepository.save(tipoFolio);
		return tipoFolio;
	}
	
	public List<TipoFolio> findTipoFolios(){
		List<Long> ids = new ArrayList<Long>();
		ids.add( Long.valueOf( Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio() ) );
		ids.add( Long.valueOf( Constantes.ETipoFolio.PREDIO.getTipoFolio() ) );
		return tipoFolioRepository.findByIdIn(ids);
	}
	
}
