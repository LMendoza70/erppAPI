package com.gisnet.erpp.service.materializacion;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPredioMonto;
import com.gisnet.erpp.repository.ActoPredioMontoRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.TipoEntradaRepository;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.util.Constantes;

@Service
public class MaterializacionActoCancelacionService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	TipoEntradaRepository tipoEntradaRepository;
	
	@Autowired
	StatusActoRepository statusActoRepository;
	
	@Autowired
	ActoService actoService;
	
	@Autowired
	ActoRepository actoRepository;
	
	@Autowired
	ActoPredioMontoRepository actoPredioMontoRepository;
	
	@Autowired
	ActoUtilService actoUtilService;
	
	@Transactional	
	public void materializarCancelacion(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		log.debug("predioActo= {}", actoPredio);

		Set<ActoPredioCampo> actoPrediocampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, ActoPorcentajeVO> actoPorcentajes = actoUtilService.parseActos(actoPrediocampos);
				
		log.debug("total de actos en siendo cancelados={}", actoPorcentajes.size());
		
		for(Iterator<Map.Entry<Integer, ActoPorcentajeVO>> it = actoPorcentajes.entrySet().iterator(); it.hasNext(); ) {
			ActoPorcentajeVO actoPorcentaje = it.next().getValue();
			if (actoPorcentaje.actoPredioMonto.getPorcentaje()==null) {
				actoPorcentaje.actoPredioMonto.setPorcentaje(100f);
			}		
			log.debug("seleccionado= {}, acto= {}, monto{}, porcentaje{}", actoPorcentaje.actoSeleccionado, actoPorcentaje.actoPredioMonto.getActo(), actoPorcentaje.actoPredioMonto.getMonto(), actoPorcentaje.actoPredioMonto.getPorcentaje());
			if (actoPorcentaje.actoPredioMonto.getPorcentaje() == 0) {
				it.remove();
			}
		}
		

		HashMap<Integer, TotalesVO> totales = new HashMap<Integer, TotalesVO>();

		// calcular totales en cancelaciones anteriores + nuevos para checar si se cancela al 100%

		for (Map.Entry<Integer, ActoPorcentajeVO> entry : actoPorcentajes.entrySet()) {
			log.debug("procesando acto gravamen {}", entry.getValue().actoPredioMonto.getActo());
			List<Acto> actosCancelacionesAnteriores = actoService.getActosCancelacionesVigentesParaActo(entry.getValue().actoPredioMonto.getActo().getId());
			
			for (Acto a : actosCancelacionesAnteriores) {
				ActoPredio ap = a.getActoPrediosParaActosOrderedByVersion().first();
				ap.getActoPredioMontosParaActoPredios().forEach((actoPredioMonto) -> {
					if (actoPredioMonto.getActo().getId() == entry.getValue().actoPredioMonto.getActo().getId()) {
						TotalesVO total = totales.get(entry.getKey());
						if (total == null) {
							total = new TotalesVO();
							totales.put(entry.getKey(), total);
						}
						total.totalPorcentaje += actoPredioMonto.getPorcentaje();
					}
				});
			}

			TotalesVO total = totales.get(entry.getKey());
			if (total == null) {
				total = new TotalesVO();
				totales.put(entry.getKey(), total);
			}
			total.totalPorcentaje += entry.getValue().actoPredioMonto.getPorcentaje();
		}

		// Si es igual al 100% o el monto es igual entonces el acto debe ser NO VIGENTE
		for (Map.Entry<Integer, ActoPorcentajeVO> entry : actoPorcentajes.entrySet()) {
			TotalesVO total = totales.get(entry.getKey());

			log.debug("totales para el acto {}, porcentaje{}", entry.getValue().actoPredioMonto.getActo().getId(), total.totalPorcentaje);

			Acto actoSiendoCancelado = entry.getValue().actoPredioMonto.getActo();

			if (actoSiendoCancelado.getActoPrediosParaActos() == null && actoSiendoCancelado.getActoPrediosParaActos().size() == 0) {
				throw new IllegalArgumentException("El acto siendo cancelado no tiene un acto_predio");
			}
			
			if (total == null || total.totalPorcentaje > 100) {
				throw new IllegalArgumentException("La cancelacion supera mas del 100%");
			}

			if (total != null && (total.totalPorcentaje == 100)) {
				// ya se completo el acto siendo cancelado
				actoSiendoCancelado.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
				actoRepository.save(actoSiendoCancelado);
			}
		}

		// cambiar a vigente acto de cancelacion
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		acto.setFechaRegistro(new Date());
		actoRepository.save(acto);

		for (Map.Entry<Integer, ActoPorcentajeVO> entry : actoPorcentajes.entrySet()) {
			ActoPredioMonto apm = entry.getValue().actoPredioMonto;
			apm.setActoPredio(actoPredio);
			actoPredioMontoRepository.save(apm);
		}

	}
	
	
	@Transactional
	public void deMaterializarCancelacion(Acto acto) {
		//regresar gravamen a ACTIVO
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
		
		for (ActoPredioMonto actoPredioMonto : actoPredio.getActoPredioMontosParaActoPredios()) {
			Acto actoGravamen = actoPredioMonto.getActo();
			actoGravamen.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
			actoRepository.save(actoGravamen);
		}
		
		
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoPredioMontoRepository.deleteByActoPredioActoId(acto.getId());
		actoRepository.save(acto);
		
	}

}
