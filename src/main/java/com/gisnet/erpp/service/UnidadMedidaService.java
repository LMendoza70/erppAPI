package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.UnidadMedida;
import com.gisnet.erpp.repository.UnidadMedidaRepository;

@Service
public class UnidadMedidaService {

	@Autowired
	private UnidadMedidaRepository unidadMedidaRepository;
	
	public List<UnidadMedida>findAll(){
		return unidadMedidaRepository.findAll();
	}
	
	public UnidadMedida create(UnidadMedida entidadUnidadMedida)
	{
		unidadMedidaRepository.save(entidadUnidadMedida);
		
		return entidadUnidadMedida;
	}
	
	public Long delete(UnidadMedida entidadUnidadMedida)
	{
		unidadMedidaRepository.delete(entidadUnidadMedida);
		
		return 0L;
	}
	
	public List<UnidadMedida>findFilter(){
		Integer[] idUsables = new Integer[] { 1,2,6 };
		return unidadMedidaRepository.findAllfilter(idUsables);
	}
	
}
