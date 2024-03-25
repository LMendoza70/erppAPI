package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.TipoCampo;
import com.gisnet.erpp.repository.TipoCampoRepository;

@Service
public class TipoCampoService{
	@Autowired
	TipoCampoRepository tipoCampoRepository;

	public TipoCampo findOne(Long id) {
		return tipoCampoRepository.findOne(id);
	}

	public List<TipoCampo> findAll() {
			return tipoCampoRepository.findAll();
	}
}
