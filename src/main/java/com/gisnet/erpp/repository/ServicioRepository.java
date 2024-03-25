package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Servicio;
import com.gisnet.erpp.domain.TipoActo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Servicio entity.
 */
@SuppressWarnings("unused")
public interface ServicioRepository extends JpaRepository<Servicio,Long>, ServicioRepositoryCustom {

	List<Servicio> findByAreaId(Long idArea);

	List<Servicio> findByClasifActoId(Long idArea);
	
	public Servicio findOneById(Long id);
	
	List<Servicio> findByAreaIdAndClasifActoId(Long idArea,Long clasifActoId);
	
	List<Servicio> findByAreaIdAndClasifActoIdAndTipoActoId(Long idArea,Long idActo,Long idTipoActo);
	
	@Query(value="SELECT p FROM Servicio p where p.clasifActo.id in(:ids) and p.tipoActo.activo = :activos ")
	public abstract List <Servicio> findAllByClasif(@Param("ids") List <Long> id,@Param("activos")Boolean activos);


	@Query(value=" select s.* from  SERVICIO  s" +
			" join TIPO_USUARIO_SERVICIO  t on t.servicio_id=s.id"+
			" where t.tipo_usuario_id=:tipo  and t.acceso_directo=:acceso", 
		nativeQuery=true
	)
	List<Servicio> findAllByTipoUsuario(@Param("tipo") Long tipoUsuario, @Param("acceso") String acceso);

	@Query(value=" select s.* from  SERVICIO  s" +
			" join TIPO_USUARIO_SERVICIO  t on t.servicio_id=s.id"+
			" where t.tipo_usuario_id=:tipo  and t.acceso_directo=:acceso and s.area_id=:areaId"+
			" order by s.nombre", 
		nativeQuery=true
	)
	List<Servicio> findAllByTipoUsuario(@Param("tipo") Long tipoUsuario, @Param("acceso") String acceso, @Param("areaId") Long areaId);
}
