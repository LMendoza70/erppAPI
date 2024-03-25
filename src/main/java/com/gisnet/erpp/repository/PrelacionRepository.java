package com.gisnet.erpp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.domain.Usuario;

/**
 * Spring Data JPA repository for the Prelacion entity.
 */
@SuppressWarnings("unused")
public interface PrelacionRepository extends JpaRepository<Prelacion,Long>, PrelacionRepositoryCustom {

	@Query("SELECT p FROM Prelacion p INNER JOIN p.usuarioAnalista ua INNER JOIN p.prioridad prd INNER JOIN p.status est WHERE ua.userName = :userName AND est.id in (:status) ORDER BY prd.id DESC, p.fechaIngreso ASC")
	List<Prelacion> findAllByAnalista(@Param("userName") String userName,@Param("status") List<Long> status);

	@Query("SELECT p FROM Prelacion p JOIN FETCH p.prelacionServiciosParaPrelacions INNER JOIN p.usuarioAnalista ua INNER JOIN p.prioridad prd INNER JOIN p.status est WHERE ua.id = :usuarioId AND est.id = :status ORDER BY prd.id DESC, p.fechaIngreso ASC")
	List<Prelacion> findAllWithServiciosByAnalistaAndStatus(@Param("usuarioId") Long usuarioId,@Param("status") Long status);

	@Query(value="SELECT nextval(:nameSequence)",nativeQuery=true)
	Integer getConsecutivo(@Param("nameSequence") String nameSequence);
	
	@Query(value="SELECT nextval('PRELACION_NEG_SEQUENCE')",nativeQuery=true)
	Integer getConsecutivoNeg();
	
	@Query(value="SELECT PRELACION_NEG_SEQUENCE.nextval from dual",nativeQuery=true)
	Integer getConsecutivoNegOracle();
	
	@Query(value="SELECT setval(:nameSequence,(select max(consecutivo) from prelacion where anio=:anio and oficina_id=:oficinaid ))",nativeQuery=true)
	Integer updateSecuencia(@Param("nameSequence") String nameSequence,@Param("anio") int anio, @Param("oficinaid") Long oficinaId);
	
	@Query(value="SELECT  case when count(1) > 0 then true else false end FROM prelacion where anio =:anio and oficina_id =:oficinaid and consecutivo =:consecutivo", nativeQuery=true)
	Boolean existPrelacion(@Param("consecutivo")Integer consecutivo,@Param("anio") int anio, @Param("oficinaid") Long oficinaId);
	
	@Query("SELECT p FROM Prelacion p INNER JOIN p.usuarioCalificador ua INNER JOIN p.prioridad prd INNER JOIN p.oficina oficina INNER JOIN p.status est WHERE ua = :usuario AND est in :status AND oficina=:oficina ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion> findAllByBandejaByUsuarioAndStatusCalificador(@Param("status") List<Status> status, @Param("usuario") Usuario usuario,@Param("oficina") Oficina oficina);
	
	@Query("SELECT p FROM Prelacion p INNER JOIN p.usuarioAnalista ua INNER JOIN p.prioridad prd INNER JOIN p.oficina oficina INNER JOIN p.status est WHERE ua = :usuario AND est in :status AND oficina=:oficina AND (p.es_digitalizado is null or p.es_digitalizado=true) ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion> findAllByBandejaByUsuarioAndStatus(@Param("status") List<Status> status, @Param("usuario") Usuario usuario,@Param("oficina") Oficina oficina);

	@Query("SELECT p FROM Prelacion p INNER JOIN p.usuarioAnalista ua INNER JOIN p.prioridad prd INNER JOIN p.oficina oficina INNER JOIN p.status est WHERE ua = :usuario AND est in :status AND oficina=:oficina AND (p.es_digitalizado is null or p.es_digitalizado=true) AND p.observacionesMotivo is not null ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion> findAllByBandejaByUsuarioAndStatusAndObservacion(@Param("status") List<Status> status, @Param("usuario") Usuario usuario,@Param("oficina") Oficina oficina);

	@Query("SELECT p FROM Prelacion p INNER JOIN p.usuarioAnalista ua INNER JOIN p.prioridad prd INNER JOIN p.oficina oficina INNER JOIN p.status est WHERE oficina=:oficina AND p.es_digitalizado=false AND p.indEntrega=true and p.status.id not in (20,21) ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion> findAllByBandejaDigitalizaByOficina(@Param("oficina") Oficina oficina);

	@Query("SELECT p FROM Prelacion p INNER JOIN p.usuarioAnalista ua INNER JOIN p.prioridad prd INNER JOIN p.status est  WHERE ua = :usuario AND est=:status ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion> findAllByBandejaByUsuarioAndStatus(@Param("status") Status status, @Param("usuario") Usuario usuario);
	
	@Query("SELECT p FROM Prelacion p INNER JOIN p.usuarioCalificador ua INNER JOIN p.prioridad prd INNER JOIN p.status est WHERE ua = :usuario AND est=:status ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion> findAllByBandejaByUsuarioCalAndStatus(@Param("status") Status status, @Param("usuario") Usuario usuario);

	@Query("SELECT p FROM Prelacion p  INNER JOIN p.prioridad prd INNER JOIN p.status est WHERE  est=:status ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion> ffindAllByBandejaByStatus(@Param("status") Status status);	

	@Query("SELECT p FROM Prelacion p  INNER JOIN p.prioridad prd INNER JOIN p.status est INNER JOIN p.oficina oficina  WHERE  est in :status AND oficina=:oficina  ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion> ffindAllByBandejaByStatusAndOficina(@Param("status") List<Status> status, @Param("oficina") Oficina oficina);

	@Query("SELECT p FROM Prelacion p INNER JOIN p.usuarioCapVal ua INNER JOIN p.prioridad prd INNER JOIN p.status est WHERE ua = :usuario AND est=:status AND (p.es_digitalizado is null or p.es_digitalizado=true) ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion> findAllByBandejaByUsuarioCYVFAndStatus(@Param("status") Status status, @Param("usuario") Usuario usuario);

	List<Prelacion> findAllByStatusAndUsuarioCapValAndConsecutivoIsNotNullOrderByConsecutivoAsc(Status status,Usuario usuario);
	
	List<Prelacion> findAllByStatusAndUsuarioAnalistaAndConsecutivoIsNotNullOrderByConsecutivoAsc(Status status,Usuario usuario);

	List<Prelacion> findAllByStatusAndUsuarioCoordinadorAndConsecutivoIsNotNullOrderByConsecutivoAsc(Status status, Usuario usuario);
	
	@Query("SELECT p FROM Prelacion p WHERE p.consecutivo=:consecutivo " +" AND p.oficina.id=:oficinaId " +	" AND YEAR(p.fechaEnvioFirma)=:anioEnvioFirma")
		    
	List<Prelacion> findBusquedaValida (@Param("oficinaId") Long oficinaId,@Param("consecutivo") Integer consecutivo,@Param("anioEnvioFirma") Integer anioEnvioFirma); 
	
	@Query(value="SELECT p FROM Prelacion p where p.status.id =:status and extract(year from p.fechaIngreso) =:a and extract(month from p.fechaIngreso) =:m and extract(day from p.fechaIngreso) =:d")
	List<Prelacion> findAllByStatusIdAndFecha(@Param("status") Long status, @Param("a") Integer a,@Param("m") Integer m,@Param("d") Integer d);
	
	@Query(value="SELECT p FROM Prelacion p where p.status.id in(:status)AND p.oficina.id=:oficinaId order by fechaIngreso DESC")
	List<Prelacion> findAllByStatusIdAndConsecutivoIsNotNullOrderByConsecutivoAsc(@Param("status") List<Long> status, @Param("oficinaId") Long oficinaId);

	@Query(value = "SELECT p FROM Prelacion p where p.status.id in(:status)AND p.oficina.id=:oficinaId order by fechaIngreso DESC")
	List<Prelacion> findAllByStatusIdAndConsecutivoIsNotNullOrderByConsecutivoAsc(@Param("status") List<Long> status, @Param("oficinaId") Long oficinaId, Pageable pageable);
	
	List<Prelacion> findAllByStatusAndConsecutivo(Status status,int consecutivo);

	@Query("SELECT p FROM Prelacion p  WHERE  p.consecutivo=:consecutivo AND p.oficina.id=:oficinaId")
	Prelacion findAllByConsecutivoOficina(@Param("consecutivo") Integer consecutivo, @Param("oficinaId") Long oficinaId);
	
	Prelacion findAllByConsecutivoAndOficinaIdAndAnio(int consecutivo,Long oficinaId,int anio);

	Prelacion findAllByOficinaIdAndAnioAndRecibosParaPrelacions_Referencia(Long oficinaId,int anio,String referencia);

	@Query("SELECT p FROM Prelacion p  WHERE  p.id=:id AND p.oficina.id=:oficinaId")
	Prelacion findAllByIdPrelacion(@Param("id") Long id, @Param("oficinaId") Long oficinaId);
	
	Prelacion findAllByConsecutivo(int consecutivo); 
	
	@Query("SELECT p FROM Prelacion p WHERE p.status.id IN(:status) and p.usuarioSolicitan =:usuario and p.fechaIngreso between :start and :end AND p.consecutivo IS NOT NULL")
	List<Prelacion> findAllNotario(@Param("status") List<Long> status,@Param("usuario")  Usuario notario,@Param("start")  Date FechaIni,@Param("end") Date FechaFin);
	
	@Query("SELECT p FROM Prelacion p  WHERE p.usuarioSolicitan =:usuario and p.fechaIngreso between :start and :end AND p.consecutivo IS NOT NULL")
	List<Prelacion> findAllNotario(@Param("usuario")  Usuario notario,@Param("start")  Date FechaIni,@Param("end") Date FechaFin);
	
	@Query("SELECT p FROM Prelacion p  WHERE  p.status.id IN(:status) and p.usuarioSolicitan =:usuario AND p.consecutivo IS NOT NULL")
	List<Prelacion> findAllNotario(@Param("status") List<Long> status,@Param("usuario")  Usuario notario);
	
	@Query("SELECT p FROM Prelacion p  WHERE p.usuarioSolicitan =:usuario AND p.consecutivo IS NOT NULL")
	List<Prelacion> findAllNotario(@Param("usuario")  Usuario notario);
	
	@Query("SELECT p FROM Prelacion p WHERE p.status IN (:status) AND p.usuarioSolicitan =:usuario ")
	List<Prelacion> findAllByStatusAndUsuarioSolicitanAndConsecutivoIsNotNullOrderByConsecutivoAsc(@Param("status") List<Status> status,@Param("usuario") Usuario usuario);
	
	@Query(value="SELECT p FROM Prelacion p where p.id in(:prelaciones) and p.fechaIngreso >= :fecha ")
	List<Prelacion> findAllById(@Param("prelaciones")List<Long> ids,@Param("fecha")Date fecha);
		
	@Query(value="SELECT p FROM Prelacion p where now()::date - p.fechaIngreso::date =:dias AND p.status.id IN (:status)", nativeQuery = true)
	List<Prelacion> findPrelacionesAtrasadas(@Param("dias")Double ids,@Param("status")List<Long> status);
		
	@Query(value="SELECT p FROM Prelacion p where now()::date - p.fechaIngreso::date >= :dias AND p.status.id IN (:status)", nativeQuery = true)
	List<Prelacion> findPrelacionesAtrasadasPost(@Param("dias")Double ids,@Param("status")List<Long> status);
	
	List<Prelacion> findAllByUsuarioSolicitanAndConsecutivoIsNull(Usuario us);

	List<Prelacion> findAllByStatusIdAndUsuarioSolicitanAndConsecutivoIsNull(Long statusId, Usuario us);

	@Query(value="SELECT p FROM Prelacion p where p.consecutivo>1")
	List<Prelacion> findAllById();

	Prelacion findById(Long id);
	
	//JADV-SUSPENSION
	List<Prelacion> findAllByStatus(Status status);
	
	List<Prelacion> findAllByMarcarSuspensivo(boolean marcarSuspensivo);
	
	@Query(value="SELECT p FROM Prelacion p where  p.status.id IN (:status) and p.oficina.id = :oficina and p.area.id in (:areas)")
	List<Prelacion> findAllByListStatus(@Param("status")List<Long> status, @Param("areas")List<Long> areas, @Param("oficina")Long oficina);

	@Query("SELECT p FROM Prelacion p  INNER JOIN p.prioridad prd INNER JOIN p.status est WHERE  est=:status and p.area.id in (:areaIds) ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion> findAllByBandejaByStatusAndAreasUsuario(@Param("status") Status status, @Param("areaIds") List<Long> areaIds);

	@Query("SELECT p FROM Prelacion p INNER JOIN p.prioridad prd WHERE  p.oficina.id=:oficinaId AND p.status.id=:statusId AND p.es_digitalizado=:dig AND p.indEntrega=:indEntrga ORDER BY prd.id DESC, p.consecutivo ASC")
	List<Prelacion>  findAllByBandejaByOficinaIdAndStatusIdAndDigitalizadoAndIndEntrega(@Param("oficinaId")Long oficinaId, @Param("statusId") Long statusId,@Param("dig")Boolean dig,@Param("indEntrga")Boolean indEntrga);
	
	List<Prelacion> findAllByOficinaIdAndConsecutivoAndAnioAndSubindice(Long oficinaId, Integer consecutivo, Integer anio, Long subindice);
	
	List<Prelacion> findAllByOficinaIdAndConsecutivoAndAnioAndSubindiceAndClaveConsulta(Long oficinaId, Integer consecutivo, Integer anio, Long subindice, String clave);

	Prelacion findOneByOficinaIdAndConsecutivoAndAnioAndSubindice(Long oficinaId, Integer consecutivo, Integer anio, Long subindice);
	
	List<Prelacion> findAllByOficinaIdAndConsecutivoAndAnio(Long oficinaId, Integer consecutivo, Integer anio);
	
	List<Prelacion> findAllByOficinaIdAndConsecutivoAndAnioAndClaveConsulta(Long oficinaId, Integer consecutivo, Integer anio, String clave);
	
	@Query(value="SELECT p FROM Prelacion p where p.consecutivo=:entrada AND p.anio=:anio And p.subindice=:subIndice and p.oficina.id=:oficinaId")
	Prelacion findByConsecutivoAndAnioAndSubIndiceAndOficinaId(@Param("entrada")Integer entrada, @Param("anio") Integer anio, @Param("subIndice")Long subIndice, @Param("oficinaId")Long oficinaId);
	
	@Query(value="SELECT pp.prelacion FROM PrelacionPredio pp WHERE pp.predio =(SELECT p2 FROM Predio p2 WHERE p2.noFolio =:noFolio) AND pp.prelacion.status.id in (7,8,21) ORDER BY pp.prelacion.consecutivo ASC")
	public List<Prelacion> findByFolio(@Param("noFolio") Integer noFolio);

}
