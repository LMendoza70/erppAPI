package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.ConsultaPrelacionVO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PrelacionRepositoryCustom {
	
	Page<ConsultaPrelacionVO> findAllByRechazadas(
			Pageable pageable,
			Integer consecutivo, Date desdeFecha, Date hastaFecha, Integer escritura, 
			Long notarioId,
			Long contratanteId,
			Long tipoActoId,
			Long registradorId);
	
	Page<ConsultaPrelacionVO> findAllByStatus(Usuario usuario, Integer status, Long folio, Integer folioPredio, Date fechaI, Date fechaH, Date fechaEF, Date fechaB,
			Long solicitante, Long registrador,Long calificador,
											  Pageable pageable,
			Long contratante,
			String referencia, Long region, Long area,
			Long coordinador,
			Long actoId,
			Long notarioId,
			String escritura,
			Boolean pantallaCoordinador,Integer prioridad);

	//JADV-SUSPENSION
	public abstract Page<Prelacion> findAllByStatusAndUsuario(Usuario usuario,
			Integer tipoUsuario,
															  Integer estado,
															  List<Constantes.Status> status,
															  Long folio,
															  Date fechaIngreso,
															  Date fechaEnvioFirma,
															  Date fechaBaja,
															  Usuario solicitante,
															  Pageable pageable);

	public abstract List<Prelacion> findAllByStatusAndUsuarioCoordinador(List <Usuario> usuario,
															  List <Constantes.Status> status,

																		 Long tipo,
																		 Long folio,
															  Date fechaIngreso,
															  Date fechaEnvioFirma,
															  Usuario solicitante,
																		 Usuario notario,

																		 Usuario calificador,
																		 Integer estado,
																		 Long oficina,
																		 Pageable pageable);

	public abstract Page<Prelacion> findAllByActosRechazados(Usuario usuario,
																		 Constantes.Status status,

																		 Long tipo,
																		 Long folio,
																		 Date fechaIngreso,
																		 Date fechaEnvioFirma,
																		 Usuario solicitante,
																		 Usuario notario,
																		 Usuario calificador,

																		 Pageable pageable);
}
