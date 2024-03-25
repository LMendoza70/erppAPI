package com.gisnet.erpp.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoColin;
import com.gisnet.erpp.repository.TipoColinRepository;

@Service
public class TipoColinService {

	@Autowired
	TipoColinRepository tipoColinRepository;
	
	public TipoColin findOne(Long id) {
		return tipoColinRepository.findOne(id);
}

	public List<TipoColin> findAll() {
			return tipoColinRepository.findAll();
	}
	
	public TipoColin create(TipoColin tipoColin)
	{
		tipoColinRepository.save(tipoColin);
		return tipoColin;
	}
	
	public Long delete(TipoColin tipoColin) {
		tipoColinRepository.save(tipoColin);
		return 0l;
	}
	
}
