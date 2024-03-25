package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.util.Constantes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the ActoPredio entity.
 */
@SuppressWarnings("unused")
public interface ActoPredioRepository extends JpaRepository<ActoPredio,Long>, ActoPredioRepositoryCustom {
	
	public Set<ActoPredio> findByPredioId(Long id);

	public List<ActoPredio> findAllByActo(Acto acto);
	
	public ActoPredio findFirstByActoId(Long actoId);

	@Query(value="select ac.* from ACTO_PREDIO ac "+  
	" join ACTO a  on a.ID=ac.ACTO_ID "+
	" join (select ap.acto_id , max(ap.version) v from ACTO_PREDIO ap join acto a on ap.acto_id=a.id where a.id= :actoId GROUP BY ap.ACTO_ID ) mm  on mm.acto_id =ac.ACTO_ID "+
	" where ac.version=mm.v and a.id= :actoId ", nativeQuery=true)
	public List<ActoPredio> findLastVersionByActo(@Param("actoId") Long actoId);

	public ActoPredio findOneById(Long id);
	
	public Optional<ActoPredio> findOneByActoIdAndVersionAndTipoEntradaId(Long actoId, Integer version, Long tipoEntradaId);

	public Optional<ActoPredio> findOneByActoIdAndVersion(Long actoId, Integer version);
	
	@Query(value="select ac.* from ACTO_PREDIO ac  "+
	" join ACTO a  on a.ID=ac.ACTO_ID  "+
	" join (select ap.acto_id , max(ap.version) v from ACTO_PREDIO ap join acto a on ap.acto_id=a.id where ap.PERSONA_JURIDICA_ID= :predioId  and a.PRELACION_ID=:prelacionId GROUP BY ap.ACTO_ID ) mm  on mm.acto_id =ac.ACTO_ID "+
	" where ac.PERSONA_JURIDICA_ID= :predioId  and a.PRELACION_ID=:prelacionId and ac.version=mm.v", nativeQuery=true)
	public List<ActoPredio> findAllByPersonaJuridicaAndPrelacion(@Param("predioId") Long predioId, @Param("prelacionId") Long prelacionId);

	@Query(value="select ac.* from ACTO_PREDIO ac  "+
	" join ACTO a  on a.ID=ac.ACTO_ID  "+
	" join (select ap.acto_id , max(ap.version) v from ACTO_PREDIO ap join acto a on ap.acto_id=a.id where ap.FOLIO_SECCION_AUXILIAR_ID= :predioId  and a.PRELACION_ID=:prelacionId GROUP BY ap.ACTO_ID ) mm  on mm.acto_id =ac.ACTO_ID "+
	" where ac.FOLIO_SECCION_AUXILIAR_ID= :predioId  and a.PRELACION_ID=:prelacionId and ac.version=mm.v", nativeQuery=true)
	public List<ActoPredio> findAllByFolioSeccionAuxiliarAndPrelacion(@Param("predioId") Long predioId, @Param("prelacionId") Long prelacionId);

	@Query(value="select ac.* from ACTO_PREDIO ac  "+
	" join ACTO a  on a.ID=ac.ACTO_ID  "+
	" join (select ap.acto_id , max(ap.version) v from ACTO_PREDIO ap join acto a on ap.acto_id=a.id where ap.MUEBLE_ID= :predioId  and a.PRELACION_ID=:prelacionId GROUP BY ap.ACTO_ID ) mm  on mm.acto_id =ac.ACTO_ID "+
	" where ac.MUEBLE_ID= :predioId  and a.PRELACION_ID=:prelacionId and ac.version=mm.v", nativeQuery=true)
	public List<ActoPredio> findAllByMuebleAndPrelacion(@Param("predioId") Long predioId, @Param("prelacionId") Long prelacionId);

	@Query(value="select ac.* from ACTO_PREDIO ac  "+
	" join ACTO a  on a.ID=ac.ACTO_ID  "+
	" join (select ap.acto_id , max(ap.version) v from ACTO_PREDIO ap join acto a on ap.acto_id=a.id where ap.PREDIO_ID= :predioId  and a.PRELACION_ID=:prelacionId GROUP BY ap.ACTO_ID ) mm  on mm.acto_id =ac.ACTO_ID "+
	" where ac.PREDIO_ID= :predioId  and a.PRELACION_ID=:prelacionId and ac.version=mm.v", nativeQuery=true)
	public List<ActoPredio> findAllByPredioAndPrelacion(@Param("predioId") Long predioId, @Param("prelacionId") Long prelacionId);
	
	@Query(value="select ac.* from ACTO_PREDIO ac  "+
	" join ACTO a  on a.ID=ac.ACTO_ID  "+
	" join (select acto_id , max(version) v, predio_id from ACTO_PREDIO GROUP BY ACTO_ID, PREDIO_ID ) mm  on mm.acto_id =ac.ACTO_ID and mm.predio_id=ac.PREDIO_ID "+
	" where ac.PREDIO_ID= :predioId  and a.status_acto_id=:statusActoId and ac.version=mm.v", nativeQuery=true)
	public List<ActoPredio> findAllByPredio(@Param("predioId") Long predioId, @Param("statusActoId") Long statusActoId);

	@Query(value="select ac.* from ACTO_PREDIO ac  "+
	" join ACTO a  on a.ID=ac.ACTO_ID  "+
	" join (select ap.acto_id , max(ap.version) v from ACTO_PREDIO ap	join acto a on ap.acto_id=a.id where (ap.PREDIO_ID= :concepto or ap.MUEBLE_ID= :concepto or ap.PERSONA_JURIDICA_ID= :concepto or ap.FOLIO_SECCION_AUXILIAR_ID= :concepto)  and a.PRELACION_ID=:prelacion GROUP BY ap.ACTO_ID ) mm  on mm.acto_id =ac.ACTO_ID "+
	" where (ac.PREDIO_ID= :concepto or ac.MUEBLE_ID= :concepto or ac.PERSONA_JURIDICA_ID= :concepto or "+
	"ac.FOLIO_SECCION_AUXILIAR_ID= :concepto)  and a.PRELACION_ID=:prelacion and ac.version=mm.v", nativeQuery=true)
	public List<ActoPredio> findAllByPrelacionAndConcepto(@Param("prelacion") Long prelacion, @Param("concepto") Long concepto);


	@Query(value="select ac.* from ACTO_PREDIO ac  "+
			" join ACTO a  on a.ID=ac.ACTO_ID  "+
			" join (select ap.acto_id , max(ap.version) v from ACTO_PREDIO ap join acto a on ap.acto_id=a.id where a.PRELACION_ID=:prelacionId and (a.vf is null or a.vf= :vf) GROUP BY ap.ACTO_ID ) mm  on mm.acto_id =ac.ACTO_ID "+
			" where  a.PRELACION_ID=:prelacionId and ac.version=mm.v and (a.vf is null or a.vf= :vf) and (a.clon is null or a.clon = false )", nativeQuery=true)
	public List<ActoPredio> findAllByPrelacionAndVfFalse( @Param("prelacionId") Long prelacionId, @Param("vf") boolean vf);

	@Query(value="select ac.* from ACTO_PREDIO ac  "+
			" join ACTO a  on a.ID=ac.ACTO_ID  "+
			" join (select ap.acto_id , max(ap.version) v from ACTO_PREDIO ap	join acto a on ap.acto_id=a.id where a.PRELACION_ID=:prelacionId and a.vf= :vf GROUP BY ap.ACTO_ID ) mm  on mm.acto_id =ac.ACTO_ID "+
			" where  a.PRELACION_ID=:prelacionId and ac.version=mm.v and a.vf= :vf", nativeQuery=true)
	public List<ActoPredio> findAllByPrelacionAndVfTrue( @Param("prelacionId") Long prelacionId, @Param("vf") boolean vf);
	
	public Optional<ActoPredio> findByActoStatusActoIdAndTipoEntradaIdAndPredioId(Long statusActoId, Long tipoEntradaId, Long predioId);
	
	@Query(value="select ac.* from ACTO_PREDIO ac  "+
			" join ACTO a  on a.ID=ac.ACTO_ID  "+
			" join (select ap.acto_id , max(ap.version) v from ACTO_PREDIO ap	join acto a on ap.acto_id=a.id where a.PRELACION_ID=:prelacionId and ap.PREDIO_ID=:predioId GROUP BY ap.ACTO_ID ) mm  on mm.acto_id =ac.ACTO_ID "+
			" where  a.PRELACION_ID=:prelacionId and ac.version=mm.v  and  ac.PREDIO_ID=:predioId  ", nativeQuery=true)
	public List<ActoPredio> findAllByPrelacionAndPredio( @Param("prelacionId") Long prelacionId,  @Param("predioId") Long predioId);

	@Query(value="select ac.* from ACTO_PREDIO ac  "+
			" join ACTO a  on a.ID=ac.ACTO_ID  "+
			" join (select ap.acto_id , max(ap.version) v from ACTO_PREDIO ap	join acto a on ap.acto_id=a.id where a.PRELACION_ID=:prelacionId and ap.PREDIO_ID IS NULL GROUP BY ap.ACTO_ID ) mm  on mm.acto_id =ac.ACTO_ID "+
			" where  a.PRELACION_ID=:prelacionId and ac.version=mm.v  and  ac.PREDIO_ID IS NULL  ", nativeQuery=true)
	public List<ActoPredio> findAllByPrelacionAndPredioIsNull( @Param("prelacionId") Long prelacionId);

	@Query(value="select * from (SELECT ac.* FROM ACTO_PREDIO ac  join ACTO a  on  a.ID=ac.ACTO_ID  join PRELACION p on p.ID=a.PRELACION_ID where a.ID=:actoId AND p.id=:prelacionId order by  ac.version desc ) q1 limit 1", nativeQuery=true)
	public ActoPredio findByActoAndActoPrelacion(@Param("actoId") Long  actoId, @Param("prelacionId") Long prelacionId );

	@Query(value="select * from (SELECT ac.* FROM ACTO_PREDIO ac  join ACTO a  on  a.ID=ac.ACTO_ID  join PRELACION p on p.ID=a.PRELACION_ID where a.ID=:actoId AND p.id=:prelacionId AND ac.VERSION=:version order by  ac.version desc ) q1 limit 1", nativeQuery=true)
	public ActoPredio findByActoAndActoPrelacion(@Param("actoId") Long  actoId, @Param("prelacionId") Long prelacionId, @Param("version") Long version );

	@Query(value="SELECT ap.version from ActoPredio ap join ap.acto a  " + 
			"WHERE ap.version = (SELECT MAX(ap2.version) FROM ActoPredio ap2 join ap2.acto a2 WHERE a.id = a2.id) and  ap.acto.id = :actoId" )
	public Long findByLastVersion(@Param("actoId") Long  actoId);
	
	public List<ActoPredio> findByActoId(Long actoId);

	@Query("select a from ActoPredio ap inner join ap.acto a where ap.predio.id = :predioId and a.tipoActo.id in (:tiposActo) and a.statusActo.id = 1")
	public Set<Acto> findActosPredioByPredioQuery(@Param("predioId") Long predioId, @Param("tiposActo") Long[] tiposActo);
	
	@Query("select a from ActoPredio ap inner join ap.acto a where ap.predio.id = :predioId and a.statusActo.id in (:status) and a.tipoActo is not null order by ap.id desc")
	public Set<Acto> findActosPredioByPredioAndStatusQuery(@Param("predioId") Long predioId, @Param("status") Long[] status);

	@Query("select a from ActoPredio ap inner join ap.acto a where ap.personaJuridica.id = :pjId and a.statusActo.id in (:status) and a.tipoActo is not null order by ap.id desc")
	public Set<Acto> findActosPredioByPersonaJuridicaAndStatusQuery(@Param("pjId") Long pjId, @Param("status") Long[] status);

	
	@Query("select a from ActoPredio ap inner join ap.acto a where ap.predio.id = :predioId and a.tipoActo.id in (:tiposActo) and a.statusActo.id in (:status)")
	public Set<Acto> findAllActosPredioByPredioAndStatusQuery(@Param("predioId") Long predioId, @Param("tiposActo") Long[] tiposActo, @Param("status") Long[] status);

	@Query("select a from ActoPredio ap inner join ap.acto a where ap.predio.id = :predioId  and a.statusActo.id = 1 and ap.tipoEntrada.id = 2 and (a.copiadoModificado is null or a.copiadoModificado = false)")
	public Set<Acto> findActosTitulares(@Param("predioId") Long predioId);

	@Query("select pp from Acto a inner join a.actoPrediosParaActos ap inner join ap.predioPersonasParaActoPredios pp inner join ap.predio p where p.id = :predioId and a.statusActo.id = 1  ")
	public Set<PredioPersona> findPersonasbyPredioIdQuery(@Param("predioId") Long predioId);

	@Query("select doc from Acto a inner join a.actoPrediosParaActos ap " +
			"inner join ap.predioPersonasParaActoPredios pp " +
			"inner join a.actoDocumentosParaActos doc " +
			"inner join ap.predio p " +
			"where p.id = :predioId and a.statusActo.id = 1  ")
	
	public Set<ActoDocumento> findEscrituraByPredioIdQuery(@Param("predioId") Long predioId);

	public List<ActoPredio> findAllByActoPrelacionIdAndPredioIdAndActoVf(Long prelacionId, Long predioId, boolean vf);

	public List<ActoPredio> findAllByActoPrelacionIdAndPersonaJuridicaIdAndActoVf(Long prelacionId, Long predioId, boolean vf);
	
	public List<ActoPredio> findAllByActoPrelacionIdAndFolioSeccionAuxiliarIdAndActoVf(Long prelacionId, Long predioId, boolean vf);
	
	public List<ActoPredio> findAllByActoPrelacionIdAndMuebleIdAndActoVf(Long prelacionId, Long predioId, boolean vf);
	
	// consulta de prelacion por acto //
	
	@Query(value="SELECT ap FROM ActoPredio ap join fetch ap.acto a join fetch a.prelacion "+
			 " where ap.predio.id = :predioId "+
			 " and ap.acto.statusActo.id = 1"+
			 " and ap.acto.tipoActo.clasifActo.id = 2"+
			 //" and ap.acto.vf = false"+
			 " ORDER BY ap.acto.prelacion.consecutivo")
	
	public List<ActoPredio> getAllGravamenesByPredioId(@Param("predioId") Long predioId);
	
	@Query(value="SELECT count(ap) FROM ActoPredio ap "+
			 " where ap.predio.id = :predioId "+
			 " and ap.acto.statusActo.id = 1"+
			 " and ap.acto.tipoActo.clasifActo.id = 2")
			// " ORDER BY ap.acto.prelacion.consecutivo")//
	
	public long countAllGravamenesByPredioId(@Param("predioId") Long predioId);
	
	@Query(value="SELECT ap FROM ActoPredio ap "+
			 " where ap.mueble.id = :muebleId "+
			 " and ap.acto.statusActo.id = 1"+
			 " and ap.acto.tipoActo.clasifActo.id = 2"+
			 " ORDER BY ap.acto.prelacion.consecutivo")
	
	public List<ActoPredio> getAllGravamenesByMuebleId(@Param("muebleId") Long muebleId);

	@Query(value="SELECT ap FROM ActoPredio ap "+
			 " where ap.personaJuridica.id = :personaJuridicaId "+
			 " and ap.acto.statusActo.id = 1"+
			 " and ap.acto.tipoActo.clasifActo.id = 2"+
			 " ORDER BY ap.acto.prelacion.consecutivo")
	
	public List<ActoPredio> getAllGravamenesByPersonaJuridicaId(@Param("personaJuridicaId") Long personaJuridicaId);

	public ActoPredio findAllByPredio(Predio predio);
	
	@Query(value="SELECT ap FROM ActoPredio ap where ap.acto in(:listA)")
	public List<ActoPredio> findAllByListActo(@Param("listA")List<Acto> listA);
	
	
	@Query(value="SELECT * FROM ACTO_PREDIO ac where ac.ACTO_ID=:actoId "
			+ " AND ac.VERSION = (SELECT MAX(ap.VERSION) from ACTO_PREDIO ap where ap.ACTO_ID = :actoId )order by id desc limit 1", nativeQuery=true)
	public ActoPredio findActoPredioByLastVersion(@Param("actoId") Long  actoId);
	
	@Query(value=" SELECT ap.* " + 
			" FROM acto_predio ap,acto a" +
			" WHERE ap.predio_id=:predioId " +
			" AND a.id=ap.acto_id" +
			" AND ( a.status_acto_id = 1 )" + 
			" ORDER BY acto_id", nativeQuery=true)
	public List<ActoPredio> findActopredioByPrelacionId(@Param("predioId") Long predioId);

	@Query("SELECT ap from ActoPredio ap  " +
			" join ap.acto a WHERE ap.version = (SELECT MAX(ap2.version) FROM ActoPredio ap2 join ap2.acto a2 WHERE a.id = a2.id) "
			+ "and  ap.acto.prelacion.id = :prelacionId and ap.predio.id = :predioId ")
	public List<ActoPredio> findAllMaxVersionByActoPrelacionIdAndPredioId(@Param("prelacionId") Long prelacionId, @Param("predioId") Long predioId);
	
	public Optional<ActoPredio> findFirstByActoIdOrderByVersionDesc(Long actoId);
	public Optional<ActoPredio> findFirstByActoIdAndTipoEntradaIdOrderByVersionDesc(Long actoId,Long tipoEntradaId);
	
	@Query("SELECT ap FROM ActoPredio ap join fetch ap.acto a where a.tipoActo.id in (49,210,124,50) and a.statusActo.id = 1 and ap.predio.id = :predioId")
	public List<ActoPredio> findAvisosVigentes(@Param("predioId") Long predioId);

}

