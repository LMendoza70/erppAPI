package com.gisnet.erpp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.repository.BitacoraRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.StatusRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;

@Service
public class AnalisisPorSistemaService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PrelacionService prelacionService;

	@Autowired
	CertificadoService certificadoService;
	
	@Autowired
	PrelacionRepository prelacionRepository;
	
	@Autowired
	BitacoraRepository bitacoraRepository;
	
	@Autowired
	StatusRepository statusRepository;
	
	@Autowired
	UsuarioService usuarioService;

    @Async
	public void procesar(Prelacion prelacion) {
		log.info("Iniciando Analisis por sistema para prelacion {}", prelacion.getId());

		prelacion = prelacionService.findOne(prelacion.getId());

		try {
			if (prelacion.getPrelacionServiciosParaPrelacions() != null && prelacion.getPrelacionServiciosParaPrelacions().size() == 1 && prelacion.getPrelacionServiciosParaPrelacions().iterator().next().getServicio().getId() == Constantes.SERVICIO_CERTIFICADO_LIBERTAD_GRAVAMEN_ID) {
				log.debug("La prelacion {} sera antendida por certificadoService.enviaCertificadoDeLivertadIGravamen", prelacion.getId());
				certificadoService.enviaCertificadoDeLivertadIGravamen(prelacion);
			} else {
				log.info("La prelacion {} no fue atendida por ningun servicio regresandolo al turnador ", prelacion.getId());

				prelacion.setNumPorSistema(prelacion.getNumPorSistema() + 1);
				prelacionService.saveAndUpdate(prelacion);
				prelacionService.enviarATurnador(prelacion);

			}
		} catch (Exception e) {
			log.info("ocurrio un error con la atencion por sistema de la prelacion {} regresandolo al turnador ", prelacion.getId());
			
			log.error(e.getMessage(), e);
			prelacion.setNumPorSistema(prelacion.getNumPorSistema() + 1);
			prelacionRepository.save(prelacion);
			bitacoraRepository.save(new Bitacora(UtilFecha.getCurrentDate(), e.getMessage(), prelacion, statusRepository.findOne(Constantes.Status.ERROR_SIENDO_ATENDIDO_POR_EL_SISTEMA.getIdStatus()), usuarioService.findOne(Constantes.USUARIO_ID_SISTEMA)));
			prelacionService.enviarATurnador(prelacion);

		}

	}

}
