package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.repository.StatusRepository;


@Service
public class StatusService{
	@Autowired
	StatusRepository statusRepository;

	public Status findOne(Long id) {
		return statusRepository.findOne(id);
	}

	public List<Status> findAll() {
			return statusRepository.findAll();
	}

	
	public List<Status> findAllUsables() {
		Integer[] idUsables = new Integer[] { 1, 2, 3, 4, 6, 14 };
		return statusRepository.findAllUsable(idUsables);
		//return null;
	}
	
	public List<Status> findAllUsablesCoordinador() {
		Integer[] idUsables = new Integer[] { 1,2,3,4,6,7,8,9,10,11,16,17,22,23,24,25 };
		return statusRepository.findAllUsable(idUsables);
	}
	
	public List<Status> findAllUpkeepPrelacion() {
		Integer[] idUsables = new Integer[] { 1,3,4,6,7,8,10,16,17,20,21,999 };
		return statusRepository.findAllUsable(idUsables);
	}
}