package com.gisnet.erpp.service.materializacion;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.RecursoInconformidad;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.CampoValoresRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.RecursoInconformidadRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.StatusRepository;
import com.gisnet.erpp.repository.TipoInconformidadRepository;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.util.Constantes;

@Service
public class MaterializacionRecursoInconformidadService {

	@Autowired
	PrelacionRepository prelacionRepository;

	@Autowired
	CampoValoresRepository campoValoresRepository;

	@Autowired
	RecursoInconformidadRepository recursoInconformidadRepository;

	@Autowired
	TipoInconformidadRepository tipoInconformidadRepository;

	@Autowired
	StatusActoRepository statusActoRepository;

	@Autowired
	ActoRepository actoRepository;

	@Autowired
	StatusRepository statusRepository;

	@Autowired
	PrelacionService prelacionService;
	
	@Autowired
	ActoUtilService actoUtilService;
	
	@Autowired
	ActoPredioRepository actoPredioRepository;

	private final long CONFIRMA_DENEGACION_CAMPO_ID = 5067L;
	private final long PRELACION_CAMPO_ID = 5066L;
	private final long FUNDAMENTO_CAMPO_ID = 5065L;
	private final long OPERA_SOBRE_CAMPO_ID = 543L;
	private final long HABILITA_PRELACION_CAMPO_ID = 261L;

	@Transactional
	public void deMaterializaRecursoInconformidad(Acto acto) {
		recursoInconformidadRepository.deleteByActoId(acto.getId());
		Optional<Bitacora> bitacora = prelacionService.findBitacoraByActoId(acto.getId());
		if (bitacora.isPresent()) {
			Prelacion prelacion = bitacora.get().getPrelacion();
			prelacion.setStatus(bitacora.get().getStatus());
			prelacionRepository.save(prelacion);
		}
		prelacionService.deleteBitacoraByActo(acto.getId());
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
	}

	@Transactional
	public void materializaRecursoInconformidad(Acto acto) {

		ActoPredio actoPredio = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto.getId()).get();

		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
		Boolean[] confirmaDenegacion = { false };
		HashMap<Integer, PrelacionVO> prelaciones = new HashMap<Integer, PrelacionVO>();
		String[] fundamento = { null };
		String[] tipoInconformidad = { null };

		actoPredioCampos.forEach((actoPredioCampo) -> {
			Long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
			int index = actoPredioCampo.getOrden();

			if (campoId == CONFIRMA_DENEGACION_CAMPO_ID) {
				confirmaDenegacion[0] = actoUtilService.getBooleanValor(actoPredioCampo);
			}

			if (campoId == PRELACION_CAMPO_ID || campoId == HABILITA_PRELACION_CAMPO_ID) {
				PrelacionVO prelacion = prelaciones.get(index);
				if (prelacion == null) {
					prelacion = new PrelacionVO();
					prelaciones.put(index,prelacion);
				}

				if (campoId == PRELACION_CAMPO_ID && actoPredioCampo.getValor() != null
						&& !actoPredioCampo.getValor().isEmpty()) {
					prelacion.setPrelacion(prelacionRepository.findOne(Long.parseLong(actoPredioCampo.getValor())));
				}
				if(campoId == HABILITA_PRELACION_CAMPO_ID) {
					prelacion.setSeleccionada(actoUtilService.getBooleanValor(actoPredioCampo));
				}
			}

			if (campoId == FUNDAMENTO_CAMPO_ID) {
				fundamento[0] = actoPredioCampo.getValor();
			}

			if (campoId == OPERA_SOBRE_CAMPO_ID && actoPredioCampo.getValor() != null
					&& !actoPredioCampo.getValor().isEmpty()) {
				tipoInconformidad[0] = campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()))
						.getNombre();
			}

		});

		//OBTIENE PRELACION
		Prelacion[] prel = {null};
		Prelacion prelacion = null;
		prelaciones.forEach((index,item)->{
			if(item.getSeleccionada())
				prel[0] = item.getPrelacion();
		});
		prelacion = prel[0];
	
		
		RecursoInconformidad recurso = new RecursoInconformidad();
		recurso.setActo(acto);
		recurso.setConfirmaDenegacion(confirmaDenegacion[0]);
		recurso.setPrelacion(prelacion);	
		recurso.setFundamento(fundamento[0]);
		recurso.setTipoInconformidad(tipoInconformidadRepository.findOneByNombre(tipoInconformidad[0]));
		recursoInconformidadRepository.save(recurso);

		if (recurso.getConfirmaDenegacion()) {
			acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
		} else {
			// HABILITA PRELACION
			if (prelacion != null) {
				prelacionService.createBitacora(prelacion, "HABILITADA POR " + acto.getTipoActo().getNombre() + " ENTRADA "
						+ acto.getPrelacion().getConsecutivo(), acto);
				prelacion.setStatus(statusRepository.findOne(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus()));
				prelacionRepository.save(prelacion);
			}
			acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		}

		actoRepository.save(acto);

	}

}
