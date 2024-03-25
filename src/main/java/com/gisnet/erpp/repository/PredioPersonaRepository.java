package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PredioPersona;

import org.hibernate.annotations.Target;
import org.hibernate.validator.internal.xml.ElementType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the PredioPersona entity.
 */
@SuppressWarnings("unused")
public interface PredioPersonaRepository extends JpaRepository<PredioPersona,Long> {

    List<PredioPersona> findAllByPersonaId(Long Id);

    @Query("select pp from Acto a inner join a.actoPrediosParaActos ap inner join ap.predioPersonasParaActoPredios pp inner join ap.predio p where p.id = :predioId and a.prelacion.id = :prelacionId and a.statusActo.id = 1 and pp.primerRegistro != 1 ")
    Set<PredioPersona> findAllByPrelacionIdAndPredioId(@Param("predioId")  Long predioId,@Param("prelacionId") Long prelacionId);
    List<PredioPersona> findAllByPersonaIdInAndActoPredioActoStatusActoIdAndActoPredioActoPrelacionOficinaId(List<Long> Ids,Long status,Long oficinaId);
    @Query(value="select pp.* from predio_persona pp"
    +" join acto_predio ap on pp.acto_predio_id=ap.id"
    +" join acto a on ap.acto_id=a.id"
    +" join predio p on ap.predio_id=p.id"
    +" join predio_ante pa on p.id=pa.predio_id"
    +" join antecedente an on pa.antecedente_id=an.id"
    +" join libro l on an.libro_id=l.id"
    +" join secciones_x_oficina sxo on l.secciones_por_oficina_id=sxo.id"
    +" where "
    +" pp.persona_id in (:personas) and"
    +" a.status_acto_id=:statusActo and sxo.oficina_id=:oficinaId and p.status_acto_id=3" 
    +" and p.oficina_id is null and p.no_folio is null", nativeQuery=true)
    List<PredioPersona> findAllFoliosPrecaptura(@Param("personas") List<Long> Ids,@Param("statusActo") Long status,@Param("oficinaId") Long oficinaId);    
    List<PredioPersona> findByActoPredioPredioIdAndActoPredioActoStatusActoId(Long predio,Long status);
    List<PredioPersona> findByActoPredioPredioIdInAndActoPredioActoStatusActoId(List<Long> predios,Long status);
    List<PredioPersona> findByActoPredioPredioNoFolioAndActoPredioActoStatusActoId(Integer noFolio,Long status);

    
    @EntityGraph(attributePaths = {"persona", "tipoRelPersona"})
    List<PredioPersona> findAllByActoPredioIdIn(List<Long> ids);
    
    List<PredioPersona> findAllByActoPredioId(Long id);
    
    
    Long deleteByActoPredioId(Long actoPredioId);
   
     

}
