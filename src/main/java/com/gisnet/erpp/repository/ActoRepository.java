package com.gisnet.erpp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ClasifActo;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.util.Constantes.ETipoFolio;
import com.gisnet.erpp.util.Constantes.StatusActo;

/**
 * Spring Data JPA repository for the Acto entity.
 */
@SuppressWarnings("unused")
public interface ActoRepository extends JpaRepository<Acto,Long>, ActoRepositoryCustom {
	public abstract List<Acto> findByPrelacionId(Long id);

	 @Query(value="SELECT a FROM Acto a where a.prelacion.id = :prelacionId  and (a.vf= :vf or a.vf is null) and (a.clon is null or a.clon = false)  order by a.orden asc ")
	public abstract List<Acto> findAllByPrelacionIdAndVfFalseOrderByOrdenAsc(@Param("prelacionId") Long prelacionId, @Param("vf") boolean vf );

	public Acto findOneById(Long id);

	public List<Acto> findAllByPrelacion(Prelacion prelacion);

	public List<Acto> findAllByPrelacionIdOrderByOrdenAsc(Long id);
	
	public List<Acto> findAllByPrelacionIdAndVfTrueOrderByOrdenAsc(Long id);
	
	@Query(value=" select max(orden) from acto where prelacion_id=:prelacion ",nativeQuery=true)
	public Integer maxByPrelacion(@Param("prelacion") Long prelacion);
	
	@Query(value=" select max(a.orden) from Acto a where a.prelacion.id=:prelacion and a.vf= :vf ")
	public Integer maxByPrelacionAndVf(@Param("prelacion") Long prelacion, @Param("vf") boolean vf);


	@Query("SELECT a FROM Acto a join a.actoPrediosParaActos ap where ap.predio.id= :predioId and a.tipoActo.clasifActo.id= :clasifActoId and a.statusActo.id= :statusActoId and a.fechaRegistro= (select max(a1.fechaRegistro) from Acto a1 join a1.actoPrediosParaActos ap1 where ap1.predio.id= :predioId and a1.tipoActo.clasifActo.id= :clasifActoId and a1.statusActo.id= :statusActoId)  ")
	public Acto findUltimoTraslativoRegistradoByPredioId(@Param("predioId") Long predioId, @Param("clasifActoId") Long clasifActoId, @Param("statusActoId") Long statusActoId);

	@Query(value=" select ta.NOMBRE, count(a.id),a.id from acto a, acto_predio ap, tipo_acto ta, "+
			     " ( select ap.ACTO_ID as actoId, max(ap.version) as version from acto_predio ap group by ap.ACTO_ID ) q1 "+
			     " where a.id = ap.ACTO_ID "+
			     " and ta.id = a.TIPO_ACTO_ID "+
			     " and q1.actoId = a.id "+
			     " and q1.version = ap.version "+
			     " and a.STATUS_ACTO_ID = :statusActoId "+
			     " and ap.PREDIO_ID = :predioId "+
			     //" and ta.ACTIVO = true "+
			     " and ta.id IN (9,14,25,27,28,31,32,34,35,36,37,38,48,55,64,95,96,97,98,99,115,116,117,119,121,217,219,227,230,234,223) "+
			     " group by ta.nombre,a.id "+
			     " order by a.id ASC",nativeQuery=true)
	public List<Object[]> countGravamenesByPredioId(@Param("statusActoId") Long statusActoId,@Param("predioId") Long predioId);

	@Query(value=" select ta.NOMBRE, count(a.id), a.id,pre.consecutivo from acto a, acto_predio ap, tipo_acto ta,prelacion pre, "+
		     " ( select ap.ACTO_ID as actoId, max(ap.version) as version from acto_predio ap group by ap.ACTO_ID ) q1 "+
		     " where a.id = ap.ACTO_ID "+
		     " and a.prelacion_id = pre.id "+
		     " and ta.id = a.TIPO_ACTO_ID "+
		     " and q1.actoId = a.id "+
		     " and q1.version = ap.version "+
		     " and a.STATUS_ACTO_ID = :statusActoId "+
		     " and ap.PREDIO_ID = :predioId "+
		     //" and ta.ACTIVO = true "+
		     " and ta.id IN (49,50,45,47,53,56,58,60,61,66,118,233,210,220,228,243,247,1231,248,43,70) "+
		     " group by ta.nombre,a.id,pre.consecutivo "+
		     " order by ta.nombre ",nativeQuery=true)
	public List<Object[]> countLimitacionesByPredioId(@Param("statusActoId") Long statusActoId,@Param("predioId") Long predioId);

	@Query("SELECT a FROM Acto a join fetch a.actoPrediosParaActos ap where a.statusActo.id= :statusActoId and ap.predio.id= :predioId and a.tipoActo.id in (49,50)  ")
	public List<Acto> findActivosByStatusActoAndClasifActoAndSubClasificacionAndPredioId(@Param("statusActoId") Long statusActoId, @Param("predioId") Long predioId);
	
	
	@Query("SELECT a FROM Acto a join fetch a.actoPrediosParaActos ap where a.statusActo.id= :statusActoId and ap.predio.id= :predioId ")
	public List<Acto> findActivosByStatusActoAndPredioId(@Param("statusActoId") Long statusActoId, @Param("predioId") Long predioId);

	@Query("SELECT a FROM Acto a join fetch a.actoPrediosParaActos ap join fetch a.prelacion p where a.statusActo.id in (:statusActoId) and ap.predio.id= :predioId order by a.id desc")
	public List<Acto> findByStatusActoAndPredioId(@Param("statusActoId") List<Long> statusActoId, @Param("predioId") Long predioId);
	
    public List<Acto>  findDistinctByStatusActoIdAndActoPrediosParaActosPredioIdAndTipoActoClasifActoIn(Long statusActoId, Long predioId, List<ClasifActo> clasifActo );
    
    public List<Acto>  findDistinctByActoRelesParaActosActoSigId(Long actoId);
    
    public List<Acto>  findByActoRelesParaActoSiguientesActoIn(List<Acto> actosSource);
    
    public List<Acto>  findDistinctByStatusActoIdAndActoPrediosParaActosActoPredioMontosParaActoPrediosActoIdAndTipoActoClasifActoId(Long statusActoId, Long actoId, Long clasifActoId );


		@Query(value="SELECT ac.* FROM ACTO ac WHERE id IN(SELECT a.ID FROM (SELECT DISTINCT ap.* FROM PREDIO_PERSONA pp INNER JOIN ACTO_PREDIO ap ON pp.ACTO_PREDIO_ID=ap.ID ) a WHERE a.VERSION=(SELECT max(b.VERSION) FROM (SELECT DISTINCT ap.* FROM PREDIO_PERSONA pp INNER JOIN ACTO_PREDIO ap ON pp.ACTO_PREDIO_ID=ap.ID ) b WHERE a.ACTO_ID=b.ACTO_ID AND a.PREDIO_ID=b.PREDIO_ID) AND a.PERSONA_JURIDICA_ID= :personaJuridicaId)",nativeQuery=true)
    List<Acto> findActosHerenciaPJ(@Param("personaJuridicaId") Long personaJuridicaId);

    @Query(value="SELECT ac.* FROM ACTO ac WHERE id IN(SELECT a.ID FROM (SELECT DISTINCT ap.* FROM PREDIO_PERSONA pp INNER JOIN ACTO_PREDIO ap ON pp.ACTO_PREDIO_ID=ap.ID ) a WHERE a.VERSION=(SELECT max(b.VERSION) FROM (SELECT DISTINCT ap.* FROM PREDIO_PERSONA pp INNER JOIN ACTO_PREDIO ap ON pp.ACTO_PREDIO_ID=ap.ID ) b WHERE a.ACTO_ID=b.ACTO_ID AND a.PREDIO_ID=b.PREDIO_ID) AND a.FOLIO_SECCION_AUXILIAR_ID= :folioSeccionAuxiliarId)",nativeQuery=true)
    List<Acto> findActosHerenciaPA(@Param("folioSeccionAuxiliarId") Long folioSeccionAuxiliarId);

    @Query(value="SELECT ac.* FROM ACTO ac WHERE id IN(SELECT a.ID FROM (SELECT DISTINCT ap.* FROM PREDIO_PERSONA pp INNER JOIN ACTO_PREDIO ap ON pp.ACTO_PREDIO_ID=ap.ID ) a WHERE a.VERSION=(SELECT max(b.VERSION) FROM (SELECT DISTINCT ap.* FROM PREDIO_PERSONA pp INNER JOIN ACTO_PREDIO ap ON pp.ACTO_PREDIO_ID=ap.ID ) b WHERE a.ACTO_ID=b.ACTO_ID AND a.PREDIO_ID=b.PREDIO_ID) AND a.MUEBLE_ID= :muebleId)",nativeQuery=true)
    List<Acto> findActosHerenciaM(@Param("muebleId") Long muebleId);


    public List<Acto> findDistinctByActoPrediosParaActosPredioIdAndPrelacionId(Long idPredio, Long prelacionId);

    public List<Acto> findByActoPrediosParaActosPredioId (Long idPredio);

    public List<Acto> findByActoPrediosParaActosMuebleId(Long idMueble);
    
    public List<Acto> findByActoPrediosParaActosPersonaJuridicaId(Long idPj);
    
    public List<Acto> findByActoPrediosParaActosFolioSeccionAuxiliarId(Long idSeccionAuxiliar);

    @Query(value="SELECT ac.* FROM ACTO ac WHERE id IN(SELECT a.ID FROM (SELECT DISTINCT ap.* FROM PREDIO_PERSONA pp INNER JOIN ACTO_PREDIO ap ON pp.ACTO_PREDIO_ID=ap.ID ) a WHERE a.VERSION=(SELECT max(b.VERSION) FROM (SELECT DISTINCT ap.* FROM PREDIO_PERSONA pp INNER JOIN ACTO_PREDIO ap ON pp.ACTO_PREDIO_ID=ap.ID ) b WHERE a.ACTO_ID=b.ACTO_ID AND a.PREDIO_ID=b.PREDIO_ID) AND a.PREDIO_ID= :predioId)",nativeQuery=true)
    List<Acto> findActosHerenciaP(@Param("predioId") Long predioId);

	@Query(value="SELECT a FROM Acto a "+
			 " where a.prelacion.id = :prelacionId "+
			 " and a.motivo.id is not null")
	public List<Acto> getAllActoRechazoByActoPrelacionId(@Param("prelacionId") Long id);
	
	@Modifying
	@Query("update Acto o set o.fechaRegistro= :fechaRegistro where id= :actoId ")
	public void updateFechaRegistro(@Param("actoId") Long actoId, @Param("fechaRegistro") Date fechaRegistro);

	@Query(value="SELECT a FROM Acto a where a.prelacion in(:listPrel2)")
	public List<Acto> findAllByListPrelacion(@Param("listPrel2")List<Prelacion> listPrel2);

	/* @Query(value="SELECT a.prelacion.id FROM Acto a where a.id =:ActoId") 
	 public Long  findPrelacionIdByActoId(@Param("ActoId") Long  ActoId);*/
	
	@Query(value="SELECT a FROM Acto a where a.prelacion.id =:prelacionId and (a.vf is null or vf=false) order by a.orden") 
	public Acto[] findActosByPrelacionId(@Param("prelacionId") Long  prelacionId);
	
	@Query(value="SELECT a FROM Acto a where a.prelacion.id =:prelacionId and a.statusActo.id =  5 and (a.vf is null or a.vf  = false) and (a.clon is null or a.clon = false)") 
	public List<Acto> findActosByPrelacionIdAndRechazados(@Param("prelacionId") Long  prelacionId);
	
	/*@Query(value="SELECT MAX(a.version) FROM Acto a WHERE a.prelacion.id =:prelacionId")
	public Long findActoVersionByPrelacionId(@Param("prelacionId") Long  prelacionId);*/
	
	/*@Query(value="SELECT a FROM Acto a where a.version =:version AND a.prelacion.id =:prelacionId")
	public Acto findActoByPrelacion(@Param("prelacionId") Long  prelacionId,@Param("version") Long version );*/
	
	@Query(value="SELECT a.* FROM Acto a where a.version =(SELECT MAX(a.version) FROM Acto a WHERE a.PRELACION_ID =:prelacionId) AND  a.PRELACION_ID =:prelacionId ", nativeQuery=true)
	public Acto findActoByPrelacion(@Param("prelacionId") Long  prelacionId);
	
	public Optional<Acto> findById(Long id);
	
	List<Acto> findAllByPrelacionId(Long prelacionid);
	
	@Modifying
	@Query(value="delete from Acto a where a.id = :actoId")
	public void hardDelete(@Param("actoId") Long actoId);
	
	@Modifying
	@Query(value="delete from Acto a where a = :acto")
	public void hardDelete(@Param("acto") Acto acto);
	
	@Query(value="SELECT a FROM Acto a WHERE ID IN (SELECT ap.acto.id FROM ActoPredio ap WHERE predio =:predio) AND statusActo=1")
	public List<Acto> findAllByPredioAndStatus(@Param("predio") Predio predio);

	@Query(value="SELECT a FROM Acto a where extract(year from a.fechaRechazo) =:a and extract(month from a.fechaRechazo) =:m and extract(day from a.fechaRechazo) =:d and a.prelacion.oficina.id=:oficinaId and a.statusActo.id=5 and a.prelacion.status.id in (7,8,10)")
	public List<Acto> findAllByActoFechaRechazo(@Param("a") Integer a,@Param("m") Integer m,@Param("d") Integer d, @Param("oficinaId") Long oficinaId);
	
	@Query(value="select a from Acto a where  a.tipoActo.id =:id") 
	public List<Acto> findAllByTipoActo(@Param("id")  Long id);


	public List<Acto>  findDistinctByStatusActoIdAndActoPrediosParaActosPredioIdAndTipoActoClasifActoId(Long statusActoId, Long predioId, Long clasifActoId );

	@Query(value="select a from Acto a where a.prelacion.id = :prelacionId  and  a.tipoActo.id =:tipoActoId and a.statusActo.id not in (5)") 
	public List<Acto> findAllByPrelacionIdAndTipoActoId(@Param("prelacionId") Long prelacionId,@Param("tipoActoId") Long tipoActoId);
	
/*
@Query(value="SELECT a FROM Acto a WHERE extract(year from a.fecha_rechazo) =:anio and extract(month from a.fecha_rechazo) =:mes and extract(day from a.fecha_rechazo) =:dia")
	public List<Acto> findAllByActoFechaRechazo(@Param("anio") String anio,@Param("mes") String mes,@Param("dia") String dia);

	@Query("update Acto o set o.fechaRegistro= :fechaRegistro where id= :actoId ")
	public void updateFechaRegistro(@Param("actoId") Long actoId, @Param("fechaRegistro") Date fechaRegistro);

	select * from acto where extract(year from acto.fecha_rechazo) = '2018' 
and extract(month from acto.fecha_rechazo)='12'
and extract(day from acto.fecha_rechazo)='27';
	*/

	@Query(value="SELECT a FROM Acto a where a.prelacion.id in(SELECT pp.prelacion.id FROM PrelacionPredio pp WHERE pp.predio =(SELECT p2 FROM Predio p2 WHERE p2.noFolio =:noFolio))")
	public List<Acto> findByFolio(@Param("noFolio") Integer noFolio);
	
	@Query(value="select fn_de_materializa_modificatorios_all(:actoId)",nativeQuery=true)
	public Long deMaterializarModificatorio(@Param("actoId") Long actoId);
	
	@Query(value="select fn_de_materializa_fraccion(:predioId)",nativeQuery=true)
	public Long deMaterializarFraccion(@Param("predioId") Long predioId);
	
	@Query(value="select fn_materializa_fraccionamiento(:actoId)",nativeQuery=true)
	public Long materializaModificatorio(@Param("actoId") Long actoId);
	
	@Query(value="SELECT * FROM Acto a where prelacion_id = :prelacionId  and (a.vf is null or vf=false) and (a.clon is null or a.clon = false) " , nativeQuery=true)
	public List<Acto> findAllActosByPrelacionVf(@Param("prelacionId") Long prelacionId);

	public Optional<Acto> findAllByPrelacionIdAndTipoActoIdIn(Long prelacionid,List<Long>tiposActo);
}
