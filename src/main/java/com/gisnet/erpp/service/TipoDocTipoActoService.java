package com.gisnet.erpp.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoDocTipoActo;
import com.gisnet.erpp.domain.TipoDocumento;
import com.gisnet.erpp.repository.TipoDocTipoActoRepository;
import com.gisnet.erpp.repository.TipoDocumentoRepository;

@Service
public class TipoDocTipoActoService {

	@Autowired
	TipoDocTipoActoRepository tipoDocTipoActoRepository;
	@Autowired
	TipoDocumentoRepository tipoDocumentoRepository;
	
	public List<TipoDocumento> findBytipoDocumento(Long id) {
		
		List<TipoDocTipoActo> items;
		List<TipoDocumento> lstTipoDocumento =  new ArrayList<TipoDocumento>();
		
		items = tipoDocTipoActoRepository.findBytipoActoId(id);
		for(TipoDocTipoActo item : items){
			//System.out.println(item.tipoDocumento(tipoDocumento));
			lstTipoDocumento.add(item.getTipoDocumento());
		}
		
		return lstTipoDocumento;
}
	
	public TipoDocumento findBytipoDocumentoId(Long id) {
		return tipoDocumentoRepository.findTipoDocumentoById(id);
	}
	
}
