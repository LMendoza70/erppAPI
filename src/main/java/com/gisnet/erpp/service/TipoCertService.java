package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoCert;
import com.gisnet.erpp.repository.TipoCertRepository;

@Service
public class TipoCertService{
	@Autowired
	TipoCertRepository tipoCertRepository;

	public TipoCert findOne(Long id) {
		return tipoCertRepository.findOne(id);
}

	public List<TipoCert> findAll() {
			return tipoCertRepository.findAll();
	}
	
	public TipoCert create(TipoCert tipoCert)
	{
		tipoCertRepository.save(tipoCert);
		return tipoCert;
	}
	public Long delete(TipoCert tipoCert)
	{
		tipoCertRepository.delete(tipoCert);
		return 0l;
	}
	
}
