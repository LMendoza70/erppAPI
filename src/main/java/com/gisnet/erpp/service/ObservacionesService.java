package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.Observacion;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.repository.AreaRepository;
import com.gisnet.erpp.repository.ObservacionRepository;
import com.gisnet.erpp.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class ObservacionesService {
	@Autowired
	ObservacionRepository observacionRepository;

	@Autowired
	AreaRepository areaRepository;

	public Observacion findOne(Long id) {
		return observacionRepository.findOne(id);
	}

	public List<Observacion> findAll() {
			return observacionRepository.findAll();
	}

	
	public Set<Observacion> findAllByArea (Long areaId) {

		final Area area = areaRepository.findOne(areaId);

		return observacionRepository.findAllByArea(area);
	}
}