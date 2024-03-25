package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.DiaInhabil;
import com.gisnet.erpp.domain.InhabilLocal;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.repository.InhabilLocalRepository;

@Service
public class InhabilLocalService{
	@Autowired
	InhabilLocalRepository inhabilLocalRepository;

	public InhabilLocal findOne(Long id) {
		return inhabilLocalRepository.findOne(id);
}

	public List<InhabilLocal> findAll() {
			return inhabilLocalRepository.findAll();
	}
	
	public InhabilLocal create(InhabilLocal inhabilLocal)
	{
		inhabilLocalRepository.save(inhabilLocal);
		return inhabilLocal;
	}
	
	public Long delete(InhabilLocal inhabilLocal) {
		inhabilLocalRepository.delete(inhabilLocal);
		return 0l;
	}
	
	public InhabilLocal findAllByDiaInhabilAndOficina(DiaInhabil diaInhabil, Oficina oficina) {
		return inhabilLocalRepository.findOneByDiaInhabilAndOficina(diaInhabil, oficina);
	}
	
}