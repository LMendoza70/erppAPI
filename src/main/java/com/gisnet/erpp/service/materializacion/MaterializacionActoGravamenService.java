package com.gisnet.erpp.service.materializacion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
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
import com.gisnet.erpp.repository.TipoMonedaRepository;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.util.Constantes;

@Service
public class MaterializacionActoGravamenService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	TipoEntradaRepository tipoEntradaRepository;
	
	@Autowired
	TipoMonedaRepository tipoMonedaRepository;
	
	@Autowired
	StatusActoRepository statusActoRepository;
	
	@Autowired
	ActoService actoService;
	
	@Autowired
	ActoPredioMontoRepository actoPredioMontoRepository;
	
	@Autowired
	ActoRepository actoRepository;
	
	@Transactional
	public void materializarGravamenes(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		log.debug("predioActo= {}", actoPredio);

		Set<ActoPredioCampo> actoPrediocampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, ActoPredioMonto> actoPredioMontos = new HashMap<Integer, ActoPredioMonto>();

		actoPrediocampos.forEach((actoPrediocampo) -> {
			long campoId = actoPrediocampo.getModuloCampo().getCampo().getId();
			log.debug("modulo_campo_id = {}, campoid={} , valor={}", actoPrediocampo.getModuloCampo().getId(), campoId, actoPrediocampo.getValor());
			int index = actoPrediocampo.getOrden();

			if (campoId == ActoUtilService.MONTO_DEL_CREDITO_CAMPO_ID || campoId == ActoUtilService.TIPO_DE_MONEDA_CAMPO_ID) {
				ActoPredioMonto actoPredioMonto = actoPredioMontos.get(index);
				if (actoPredioMonto == null) {
					actoPredioMonto = new ActoPredioMonto();
					actoPredioMonto.setPorcentaje(100f);
					actoPredioMontos.put(index, actoPredioMonto);
				}

				if (campoId == ActoUtilService.MONTO_DEL_CREDITO_CAMPO_ID && actoPrediocampo.getValor()!=null) {
					actoPredioMonto.setMonto(new BigDecimal(actoPrediocampo.getValor()));
				}

				if (campoId == ActoUtilService.TIPO_DE_MONEDA_CAMPO_ID) {
					actoPredioMonto.setTipoMoneda(tipoMonedaRepository.findOne(Long.parseLong(actoPrediocampo.getValor())));
				}

			}

		});

		log.debug("total de actos en gravamen={}", actoPredioMontos.size());
		actoPredioMontos.forEach((index, actoPredioMonto) -> {
			log.debug("acto= {}, monto={}, porcentaje={}, tipo_moneda={}", actoPredioMonto.getActo(), actoPredioMonto.getMonto(), actoPredioMonto.getPorcentaje(), actoPredioMonto.getTipoMoneda() != null ? actoPredioMonto.getTipoMoneda().getNombre() : "null");
		});

		// cambiar a vigente acto de cancelacion
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		acto.setFechaRegistro(new Date());
		actoRepository.save(acto);
		
		for (Map.Entry<Integer, ActoPredioMonto> entry : actoPredioMontos.entrySet()) {
			ActoPredioMonto apm = entry.getValue();
			apm.setActoPredio(actoPredio);
			actoPredioMontoRepository.save(apm);
		}

	}
	
	@Transactional
	public void deMaterializarGravamenes(Acto acto) {
		log.debug("Iniciando dematerializacion de acto Gravamen = {}", acto);
		
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoRepository.save(acto);
		
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
		
		actoPredioMontoRepository.deleteByActoPredio(actoPredio);
	}


}
