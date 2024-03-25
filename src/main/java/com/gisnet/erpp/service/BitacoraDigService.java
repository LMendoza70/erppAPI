package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.BitacoraDig;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.repository.BitacoraDigRepository;
import com.gisnet.erpp.repository.LibroRepository;
import com.gisnet.erpp.util.CommonUtil;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.web.api.foliosrdig.LibroDTO;

@Service
public class BitacoraDigService {

	@Autowired
	private BitacoraDigRepository bitacoraDigRepository;
	
	@Autowired
	private LibroRepository libroRepository;
	
	public List<BitacoraDig> findByLibroAndDocumento(LibroDTO libroDTO){
		List<BitacoraDig> reg = null;
		List<String> documentos = new ArrayList<>();
		Libro consultaLibro = libroRepository.findOne(libroDTO.getLibroId());
		
		if(consultaLibro != null){
			documentos.add(CommonUtil.generarBisDocumento(libroDTO.getDocumento(),libroDTO.getDocumentoBis()));
			documentos.add(Constantes.DOC_APERTURA);
			documentos.add(Constantes.DOC_CIERRE);
			reg =  bitacoraDigRepository.findByLibroIdAndDocumentoIn(consultaLibro.getId(),documentos);
		}
		return reg;
	}
}
