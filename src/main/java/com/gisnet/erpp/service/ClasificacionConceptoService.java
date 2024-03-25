package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.ClasificacionConcepto;
import com.gisnet.erpp.domain.ServicioClasifConcepto;
import com.gisnet.erpp.repository.ClasificacionConceptoRepository;
import com.gisnet.erpp.repository.ServicioClasifConceptoRepository;

@Service
public class ClasificacionConceptoService {

	@Autowired
	private ClasificacionConceptoRepository clasificacionConceptoRepository;
	
	public  List<ClasificacionConcepto> findByServiciosCotizadorId(Long servicioCotizadorId){
		return clasificacionConceptoRepository.findByServiciosClasificacionesConceptoServiciosCotizadorIdOrderByNombre(servicioCotizadorId);
	}
}
