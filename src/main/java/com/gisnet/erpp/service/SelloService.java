package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Sello;
import com.gisnet.erpp.repository.LibroRepository;
import com.gisnet.erpp.repository.SelloRepository;
import com.gisnet.erpp.util.CommonUtil;
import com.gisnet.erpp.web.api.foliosrdig.LibroDTO;

@Service
public class SelloService {

	@Autowired
	private SelloRepository selloRepository;
	
	@Autowired
	private LibroRepository libroRepository;
	
	public List<Sello> findByLibro(LibroDTO libroDTO){
		Libro consultaLibro = libroRepository.findOne(libroDTO.getLibroId());
		List<Sello> timbres = null;		
		if(consultaLibro != null){
			timbres = selloRepository.findBylibroIdAndDocumento(consultaLibro.getId(),CommonUtil.generarBisDocumento(libroDTO.getDocumento(),libroDTO.getDocumentoBis()));
		}
		return timbres;
	}
}
