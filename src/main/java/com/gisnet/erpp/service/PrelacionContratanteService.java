package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PrelacionContratante;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.repository.PrelacionContratanteRepository;
import com.gisnet.erpp.vo.PrelacionContratanteVO;

@Service
public class PrelacionContratanteService {

	@Autowired
	private PrelacionContratanteRepository repo;

	@Autowired
	private ActoPredioService actoPredioService;

	public PrelacionContratante guardarCampoDeContratante(ActoPredioCampo campo) {
		PrelacionContratante contratante = obtenerPrelacionContratante(campo);

		switch(Math.toIntExact(campo.getModuloCampo().getId())) {
			case 537:
			case 752:
			case 882:
			case 925:
				contratante.setNombre(campo.getValor());
				break;
			case 538:
			case 753:
			case 883:
			case 926:
				contratante.setPaterno(campo.getValor());
				break;
			case 539:
			case 754:
			case 884:
			case 927:
				contratante.setMaterno(campo.getValor());
				break;
			case 549:
			case 763:
			case 892:
				contratante.setDd(campo.getValor());
				break;
			case 550:
			case 764:
			case 893:
				contratante.setUv(campo.getValor());
				break;
		}

		return repo.save(contratante);
	}

	private PrelacionContratante obtenerPrelacionContratante(ActoPredioCampo campo) {
		ActoPredio actoPredio = actoPredioService.getAP(campo.getActoPredio().getId());
		return obtenerPrelacionContratante(actoPredio, campo);
	}

	private PrelacionContratante obtenerPrelacionContratante(ActoPredio actoPredio, ActoPredioCampo campo) {
		Acto acto = actoPredio.getActo();
		PrelacionContratante contratante = repo.findByActoAndOrden(acto, campo.getOrden());

		if (contratante == null) {
			contratante = new PrelacionContratante();
			contratante.setActo(acto);
			contratante.setPrelacion(acto.getPrelacion());
			contratante.setTipoActo(acto.getTipoActo());
			contratante.setOrden(campo.getOrden());
		}

		contratante.setPredio(actoPredio.getPredio());

		return contratante;
	}
	
	@Transactional(readOnly = true)
	public Page<PrelacionContratanteVO> findAllByNombre(String paterno, String materno, String nombre, Pageable pageable) {
		return repo.findAllByNombre(paterno, materno, nombre, pageable);
	}

}
