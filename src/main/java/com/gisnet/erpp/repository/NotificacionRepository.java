package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Notificacion;
import com.gisnet.erpp.domain.Usuario;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Area entity.
 */
@SuppressWarnings("unused")
public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {
	

	public List<Notificacion> findAllByUsuarioDestinatarioAndStatusNotificacionIdIn(Usuario usuario, List<Long> status);
	
	public Long countByUsuarioDestinatarioAndStatusNotificacionIdIn(Usuario usuario, List<Long> status);
}
