package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Acceso;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Prelacion;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the Acceso entity.
 */
@SuppressWarnings("unused")
public interface AccesoRepository extends JpaRepository<Acceso, Long> {
	public Acceso findOneByUsuarioIdAndSessionId(long usuarioId, String sessionId);
    public Acceso findOneBySessionIdAndUsuarioId(String sessionId,long usuarioId);
    public Acceso findFirstByUsuarioIdAndFechaFinIsNotNullOrderByFechaFinDesc(long usuarioId);
    
	public int countByUsuarioIdAndFechaFinNull(long usuarioId);

	@Modifying
	@Query("update Acceso  set fechaFin= :fechaFin where fechaFin is null ")
	public void updateFechaFinWhereFechaFinNull(@Param("fechaFin") Date fechaFin);
    
	@Modifying
	@Query(value="update Acceso  set fecha_fin= :fechaFin where usuario_id = :usuarioId  and fecha_fin is null",nativeQuery=true)
	public void userUnlock(@Param("fechaFin") Date fechaFin,@Param("usuarioId") Long usuarioId);
}
