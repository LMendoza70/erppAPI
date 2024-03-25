package com.gisnet.erpp.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoIden;
import com.gisnet.erpp.repository.TipoIdenRepository;

@Service
public class TipoIdenService {
	@Autowired
	TipoIdenRepository tipoIdenRepository;
	
	public List<TipoIden> findAll() {
		return tipoIdenRepository.findAll();
	}
	
	public TipoIden create(TipoIden tipoIden)
    {
		tipoIdenRepository.save(tipoIden);
		return tipoIden;
    }
	
	public Long delete(TipoIden tipoIden)
	{
		tipoIdenRepository.delete(tipoIden);
		return 0l;
	}
	
}