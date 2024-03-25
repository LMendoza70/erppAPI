package com.gisnet.erpp.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.ActoPredio;

/**
 * Spring Data JPA repository for the PersonaJuridica entity.
 */
@SuppressWarnings("unused")
public interface PjPersonaRepository extends JpaRepository<PjPersona,Long>{

	@Query(value="select count(modulo_campo_id) from acto_predio_campo where acto_predio_id = any("+
	"select distinct(ap.id) from pj_persona pjp, acto_predio ap, acto a, persona_juridica pj,( "+
	"select a.ID as actoId, max(ap.VERSION) as version from acto_predio ap, acto a "+
	"where ap.ACTO_ID = a.ID "+
	"	and a.STATUS_ACTO_ID = 1 "+
	"		group by a.ID "+
	"		) q1 "+
	"	where pjp.ACTO_PREDIO_ID = ap.id "+
	"	and ap.ACTO_ID = a.id "+
	"	and a.STATUS_ACTO_ID = 1 "+
	"	and a.id = q1.actoId "+
	"	and ap.VERSION = q1.version "+
	"	and ap.PERSONA_JURIDICA_ID = pj.id "+
	"	and pj.id =:pjId "+
	"	group by ap.id)and modulo_campo_id in(440);", nativeQuery=true)
	public List<Object[]> findApoderadoCountByPjId(@Param("pjId") Long pjId);
	
	@Query(value="select count(modulo_campo_id) from acto_predio_campo where acto_predio_id = any("+
	"select distinct(ap.id) from pj_persona pjp, acto_predio ap, acto a, persona_juridica pj,( "+
	"select a.ID as actoId, max(ap.VERSION) as version from acto_predio ap, acto a "+
	"where ap.ACTO_ID = a.ID "+
	"	and a.STATUS_ACTO_ID = 1 "+
	"		group by a.ID "+
	"		) q1 "+
	"	where pjp.ACTO_PREDIO_ID = ap.id "+
	"	and ap.ACTO_ID = a.id "+
	"	and a.STATUS_ACTO_ID = 1 "+
	"	and a.id = q1.actoId "+
	"	and ap.VERSION = q1.version "+
	"	and ap.PERSONA_JURIDICA_ID = pj.id "+
	"	and pj.id =:pjId "+
	"	group by ap.id)and modulo_campo_id in(1203);", nativeQuery=true)
	public List<Object[]> findOrganoDeAdministracionCountByPjId(@Param("pjId") Long pjId);


	@Query(value="select count(modulo_campo_id) from acto_predio_campo where acto_predio_id = any("+
	"select distinct(ap.id) from pj_persona pjp, acto_predio ap, acto a, persona_juridica pj,( "+
	"select a.ID as actoId, max(ap.VERSION) as version from acto_predio ap, acto a "+
	"where ap.ACTO_ID = a.ID "+
	"	and a.STATUS_ACTO_ID = 1 "+
	"		group by a.ID "+
	"		) q1 "+
	"	where pjp.ACTO_PREDIO_ID = ap.id "+
	"	and ap.ACTO_ID = a.id "+
	"	and a.STATUS_ACTO_ID = 1 "+
	"	and a.id = q1.actoId "+
	"	and ap.VERSION = q1.version "+
	"	and ap.PERSONA_JURIDICA_ID = pj.id "+
	"	and pj.id =:pjId "+
	"	group by ap.id)and modulo_campo_id in(631);", nativeQuery=true)
	public List<Object[]> findSociosCountByPjId(@Param("pjId") Long pjId);
	
	@Query(value="select pjp.* from pj_persona pjp, acto_predio ap, acto a, persona_juridica pj, "
			+ "( "
			+ "select a.ID as actoId, max(ap.VERSION) as version from acto_predio ap, acto a "
			+ "where ap.ACTO_ID = a.ID "
			+ "and a.STATUS_ACTO_ID = 1 "
			+ "group by a.ID "
			+ ") q1 "
			+ "where pjp.ACTO_PREDIO_ID = ap.id "
			+ "and ap.ACTO_ID = a.id "
			+ "and a.STATUS_ACTO_ID = 1 "
			+ "and a.id = q1.actoId "
			+ "and ap.VERSION = q1.version "
			+ "and ap.PERSONA_JURIDICA_ID = pj.id "
			+ "and pj.id = :pjId "
			+ "and pjp.tipo_rel_persona_id=4", nativeQuery=true)
	public List<PjPersona> findSociosByPjId(@Param("pjId") Long pjId);
    @Query("select pj from Acto a inner join a.actoPrediosParaActos ap inner join ap.personaJuridicaPersonasParaActoPredios pj where a.prelacion.id = :prelacionId and a.statusActo.id = 1  ")
	Set<PjPersona> findAllByPrelacionId(@Param("prelacionId") Long prelacionId);
	
	List<PjPersona> findAllByActoPredio(ActoPredio ap);
	
    @EntityGraph(attributePaths = {"persona", "tipoRelPersona"})
    List<PjPersona> findAllByActoPredioIdIn(List<Long> ids);
    
    Long deleteByActoPredioId(Long actoPredioId);
}
