package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.AreaClasifActo;
import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.Escritorio;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.PrelacionServicio;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.TurnoElectronico;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioOfiAreas;
import com.gisnet.erpp.repository.BitacoraRepository;
import com.gisnet.erpp.repository.DiaInhabilRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.TurnoElectronicoRepository;
import com.gisnet.erpp.repository.UsuarioActosRepository;
import com.gisnet.erpp.repository.UsuarioClasifActoServicioRepository;
import com.gisnet.erpp.repository.UsuarioOfiAreasRepository;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.repository.UsuarioServicioRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;

@Service
public class TurnadorService {

	private static final Logger log = LoggerFactory.getLogger(TurnadorService.class);

	@Autowired
	private EscritorioService escritorioService;
	
	@Autowired
	private PrelacionRepository prelacionRepository;
	
	@Autowired
	private UsuarioOfiAreasRepository usuarioOfiAreasRepository;
	
	@Autowired
	private UsuarioClasifActoServicioRepository usuarioClasifActoServicioRepository;
	
	@Autowired
	private UsuarioActosRepository  usuarioActosRepository;
	
	@Autowired
	private UsuarioServicioRepository usuarioServicioRepository;
	
	@Autowired
	private BitacoraRepository bitacoraRepository;
	
	@Autowired
	private TurnoElectronicoRepository turnoElectronicoRepository;
	
	@Autowired
	private DiaInhabilRepository diaInhabilRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EnvironmentService environmentService;
	
	@Autowired
	private AnalisisPorSistemaService analisisPorSistemaService;

 	@Async
	public void asignaPrelacionAnalista(Prelacion prelacion){
		log.debug("Turnando la prelacion con id : "+prelacion.getId());
		Prelacion prelacionConsulta = prelacionRepository.getOne(prelacion.getId());
		Constantes.Status status = Constantes.Status.getStatus(prelacionConsulta.getStatus().getId());
		
		
		// *IF* PARA USUARIO NOTARIO CON TIPOS DE ACTO 207,209 PARA QUE PUEDA DIRIGIR EL TURNADO A LA OFICINA SELECCIONADA
		//EN EL COMBO DE PORTAL NOTARIAL SECCION CERTIFICACIONES
		if(prelacionConsulta.getOficina().getId()!=prelacion.getOficina().getId()){
			prelacionConsulta.setOficina(prelacion.getOficina());
		}

		switch (status) {
		case ASIGNADOR_A_VALIDADOR_DE_PREDIOS:
			asignaAAanlista(prelacionConsulta);
			break;
		default:
			//if(!canAnalizadoPorSistema(prelacionConsulta)){
				//IHM Modificar				
				//if(isCYVF(prelacionConsulta)){
				//	asignarACyvf(prelacionConsulta);
				//} else {
					asignaAAanlista(prelacionConsulta);
				//}
			//} else {
				//enviarAAnalisadoPorSistema(prelacion);
			//}
		}
		
	}

	
	private void asignaAAanlista(Prelacion prelacionConsulta ){
		List<UsuarioOfiAreas> lstUsuariosOfi = null;
		List<Usuario> lstUsuarios = null;
		List<Usuario> lstUsuariosFinal = null;
		Escritorio escAsignado = null;
		Integer ponderacionAsumar = null;
		Status statusPrelacion = null;
		int numDiasEstimados = 0;
		Long idAreaActo = null;
		
		Date fechaActual = UtilFecha.getCurrentDate();
		
		log.debug("a Analista Se crean escritorios para usuarios con fecha [{}] y oficina [{}]",fechaActual,prelacionConsulta.getId());
		escritorioService.createEscritoriosByOficina(prelacionConsulta.getOficina().getId(), UtilFecha.getStartOfDay(fechaActual),Constantes.Rol.REGISTRADOR.getIdRol());
		
		//Se valida que sean actos
		if(prelacionConsulta.getActosParaPrelacions() != null && !prelacionConsulta.getActosParaPrelacions().isEmpty()){
			log.debug("Prelacion con actos ....");
			final List<Long> lstClasificacionActos = getClasificacionActos(prelacionConsulta.getActosParaPrelacions());
			log.debug("Numero de clasificacion de actos : {}",lstClasificacionActos.size());
			final List<Long> lstTiposActos = getTiposActos(prelacionConsulta.getActosParaPrelacions());
			log.debug("Numero de tipos de acto : {}",lstTiposActos.size());
			lstUsuariosOfi = usuarioOfiAreasRepository.findByOficinaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(prelacionConsulta.getOficina().getId(), Boolean.TRUE, Constantes.Rol.REGISTRADOR.getIdRol());
			log.debug("Usuarios que cumplen elprimer filtro : {}",lstUsuariosOfi.size());
			lstUsuarios = getUsuarios(lstUsuariosOfi);
			log.debug("Usuarios sin duplicados : {}",lstUsuarios.size());
			lstUsuariosFinal = lstUsuarios.stream().filter(usuario ->{
				return (usuarioClasifActoServicioRepository.countByUsuarioIdAndTurnarTodosAndClasifActoIdIn(usuario.getId(), Constantes.TURNAR_TODOS, lstClasificacionActos).intValue() == lstClasificacionActos.size()
						|| usuarioActosRepository.countByUsuarioIdAndIndTurnadoAndTipoActoIdIn(usuario.getId(), Boolean.TRUE, lstTiposActos).intValue() == lstTiposActos.size());
			}).collect(Collectors.toList());
			log.debug("Usuarios que cumplen todos los filtros: {}",lstUsuariosFinal.size());
			ponderacionAsumar = calculaPonderacionActos(prelacionConsulta.getActosParaPrelacions());
			log.debug("Ponderacion a sumar : {}",ponderacionAsumar);
			numDiasEstimados = sumaDiasEstimadosActos(prelacionConsulta.getActosParaPrelacions());
		//Se valida que sean servicios
		}else if(prelacionConsulta.getPrelacionServicioParaPrelacion() != null && !prelacionConsulta.getPrelacionServicioParaPrelacion().isEmpty()){
			log.debug("Prelacion con servicios ....");
			final List<Long> lstServicios = getServicio(prelacionConsulta.getPrelacionServicioParaPrelacion());
			log.debug("Numero de servicios en la prelacion : {}",lstServicios.size());
			final List<Long> lstAreas = getAreasFromServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
			log.debug("Numero de areas de los servicios : {}",lstAreas.size());
			lstUsuariosOfi = usuarioOfiAreasRepository.findByOficinaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(prelacionConsulta.getOficina().getId(), Boolean.TRUE, Constantes.Rol.REGISTRADOR.getIdRol());
			log.debug("Usuarios que cumplen elprimer filtro : {}",lstUsuariosOfi.size());
			lstUsuarios = getUsuarios(lstUsuariosOfi);
			log.debug("Usuarios sin duplicados : {}",lstUsuarios.size());
			lstUsuariosFinal = lstUsuarios.stream().filter(usuario -> {
				return (usuarioOfiAreasRepository.countByUsuarioIdAndActivoAndAreaIdIn(usuario.getId(),Boolean.TRUE, lstAreas).intValue() == lstAreas.size());
			}).filter(usuario -> {
				return (usuarioClasifActoServicioRepository.countByUsuarioIdAndTurnarTodosAndServicioIdIn(usuario.getId(), Constantes.TURNAR_TODOS, lstServicios).intValue() == lstServicios.size()
						|| usuarioServicioRepository.countByUsuarioIdAndIndTurnadoAndServicioIdIn(usuario.getId(), Boolean.TRUE, lstServicios).intValue() == lstServicios.size());
			}).collect(Collectors.toList());
			log.debug("Usuarios que cumplen todos los filtros: {}",lstUsuariosFinal.size());
			ponderacionAsumar = calculaPonderacionServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
			log.debug("Ponderacion a sumar : {}",ponderacionAsumar);
			numDiasEstimados = sumaDiasEstimadosServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
		}
		
		//Se valida que existan usuarios con las reglas
		if(lstUsuariosFinal != null && !lstUsuariosFinal.isEmpty()){
			try {
				log.debug("Recupearndo el escritorio con menor ponderacion ...");
				escAsignado = escritorioService.findByDiaEscritorioAndRolIdAndUsuarioIdInOrderByPonderacionAsc(UtilFecha.getStartOfDay(fechaActual),Constantes.Rol.REGISTRADOR.getIdRol(),getUsuariosIds(lstUsuariosFinal)).get(0);
				log.debug("Escritorio recuperado : ",escAsignado);
				ponderacionAsumar = ponderacionAsumar.intValue() + escAsignado.getPonderacion().intValue();
				log.debug("Ponderacion final : {}",ponderacionAsumar);
				escAsignado.setPonderacion(ponderacionAsumar);
				escritorioService.save(escAsignado);
				log.debug("Escritorio actualizado!");
				
				//Se valida que sean actos
				if(prelacionConsulta.getActosParaPrelacions() != null && !prelacionConsulta.getActosParaPrelacions().isEmpty()){
					log.debug("Prelacion con actos ....");
					final List<Long> lstClasificacionActos = getClasificacionActos(prelacionConsulta.getActosParaPrelacions());
					log.debug("Numero de clasificacion de actos : {}",lstClasificacionActos.size());
					final List<Long> lstTiposActos = getTiposActos(prelacionConsulta.getActosParaPrelacions());
					log.debug("Numero de tipos de acto : {}",lstTiposActos.size());
					idAreaActo = getAreaFromActo(prelacionConsulta.getActosParaPrelacions());
					if(idAreaActo != null){
						lstUsuariosOfi = usuarioOfiAreasRepository.findByOficinaIdAndAreaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(prelacionConsulta.getOficina().getId(),idAreaActo,Boolean.TRUE, Constantes.Rol.REGISTRADOR.getIdRol());
					}else{
						lstUsuariosOfi = usuarioOfiAreasRepository.findByOficinaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(prelacionConsulta.getOficina().getId(), Boolean.TRUE, Constantes.Rol.REGISTRADOR.getIdRol());
					}		
					
					log.debug("Usuarios que cumplen elprimer filtro : {}",lstUsuariosOfi.size());
					lstUsuarios = getUsuarios(lstUsuariosOfi);
					log.debug("Usuarios sin duplicados : {}",lstUsuarios.size());
					lstUsuariosFinal = lstUsuarios.stream().filter(usuario ->{
						return (usuarioClasifActoServicioRepository.countByUsuarioIdAndTurnarTodosAndClasifActoIdIn(usuario.getId(), Constantes.TURNAR_TODOS, lstClasificacionActos).intValue() == lstClasificacionActos.size()
								|| usuarioActosRepository.countByUsuarioIdAndIndTurnadoAndTipoActoIdIn(usuario.getId(), Boolean.TRUE, lstTiposActos).intValue() == lstTiposActos.size());
					}).collect(Collectors.toList());
					log.debug("Usuarios que cumplen todos los filtros: {}",lstUsuariosFinal.size());
					ponderacionAsumar = calculaPonderacionActos(prelacionConsulta.getActosParaPrelacions());
					log.debug("Ponderacion a sumar : {}",ponderacionAsumar);
					numDiasEstimados = sumaDiasEstimadosActos(prelacionConsulta.getActosParaPrelacions());
				//Se valida que sean servicios
				}else if(prelacionConsulta.getPrelacionServicioParaPrelacion() != null && !prelacionConsulta.getPrelacionServicioParaPrelacion().isEmpty()){
					log.debug("Prelacion con servicios ....");
					final List<Long> lstServicios = getServicio(prelacionConsulta.getPrelacionServicioParaPrelacion());
					log.debug("Numero de servicios en la prelacion : {}",lstServicios.size());
					final List<Long> lstAreas = getAreasFromServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
					log.debug("Numero de areas de los servicios : {}",lstAreas.size());
					lstUsuariosOfi = usuarioOfiAreasRepository.findByOficinaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(prelacionConsulta.getOficina().getId(), Boolean.TRUE, Constantes.Rol.REGISTRADOR.getIdRol());
					log.debug("Usuarios que cumplen elprimer filtro : {}",lstUsuariosOfi.size());
					lstUsuarios = getUsuarios(lstUsuariosOfi);
					log.debug("Usuarios sin duplicados : {}",lstUsuarios.size());
					lstUsuariosFinal = lstUsuarios.stream().filter(usuario -> {
						return (usuarioOfiAreasRepository.countByUsuarioIdAndActivoAndAreaIdIn(usuario.getId(),Boolean.TRUE, lstAreas).intValue() == lstAreas.size());
					}).filter(usuario -> {
						return (usuarioClasifActoServicioRepository.countByUsuarioIdAndTurnarTodosAndServicioIdIn(usuario.getId(), Constantes.TURNAR_TODOS, lstServicios).intValue() == lstServicios.size()
								|| usuarioServicioRepository.countByUsuarioIdAndIndTurnadoAndServicioIdIn(usuario.getId(), Boolean.TRUE, lstServicios).intValue() == lstServicios.size());
					}).collect(Collectors.toList());
					log.debug("Usuarios que cumplen todos los filtros: {}",lstUsuariosFinal.size());
					ponderacionAsumar = calculaPonderacionServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
					log.debug("Ponderacion a sumar : {}",ponderacionAsumar);
					numDiasEstimados = sumaDiasEstimadosServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
				}
				log.debug("Actualizando prelacion con el usuario seleccionado ...");
				prelacionConsulta.setUsuarioAnalista(escAsignado.getUsuario());
				 //IHM Actualizar usuario Captura y Validacion
				if(prelacionConsulta.getPrelacionAntesParaPrelacions() != null && !prelacionConsulta.getPrelacionAntesParaPrelacions().isEmpty()){
					prelacionConsulta.setUsuarioCapVal(escAsignado.getUsuario());
				}
				statusPrelacion = new Status();
				statusPrelacion.setId(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus());
				prelacionConsulta.setStatus(statusPrelacion);
				prelacionConsulta.setFechaProgramadaEntrega(addDiasHabiles(UtilFecha.getCurrentDate(), numDiasEstimados));
				prelacionRepository.save(prelacionConsulta);
				
				log.debug("Guardando en la bitacora de turnado...");
				bitacoraRepository.save(createBitacoraTurnado(escAsignado, prelacionConsulta));
				log.debug("Guardando en turno electronico ...");
				turnoElectronicoRepository.save(createTurnoElectronico(escAsignado, prelacionConsulta, fechaActual));
				
				log.debug("Proceso finalizado!");
			}catch(Exception e) {
				statusPrelacion = new Status();
				statusPrelacion.setId(Constantes.Status.RECIBIDO_PENDIENTE_TURNAR.getIdStatus());
				prelacionConsulta.setStatus(statusPrelacion);
				prelacionRepository.save(prelacionConsulta);
			}
	
		}else{
			statusPrelacion = new Status();
			statusPrelacion.setId(Constantes.Status.RECIBIDO_PENDIENTE_TURNAR.getIdStatus());
			prelacionConsulta.setStatus(statusPrelacion);
			prelacionRepository.save(prelacionConsulta);
		}
	}

	public void asignaACalificador(Prelacion prelacionConsulta) {
		List<UsuarioOfiAreas> lstUsuariosOfi = null;
		List<Usuario> lstUsuarios = null;
		List<Usuario> lstUsuariosFinal = null;
		Escritorio escAsignado = null;
		Integer ponderacionAsumar = null;
		Status statusPrelacion = null;
		int numDiasEstimados = 0;
		Long idAreaActo = null;

		Date fechaActual = UtilFecha.getCurrentDate();

		log.debug("aCalificador Se crean escritorios para usuarios con fecha [{}] y oficina [{}]", fechaActual,
				prelacionConsulta.getId());
		escritorioService.createEscritoriosByOficina(prelacionConsulta.getOficina().getId(),
				UtilFecha.getStartOfDay(fechaActual), Constantes.Rol.CALIFICADOR.getIdRol());

		// Se valida que sean actos
		if (prelacionConsulta.getActosParaPrelacions() != null
				&& !prelacionConsulta.getActosParaPrelacions().isEmpty()) {
			log.debug("Prelacion con actos ....");
			final List<Long> lstClasificacionActos = getClasificacionActos(prelacionConsulta.getActosParaPrelacions());
			log.debug("Numero de clasificacion de actos : {}", lstClasificacionActos.size());
			final List<Long> lstTiposActos = getTiposActos(prelacionConsulta.getActosParaPrelacions());
			log.debug("Numero de tipos de acto : {}", lstTiposActos.size());
			lstUsuariosOfi = usuarioOfiAreasRepository.findByOficinaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId( //aqui
					prelacionConsulta.getOficina().getId(), Boolean.TRUE, Constantes.Rol.CALIFICADOR.getIdRol());
			log.debug("Usuarios que cumplen elprimer filtro : {}", lstUsuariosOfi.size());
			lstUsuarios = getUsuarios(lstUsuariosOfi);
			log.debug("Usuarios sin duplicados : {}", lstUsuarios.size());
			lstUsuariosFinal = lstUsuarios.stream().filter(usuario -> {
				return (usuarioClasifActoServicioRepository
						.countByUsuarioIdAndTurnarTodosAndClasifActoIdIn(usuario.getId(), Constantes.TURNAR_TODOS,
								lstClasificacionActos)
						.intValue() == lstClasificacionActos.size()
						|| usuarioActosRepository.countByUsuarioIdAndIndTurnadoAndTipoActoIdIn(usuario.getId(),
								Boolean.TRUE, lstTiposActos).intValue() == lstTiposActos.size());
			}).collect(Collectors.toList());
			log.debug("Usuarios que cumplen todos los filtros: {}", lstUsuariosFinal.size());
			ponderacionAsumar = calculaPonderacionActos(prelacionConsulta.getActosParaPrelacions());
			log.debug("Ponderacion a sumar : {}", ponderacionAsumar);
			numDiasEstimados = sumaDiasEstimadosActos(prelacionConsulta.getActosParaPrelacions());
			// Se valida que sean servicios
		} else if (prelacionConsulta.getPrelacionServicioParaPrelacion() != null
				&& !prelacionConsulta.getPrelacionServicioParaPrelacion().isEmpty()) {
			log.debug("Prelacion con servicios ....");
			final List<Long> lstServicios = getServicio(prelacionConsulta.getPrelacionServicioParaPrelacion());
			log.debug("Numero de servicios en la prelacion : {}", lstServicios.size());
			final List<Long> lstAreas = getAreasFromServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
			log.debug("Numero de areas de los servicios : {}", lstAreas.size());
			lstUsuariosOfi = usuarioOfiAreasRepository.findByOficinaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(
					prelacionConsulta.getOficina().getId(), Boolean.TRUE, Constantes.Rol.CALIFICADOR.getIdRol());
			log.debug("Usuarios que cumplen elprimer filtro : {}", lstUsuariosOfi.size());
			lstUsuarios = getUsuarios(lstUsuariosOfi);
			log.debug("Usuarios sin duplicados : {}", lstUsuarios.size());
			lstUsuariosFinal = lstUsuarios.stream().filter(usuario -> {
				return (usuarioOfiAreasRepository
						.countByUsuarioIdAndActivoAndAreaIdIn(usuario.getId(), Boolean.TRUE, lstAreas)
						.intValue() == lstAreas.size());
			}).filter(usuario -> {
				return (usuarioClasifActoServicioRepository
						.countByUsuarioIdAndTurnarTodosAndServicioIdIn(usuario.getId(), Constantes.TURNAR_TODOS,
								lstServicios)
						.intValue() == lstServicios.size()
						|| usuarioServicioRepository.countByUsuarioIdAndIndTurnadoAndServicioIdIn(usuario.getId(),
								Boolean.TRUE, lstServicios).intValue() == lstServicios.size());
			}).collect(Collectors.toList());
			log.debug("Usuarios que cumplen todos los filtros: {}", lstUsuariosFinal.size());
			ponderacionAsumar = calculaPonderacionServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
			log.debug("Ponderacion a sumar : {}", ponderacionAsumar);
			numDiasEstimados = sumaDiasEstimadosServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
		}

		// Se valida que existan usuarios con las reglas
		if (lstUsuariosFinal != null && !lstUsuariosFinal.isEmpty()) {
			log.debug("Recupearndo el escritorio con menor ponderacion ...");
			escAsignado = escritorioService.findByDiaEscritorioAndRolIdAndUsuarioIdInOrderByPonderacionAsc(
					UtilFecha.getStartOfDay(fechaActual), Constantes.Rol.CALIFICADOR.getIdRol(),
					getUsuariosIds(lstUsuariosFinal)).get(0);
			log.debug("Escritorio recuperado : ", escAsignado);
			ponderacionAsumar = ponderacionAsumar.intValue() + escAsignado.getPonderacion().intValue();
			log.debug("Ponderacion final : {}", ponderacionAsumar);
			escAsignado.setPonderacion(ponderacionAsumar);
			escritorioService.save(escAsignado);
			log.debug("Escritorio actualizado!");

			// Se valida que sean actos
			if (prelacionConsulta.getActosParaPrelacions() != null
					&& !prelacionConsulta.getActosParaPrelacions().isEmpty()) {
				log.debug("Prelacion con actos ....");
				final List<Long> lstClasificacionActos = getClasificacionActos(
						prelacionConsulta.getActosParaPrelacions());
				log.debug("Numero de clasificacion de actos : {}", lstClasificacionActos.size());
				final List<Long> lstTiposActos = getTiposActos(prelacionConsulta.getActosParaPrelacions());
				log.debug("Numero de tipos de acto : {}", lstTiposActos.size());
				idAreaActo = getAreaFromActo(prelacionConsulta.getActosParaPrelacions());
				if (idAreaActo != null) {
					lstUsuariosOfi = usuarioOfiAreasRepository
							.findByOficinaIdAndAreaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(
									prelacionConsulta.getOficina().getId(), idAreaActo, Boolean.TRUE,
									Constantes.Rol.CALIFICADOR.getIdRol());
				} else {
					lstUsuariosOfi = usuarioOfiAreasRepository
							.findByOficinaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(
									prelacionConsulta.getOficina().getId(), Boolean.TRUE,
									Constantes.Rol.CALIFICADOR.getIdRol());
				}

				log.debug("Usuarios que cumplen elprimer filtro : {}", lstUsuariosOfi.size());
				lstUsuarios = getUsuarios(lstUsuariosOfi);
				log.debug("Usuarios sin duplicados : {}", lstUsuarios.size());
				lstUsuariosFinal = lstUsuarios.stream().filter(usuario -> {
					return (usuarioClasifActoServicioRepository
							.countByUsuarioIdAndTurnarTodosAndClasifActoIdIn(usuario.getId(), Constantes.TURNAR_TODOS,
									lstClasificacionActos)
							.intValue() == lstClasificacionActos.size()
							|| usuarioActosRepository.countByUsuarioIdAndIndTurnadoAndTipoActoIdIn(usuario.getId(),
									Boolean.TRUE, lstTiposActos).intValue() == lstTiposActos.size());
				}).collect(Collectors.toList());
				log.debug("Usuarios que cumplen todos los filtros: {}", lstUsuariosFinal.size());
				ponderacionAsumar = calculaPonderacionActos(prelacionConsulta.getActosParaPrelacions());
				log.debug("Ponderacion a sumar : {}", ponderacionAsumar);
				numDiasEstimados = sumaDiasEstimadosActos(prelacionConsulta.getActosParaPrelacions());
				// Se valida que sean servicios
			} else if (prelacionConsulta.getPrelacionServicioParaPrelacion() != null
					&& !prelacionConsulta.getPrelacionServicioParaPrelacion().isEmpty()) {
				log.debug("Prelacion con servicios ....");
				final List<Long> lstServicios = getServicio(prelacionConsulta.getPrelacionServicioParaPrelacion());
				log.debug("Numero de servicios en la prelacion : {}", lstServicios.size());
				final List<Long> lstAreas = getAreasFromServicios(
						prelacionConsulta.getPrelacionServicioParaPrelacion());
				log.debug("Numero de areas de los servicios : {}", lstAreas.size());
				lstUsuariosOfi = usuarioOfiAreasRepository
						.findByOficinaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(
								prelacionConsulta.getOficina().getId(), Boolean.TRUE,
								Constantes.Rol.CALIFICADOR.getIdRol());
				log.debug("Usuarios que cumplen elprimer filtro : {}", lstUsuariosOfi.size());
				lstUsuarios = getUsuarios(lstUsuariosOfi);
				log.debug("Usuarios sin duplicados : {}", lstUsuarios.size());
				lstUsuariosFinal = lstUsuarios.stream().filter(usuario -> {
					return (usuarioOfiAreasRepository
							.countByUsuarioIdAndActivoAndAreaIdIn(usuario.getId(), Boolean.TRUE, lstAreas)
							.intValue() == lstAreas.size());
				}).filter(usuario -> {
					return (usuarioClasifActoServicioRepository
							.countByUsuarioIdAndTurnarTodosAndServicioIdIn(usuario.getId(), Constantes.TURNAR_TODOS,
									lstServicios)
							.intValue() == lstServicios.size()
							|| usuarioServicioRepository.countByUsuarioIdAndIndTurnadoAndServicioIdIn(usuario.getId(),
									Boolean.TRUE, lstServicios).intValue() == lstServicios.size());
				}).collect(Collectors.toList());
				log.debug("Usuarios que cumplen todos los filtros: {}", lstUsuariosFinal.size());
				ponderacionAsumar = calculaPonderacionServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
				log.debug("Ponderacion a sumar : {}", ponderacionAsumar);
				numDiasEstimados = sumaDiasEstimadosServicios(prelacionConsulta.getPrelacionServicioParaPrelacion());
			}
			log.debug("Actualizando prelacion con el usuario seleccionado ...");
			prelacionConsulta.setUsuarioCalificador(escAsignado.getUsuario());
			statusPrelacion = new Status();
			statusPrelacion.setId(Constantes.Status.ASIGNADO_A_CALIFICADOR.getIdStatus());
			prelacionConsulta.setStatus(statusPrelacion);
			prelacionConsulta.setFechaProgramadaEntrega(addDiasHabiles(UtilFecha.getCurrentDate(), numDiasEstimados));
			prelacionRepository.save(prelacionConsulta);

			log.debug("Guardando en la bitacora de turnado...");
			bitacoraRepository.save(createBitacoraTurnado(escAsignado, prelacionConsulta));
			log.debug("Guardando en turno electronico ...");
			turnoElectronicoRepository.save(createTurnoElectronico(escAsignado, prelacionConsulta, fechaActual));

			log.debug("Proceso finalizado!");
		} else {
			statusPrelacion = new Status();
			statusPrelacion.setId(Constantes.Status.RECIBIDO_PENDIENTE_TURNAR.getIdStatus());
			prelacionConsulta.setStatus(statusPrelacion);
			prelacionRepository.save(prelacionConsulta);
		}
	}

	private void enviarAAnalisadoPorSistema(Prelacion prelacion){
		analisisPorSistemaService.procesar(prelacion);
	}

	
	private boolean canAnalizadoPorSistema(Prelacion prelacion){
		Set<PrelacionServicio> servicios = prelacion.getPrelacionServiciosParaPrelacions();
		boolean isServicioCertificado = false;

		if(servicios != null && !servicios.isEmpty()){
			isServicioCertificado = servicios.stream().findFirst().get().getServicio().getId().longValue() == Constantes.SERVICIO_CERTIFICADO_LIBERTAD_GRAVAMEN_ID;
		}

		return (prelacion.getStatus().getId().longValue() == (Constantes.Status.RECIBIDO_POR_VENTANILLA.getIdStatus().longValue())	&& isServicioCertificado && prelacion.getNumPorSistema() == 0);
	}

	
	private List<Long> getServicio(Set<PrelacionServicio> prelacionServicios){
		Map<Long,Long> servicios = new HashMap<>();
		prelacionServicios.forEach(prelacionServicio -> {
			servicios.put(prelacionServicio.getServicio().getId(),prelacionServicio.getServicio().getId() );
		});
		
		return servicios.keySet().stream().collect(Collectors.toList());
	}
	
	private List<Long> getClasificacionActos(Set<Acto> actos){
		Map<Long,Long> clasificacionActos = new HashMap<>();
		actos.forEach(acto -> {
			if(acto.getVf() == null || acto.getVf()==false) {
				clasificacionActos.put(acto.getTipoActo().getClasifActo().getId(),acto.getTipoActo().getClasifActo().getId());
			}
		});
		
		return clasificacionActos.keySet().stream().collect(Collectors.toList());
	}
	
	private List<Long> getTiposActos(Set<Acto> actos){
		Map<Long,Long> tiposActos = new HashMap<>();
		actos.forEach(acto -> {
			if(acto.getVf()==null || acto.getVf()==false){
				tiposActos.put(acto.getTipoActo().getId(),acto.getTipoActo().getId());
			}
		});
		
		return tiposActos.keySet().stream().collect(Collectors.toList());
	}
	
	private List<Long> getAreasFromServicios(Set<PrelacionServicio> prelacionServicios){
		Map<Long,Long> areas = new HashMap<>();
		
		prelacionServicios.forEach(prelacionServicio -> {
			areas.put(prelacionServicio.getServicio().getArea().getId(),prelacionServicio.getServicio().getArea().getId() );
		});
		return areas.keySet().stream().collect(Collectors.toList());
	}
	
	private List<Usuario> getUsuarios(List<UsuarioOfiAreas> lstUsuariosOfi){
		Map<Long,Usuario> usuarios = new HashMap<>();
		lstUsuariosOfi.forEach(usuarioOfi -> {
			usuarios.put(usuarioOfi.getUsuario().getId(), usuarioOfi.getUsuario());
		});
		return usuarios.values().stream().collect(Collectors.toList());
	}
	
	private List<Long> getUsuariosIds(List<Usuario> lstUsuarios){
		List<Long> usuarios = new ArrayList<>();
		lstUsuarios.forEach(usuario -> {
			usuarios.add(usuario.getId());
		});
		return usuarios;
	}
	
	private Integer calculaPonderacionServicios(Set<PrelacionServicio> prelacionServicios){
		int ponderacion = 0;
		for(PrelacionServicio servicio :prelacionServicios ){
			ponderacion = ponderacion + servicio.getServicio().getPonderacion().intValue();
		}

		return Integer.valueOf(ponderacion);
	}
	
	private Integer calculaPonderacionActos(Set<Acto> actos){
		int ponderacion = 0;
		for(Acto acto :actos ){
			ponderacion = ponderacion + acto.getTipoActo().getPonderacion().intValue();
		}

		return Integer.valueOf(ponderacion);
	}
	
	private Bitacora createBitacoraTurnado(Escritorio escritorio,Prelacion prelacion){
		//Status asignadoAnalista = new Status();
		//asignadoAnalista.setId(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus());
		Bitacora bitacora = new Bitacora();
		bitacora.setStatus(prelacion.getStatus());
		bitacora.setUsuario(escritorio.getUsuario());
		bitacora.setPrelacion(prelacion);
		bitacora.setFecha(UtilFecha.getCurrentDate());
		
		return bitacora;
	}
	
	private TurnoElectronico createTurnoElectronico(Escritorio escritorio,Prelacion prelacion,Date fechaEntrada){
		TurnoElectronico electronico = new TurnoElectronico();
		electronico.setFechaHoraAsigna(UtilFecha.getCurrentDate());
		electronico.setFechaHoraEntrada(fechaEntrada);
		electronico.setPonderacion(escritorio.getPonderacion());
		electronico.setStatus(Integer.valueOf(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus().intValue()));
		electronico.setUsuarioOrigen(prelacion.getUsuarioVentanilla());
		electronico.setPrelacion(prelacion);
		electronico.setUsuarioDestino(escritorio.getUsuario());
		
		return electronico;
	}
	//JADV-SUSPENSION
	public Date addDiasHabiles(Date fechaActual, int dias) {
		int diaSum = 0;
		Calendar fecha = Calendar.getInstance();
		fecha.setTime(fechaActual);
		Date fechaIni = null;
		Date fechaFin = null;
		while (diaSum < dias) {
			fecha.add(Calendar.DATE, 1);
			fechaIni = UtilFecha.getStartOfDay(fecha.getTime());
			fechaFin = UtilFecha.getEndOfDay(fecha.getTime());
			if (fecha.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
					&& fecha.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
					&& diaInhabilRepository.countByFechaDiaInhabilBetween(fechaIni, fechaFin).longValue() == 0) {
				diaSum++;
			}
		}

		return fecha.getTime();
	}

	public int ObtieneDiasDesdeFecha(Date fechaBase) {
		int diaSum = 0;
		Calendar fecha = Calendar.getInstance();
		fecha.setTime(new Date());
		Date fechaIni = null;
		Date fechaFin = null;
	
		int dias=(int) ((fecha.getTime().getTime() - fechaBase.getTime() )/86400000);
		
		while (diaSum < dias) {
			fecha.add(Calendar.DATE, 1);
			fechaIni = UtilFecha.getStartOfDay(fecha.getTime());
			fechaFin = UtilFecha.getEndOfDay(fecha.getTime());
			if (fecha.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
					&& fecha.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
					&& diaInhabilRepository.countByFechaDiaInhabilBetween(fechaIni, fechaFin).longValue() == 0) {
				diaSum++;
			}
		}

		return diaSum;
	}
	
	private  int sumaDiasEstimadosActos(Set<Acto> actos){
		int dias = 0;
		for(Acto acto :actos ){
			if(acto.getTipoActo().getDiasTramitar() != null){
				dias = dias + acto.getTipoActo().getDiasTramitar().intValue();
			}
		}
		return dias;
	}
	
	private  int sumaDiasEstimadosServicios(Set<PrelacionServicio> prelacionServicios){
		int dias = 0;
		for(PrelacionServicio servicio :prelacionServicios ){
			if(servicio.getServicio().getDiasTramitar() != null){
				dias = dias + servicio.getServicio().getDiasTramitar().intValue();
			}
		}
		return dias;
	}
	
	private boolean isCYVF(Prelacion prelacion){
		boolean asignar = false;
		if(prelacion.getPrelacionAntesParaPrelacions() != null && !prelacion.getPrelacionAntesParaPrelacions().isEmpty()){
			log.debug("Existen registros en prelacion_entecedente!");
			asignar = true;
		}
		if(!asignar && prelacion.getPrelacionPrediosParaPrelacions() != null && !prelacion.getPrelacionPrediosParaPrelacions().isEmpty()){
			log.debug("Existen registros en prelacion predio se validara si la caratula esta actualizada para algun predio ..");
			for(PrelacionPredio prelacionPredio:prelacion.getPrelacionPrediosParaPrelacions()){
				log.debug("PRELACION PREDIO : {}",prelacionPredio);
				if(prelacionPredio != null && !isCaratulaActualizada(prelacionPredio)){
					log.debug("Caratula no actualizada para el tipo de folio : [{}]",prelacionPredio.getTipoFolio());
					asignar = true;
					break;
				}
			}
			if(!asignar){
				for(PrelacionPredio prelacionPredio:prelacion.getPrelacionPrediosParaPrelacions()){
					
					//Es predio
					if(prelacionPredio.getTipoFolio() != null && prelacionPredio.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.PREDIO.idTipoFolio.intValue()){
						log.debug("Validando actos firmado para predio ...");
						if(prelacionPredio.getPredio() != null && prelacionPredio.getPredio().getActoPrediosParaPredios() != null && !prelacionPredio.getPredio().getActoPrediosParaPredios().isEmpty()){
							log.debug("Existen actos se validara que tengan hash_fila");
							for(ActoPredio actoPredio : prelacionPredio.getPredio().getActoPrediosParaPredios()){
								if(actoPredio.getActo().getTipoActo()!=null) 
								{
									if((actoPredio.getActo().getActoFirmasParaActos() == null || actoPredio.getActo().getActoFirmasParaActos().isEmpty())										 
											&& (actoPredio.getActo().getStatusActo().getId().longValue() == Constantes.Status.RECIBIDO_POR_VENTANILLA.getIdStatus().longValue() 
											||actoPredio.getActo().getStatusActo().getId().longValue() == Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus().longValue()  )){
										log.debug("El acto [{}] tiene hash_fila = null",actoPredio.getActo().getId());
										asignar = true;
										break;
									}
								}
							}
						}
					}
					//Es mueble
					if(prelacionPredio.getTipoFolio() != null && prelacionPredio.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.MUEBLE.idTipoFolio.intValue()){
						log.debug("Validando actos firmado para mueble ...");
						if(prelacionPredio.getMueble() != null && prelacionPredio.getMueble().getActoPrediosParaMuebles() != null && !prelacionPredio.getMueble().getActoPrediosParaMuebles().isEmpty()){
							log.debug("Existen actos se validara que tengan hash_fila");
							for(ActoPredio actoPredio : prelacionPredio.getMueble().getActoPrediosParaMuebles()){
								if((actoPredio.getActo().getActoFirmasParaActos() == null || actoPredio.getActo().getActoFirmasParaActos().isEmpty()) 
									&& (actoPredio.getActo().getStatusActo().getId().longValue() == Constantes.Status.RECIBIDO_POR_VENTANILLA.getIdStatus().longValue() 
									||actoPredio.getActo().getStatusActo().getId().longValue() == Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus().longValue()  ))
								{
									log.debug("El acto [{}] tiene hash_fila = null",actoPredio.getActo().getId());
									asignar = true;
									break;
								}
							}
						}
					}
					
					//Folio seccion auxiliar
					if(prelacionPredio.getTipoFolio() != null && prelacionPredio.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio.intValue()){
						log.debug("Validando actos firmado para auxiliar ...");
						if(prelacionPredio.getFolioSeccionAuxiliar() != null && prelacionPredio.getFolioSeccionAuxiliar().getActoPrediosParaFolioSeccionAuxiliares() != null && !prelacionPredio.getFolioSeccionAuxiliar().getActoPrediosParaFolioSeccionAuxiliares().isEmpty()){
							log.debug("Existen actos se validara que tengan hash_fila");
							for(ActoPredio actoPredio : prelacionPredio.getFolioSeccionAuxiliar().getActoPrediosParaFolioSeccionAuxiliares()){
								if((actoPredio.getActo().getActoFirmasParaActos() == null || actoPredio.getActo().getActoFirmasParaActos().isEmpty()) 
										&& (actoPredio.getActo().getStatusActo().getId().longValue() == Constantes.Status.RECIBIDO_POR_VENTANILLA.getIdStatus().longValue() 
																				||actoPredio.getActo().getStatusActo().getId().longValue() == Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus().longValue()  )){
									log.debug("El acto [{}] tiene hash_fila = null",actoPredio.getActo().getId());
									asignar = true;
									break;
								}
							}
						}
					}	
					
					//Es persona juridica
					if(prelacionPredio.getTipoFolio() != null && prelacionPredio.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio.intValue()){
						log.debug("Validando actos firmado para perona juridica ...");
						if(prelacionPredio.getPersonaJuridica() != null && prelacionPredio.getPersonaJuridica().getActoPrediosParaPersonasJuridicas() != null && !prelacionPredio.getPersonaJuridica().getActoPrediosParaPersonasJuridicas().isEmpty()){
							log.debug("Existen actos se validara que tengan hash_fila");
							for(ActoPredio actoPredio : prelacionPredio.getPersonaJuridica().getActoPrediosParaPersonasJuridicas()){
								if((actoPredio.getActo().getActoFirmasParaActos() == null || actoPredio.getActo().getActoFirmasParaActos().isEmpty()) 
										&& (actoPredio.getActo().getStatusActo().getId().longValue() == Constantes.Status.RECIBIDO_POR_VENTANILLA.getIdStatus().longValue() 
																				||actoPredio.getActo().getStatusActo().getId().longValue() == Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus().longValue()  )){
									log.debug("El acto [{}] tiene hash_fila = null",actoPredio.getActo().getId());
									asignar = true;
									break;
								}
							}
						}
					}
				}
			}
		}
		log.debug("Asignar a CYVF : {}",asignar);
		return asignar;
	}
	
	private void asignarACyvf(Prelacion prelacion){
		log.debug("Asignando a CYVF ...");
		Status statusPrelacion = null;
		List<Usuario> lstUsuariosCyvf = usuarioRepository.findByOficinaIdAndUsuarioRolesParaUsuariosRolIdAndActivo(prelacion.getOficina().getId(), Constantes.Rol.VALIDADOR.getIdRol(), Boolean.TRUE);
		List<Long> usuariosId = null;
		Date fechaActual = UtilFecha.getCurrentDate();
		Escritorio escAsignado = null;
		Integer ponderacionAsumar = Constantes.PONDERACION_CYVF;//
		if(lstUsuariosCyvf != null && !lstUsuariosCyvf.isEmpty()){
			log.debug("CYVF Se crean escritorios para usuarios con fecha [{}] y oficina [{}]",fechaActual,prelacion.getId());
			escritorioService.createEscritoriosByOficina(prelacion.getOficina().getId(), UtilFecha.getStartOfDay(fechaActual),Constantes.Rol.VALIDADOR.getIdRol());
			usuariosId = getUsuariosIds(lstUsuariosCyvf);
			
			log.debug("Recupearndo el escritorio con menor ponderacion ...");
			escAsignado = escritorioService.findByDiaEscritorioAndRolIdAndUsuarioIdInOrderByPonderacionAsc(UtilFecha.getStartOfDay(fechaActual),Constantes.Rol.VALIDADOR.getIdRol(),usuariosId).get(0);
			log.debug("Escritorio recuperado : ",escAsignado);
			ponderacionAsumar = ponderacionAsumar.intValue() + escAsignado.getPonderacion().intValue();
			log.debug("Ponderacion final : {}",ponderacionAsumar);
			escAsignado.setPonderacion(ponderacionAsumar);
			escritorioService.save(escAsignado);
			log.debug("Escritorio actualizado!");
			
			log.debug("Actualizando prelacion con el usuario seleccionado ...");
			prelacion.setUsuarioCapVal(escAsignado.getUsuario());
			statusPrelacion = new Status();
			statusPrelacion.setId(Constantes.Status.ASIGNADOR_A_VALIDADOR_DE_PREDIOS.getIdStatus());
			prelacion.setStatus(statusPrelacion);
			//prelacion.setFechaProgramadaEntrega(addDiasHabiles(UtilFecha.getCurrentDate(), numDiasEstimados));
			prelacionRepository.save(prelacion);
			
			log.debug("Guardando en la bitacora de turnado...");
			bitacoraRepository.save(createBitacoraTurnado(escAsignado, prelacion));
			
			log.debug("Guardando en turno electronico ...");
			turnoElectronicoRepository.save(createTurnoElectronico(escAsignado, prelacion, fechaActual));
			
			log.debug("Proceso CYVF finalizado!");
			
		}else{//No hay validadores se queda pendiente
			log.debug("CYVF No existen validadores se queda pendiente ...");
			statusPrelacion = new Status();
			statusPrelacion.setId(Constantes.Status.RECIBIDO_PENDIENTE_TURNAR.getIdStatus());
			prelacion.setStatus(statusPrelacion);
			prelacionRepository.save(prelacion);
		}
		
	}
	
	public boolean isCaratulaActualizada(PrelacionPredio prelacionPredio){
		boolean isCaratulaActualizada = false;
		log.debug("Tipo de folio a validar : {}",prelacionPredio.getTipoFolio());
		log.debug("PREDI : {}",prelacionPredio.getPredio());
		log.debug("MUEBLE : {}",prelacionPredio.getMueble());
		log.debug("SECCION AUXILIAR : {}",prelacionPredio.getFolioSeccionAuxiliar());
		log.debug("PERSONA JURIDICA : {}",prelacionPredio.getPersonaJuridica());
		if(prelacionPredio.getTipoFolio() != null && prelacionPredio.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.PREDIO.idTipoFolio.intValue()
				&& prelacionPredio.getPredio() != null){
			log.debug("Bandera de caratula actualizada : {}",prelacionPredio.getPredio().getCaratulaActualizada());
			isCaratulaActualizada = prelacionPredio.getPredio().getCaratulaActualizada() == null?false:prelacionPredio.getPredio().getCaratulaActualizada().booleanValue(); 
		}else if(prelacionPredio.getTipoFolio() != null && prelacionPredio.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.MUEBLE.idTipoFolio.intValue()
				&& prelacionPredio.getMueble() != null){
			log.debug("Bandera de caratula actualizada : {}",prelacionPredio.getMueble().getCaratulaActualizada());
			isCaratulaActualizada = prelacionPredio.getMueble().getCaratulaActualizada() == null?false:prelacionPredio.getMueble().getCaratulaActualizada().booleanValue();
		}else if(prelacionPredio.getTipoFolio() != null && prelacionPredio.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio.intValue()
				&& prelacionPredio.getFolioSeccionAuxiliar() != null){
			log.debug("Bandera de caratula actualizada : {}",prelacionPredio.getFolioSeccionAuxiliar().getCaratulaActualizada());
			isCaratulaActualizada = prelacionPredio.getFolioSeccionAuxiliar().getCaratulaActualizada() == null?false:prelacionPredio.getFolioSeccionAuxiliar().getCaratulaActualizada().booleanValue();
		}else if(prelacionPredio.getTipoFolio() != null && prelacionPredio.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio.intValue()
				&& prelacionPredio.getPersonaJuridica() != null){
			log.debug("Bandera de caratula actualizada : {}",prelacionPredio.getPersonaJuridica().getCaratulaActualizada());
			isCaratulaActualizada = prelacionPredio.getPersonaJuridica().getCaratulaActualizada() == null?false:prelacionPredio.getPersonaJuridica().getCaratulaActualizada().booleanValue();
		}
		return isCaratulaActualizada;
	}
	
	private Long getAreaFromActo(Set<Acto> actos){
		Long idArea = null;
		TipoActo tipoActo = null;
		AreaClasifActo areaClasifActo = null;
		if(actos != null && !actos.isEmpty()){
			tipoActo = actos.iterator().next().getTipoActo();
			if(tipoActo != null && tipoActo.getClasifActo() != null && tipoActo.getClasifActo().getAreaClasifActosParaClasifActos() != null
					&& !tipoActo.getClasifActo().getAreaClasifActosParaClasifActos().isEmpty()){
				areaClasifActo = tipoActo.getClasifActo().getAreaClasifActosParaClasifActos().iterator().next();
				if(areaClasifActo != null && areaClasifActo.getArea() != null){
					idArea = areaClasifActo.getArea().getId();
				}
			}
		}
		
		return idArea;
	}
}
