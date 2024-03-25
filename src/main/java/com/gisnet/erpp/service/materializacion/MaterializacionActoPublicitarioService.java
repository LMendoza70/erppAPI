package com.gisnet.erpp.service.materializacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPredioMonto;
import com.gisnet.erpp.domain.ActoPublicitario;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoPublicitarioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.service.MaterializacionActoService;
import com.gisnet.erpp.service.PredioService;
import com.gisnet.erpp.util.Constantes;

@Service
public class MaterializacionActoPublicitarioService {
	
	@Autowired
	MaterializacionActoModificatorioService materializacionActoModificatorioService;
	
	@Autowired
	ActoPublicitarioRepository actoPublicitarioRepository; 
	
	@Autowired
	PredioService predioService;
	
	@Autowired
	MaterializacionActoService materializacionActoService;
	
	@Autowired
	ActoPredioRepository actoPredioRepository;
	
	@Autowired
	ActoUtilService actoUtilService;
	
	@Autowired
	StatusActoRepository statusActoRepository;
	
	@Autowired
	ActoRepository actoRepository;
	
	
	public void materializarActoPublicitario(Acto acto){
		System.out.println("***materializando---ActoPublicitario**\n\n");
		String nombreSecuenciaConsecutivo= null;
		Optional<ActoPublicitario> apActo = actoPublicitarioRepository.findFirstByActoId(acto.getId()); 
		if(!apActo.isPresent() && acto.getTipoActo().getId()!=76) {
			Oficina ofi = acto.getPrelacion().getOficina();
			nombreSecuenciaConsecutivo = "acto_publicitario_numero";
			if(ofi.getNumOficina()!=null || !ofi.getNumOficina().isEmpty()){
				nombreSecuenciaConsecutivo= nombreSecuenciaConsecutivo+"_"+ofi.getNumOficina();
			}
			nombreSecuenciaConsecutivo= nombreSecuenciaConsecutivo+"_seq";
			System.out.println("***Secuencia---ActoPublicitario:"+nombreSecuenciaConsecutivo);	
			Long n=actoPublicitarioRepository.getNumeroActoPublicitario(nombreSecuenciaConsecutivo);
			ActoPublicitario ap= new ActoPublicitario();
			ap.setActo(acto);
			ap.setNumero(n.intValue());
			ap.setOficina(ofi);
			actoPublicitarioRepository.save(ap);
			System.out.println("--"+n);
		}
		
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
		Boolean[]  afectaFolio =  {false};
		List<String> folios = new ArrayList<String>();
		 
		actoPredioCampos.forEach((actoPredioCampo)->{
			Long campoId =  actoPredioCampo.getModuloCampo().getCampo().getId();
			
			if (campoId ==  Constantes.AFECTA_FOLIO_CAMPO_ID) {
				afectaFolio[0] = actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
						? "true".equals(actoPredioCampo.getValor().toLowerCase())
				  			   				: false;
				}
			   
			if (campoId == Constantes.FOLIO_AFECTADO_CAMPO_ID && actoPredioCampo.getValor() != null
					&& !actoPredioCampo.getValor().isEmpty()) {
				   folios.add(actoPredioCampo.getValor());
			   }
		});
		
		//SI AFECTA FOLIOS CLONA EL ACTO PUBLICITARIO
		if (afectaFolio[0] && folios != null && folios.size() > 0) {
			folios.forEach(folio->{
				Predio predio =  predioService.findPredioByNoFolio(Integer.parseInt(folio));
				if(predio==null) {
					throw new IllegalArgumentException("EL FOLIO "+ folio +" NO EXISTE");
				}else if(!predio.getOficina().getId().equals(acto.getPrelacion().getOficina().getId())){
					throw new IllegalArgumentException("EL FOLIO "+ folio +" NO PERTENCE A LA OFICINA");
				}else {
					HashMap<Integer, ActoPorcentajeVO> actoClonar =  new HashMap<Integer, ActoPorcentajeVO>();
					ActoPorcentajeVO target = new ActoPorcentajeVO();
					target.actoPredioMonto = new ActoPredioMonto();
					target.actoPredioMonto.setActo(acto);
					target.actoSeleccionado=true;
					actoClonar.put(0,target);
					materializacionActoModificatorioService.saveActosSeleccionados(actoClonar, acto.getPrelacion(),
							predio, acto);
					
				}
			});
		}
		
	}
	
	
	public void deMaterializaActosPublicitarios(Acto acto) {
		materializacionActoService.deHeredarActos(acto);
	}
	
	
	public void materializaCancelacion(Acto acto) {
		Optional<ActoPredio> oActoPredio = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto.getId());
		if(!oActoPredio.isPresent())
			throw new IllegalArgumentException("El acto no tiene un acto_predio");
		Set<ActoPredioCampo> actoPredioCampos = oActoPredio.get().getActoPredioCamposParaActoPredios();
		HashMap<Integer, ActoPorcentajeVO> actosSeleccionados = actoUtilService.parseActos(actoPredioCampos);
		actosSeleccionados.forEach((index,x)->{
			if(x.actoSeleccionado) {
				Acto _acto = x.actoPredioMonto.getActo();
				_acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
				actoRepository.save(_acto);
			}
		});
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		actoRepository.save(acto);
	}
	
	
	public void deMaterializaCancelacion(Acto acto) {
		Optional<ActoPredio> oActoPredio = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto.getId());
		if(!oActoPredio.isPresent())
			throw new IllegalArgumentException("El acto no tiene un acto_predio");
		Set<ActoPredioCampo> actoPredioCampos = oActoPredio.get().getActoPredioCamposParaActoPredios();
		HashMap<Integer, ActoPorcentajeVO> actosSeleccionados = actoUtilService.parseActos(actoPredioCampos);
		actosSeleccionados.forEach((index,x)->{
			if(x.actoSeleccionado) {
				Acto _acto = x.actoPredioMonto.getActo();
				_acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
				actoRepository.save(_acto);
			}
		});
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoRepository.save(acto);
	}
}
