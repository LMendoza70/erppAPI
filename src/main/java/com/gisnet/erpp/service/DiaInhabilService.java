package com.gisnet.erpp.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.DiaInhabil;
import com.gisnet.erpp.repository.DiaInhabilRepository;

@Service
public class DiaInhabilService{
	@Autowired
	DiaInhabilRepository diaInhabilRepository;

	public DiaInhabil findOne(Long id) {
		return diaInhabilRepository.findOne(id);
}

	public List<DiaInhabil> findAll() {
			return diaInhabilRepository.findAll();
	}
	
	public DiaInhabil create(DiaInhabil diaInhabil)
	{
		diaInhabilRepository.save(diaInhabil);
		return diaInhabil;
	}
	
	public Long delete(DiaInhabil diaInhabil) {
		diaInhabilRepository.delete(diaInhabil);
		return 0l;
	}
	
	public List<DiaInhabil> findAllByMesAndDia(Integer mes, Integer dia) {
		return diaInhabilRepository.findAllByMesAndDia(mes, dia);
	}
}