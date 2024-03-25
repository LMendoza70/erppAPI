package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.Predio;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the PrelacionPredio entity.
 */
@SuppressWarnings("unused")
public interface PrelacionPredioRepository extends JpaRepository<PrelacionPredio,Long> {


    List<PrelacionPredio>  findByPrelacionIdOrderByIdAsc(Long id);

    List<PrelacionPredio>  findByPrelacionIdAndEstatus(Long id,int estatus);
    
    @Query("select pp from PrelacionPredio pp join pp.prelacion p join p.actosParaPrelacions a where a.id = :actoId")
	List<PrelacionPredio>  findByActoId(@Param("actoId") Long actoId);
    
    List<PrelacionPredio>  findByPrelacionIdAndPredioStatusActoId(Long prelacionId, Long statusActoId);
    
    List<PrelacionPredio>  findByPrelacionIdAndTipoFolioId(Long id,Long tipo);

    PrelacionPredio findOneByPrelacionIdAndPredioId (long prelacionId, long predioId);
    PrelacionPredio findFirstByPrelacionIdAndPredioId (long prelacionId, long predioId);
    PrelacionPredio findByPrelacionIdAndPersonaJuridicaId(long prelacionId, long predioId);
    PrelacionPredio findByPrelacionIdAndFolioSeccionAuxiliarId(long prelacionId, long predioId);
    PrelacionPredio findByPrelacionIdAndMuebleId(long prelacionId, long predioId);

    PrelacionPredio findByPrelacionIdAndId(long prelacionId, long prelacionPredioId);

    List<PrelacionPredio> findByIdVersion(String idVersion);
    
    @Query(value="SELECT p FROM PrelacionPredio pp JOIN  pp.prelacion p WHERE pp.predio.id = :predioId and p.id not in (:prelacionId) AND p.status.id in(2,3,4,5,6,15,16) ")
    List<Prelacion>   findPrelacionEnProcesoByPredioId(@Param("predioId") Long predioId,@Param("prelacionId") Long prelacionId);

    @Query(value="SELECT p.* FROM PRELACION_PREDIO p  WHERE p.ID_VERSION= :idVersion AND ESTATUS=1  ORDER BY VERSION DESC",nativeQuery=true)
    List<PrelacionPredio> findByVersionId(@Param("idVersion") String idVersion);

    @Query(value="SELECT a.* FROM PRELACION_PREDIO a WHERE a.PRELACION_ID= :prelacionId AND ESTATUS=1 ORDER BY a.PREDIO_ID",nativeQuery=true)
    List<PrelacionPredio>  findByPrelacionIdAndVersionId(@Param("prelacionId") Long prelacionId);

    @Query(value="SELECT a.* FROM PRELACION_PREDIO a INNER JOIN PERSONA_JURIDICA pjur ON a.PERSONA_JURIDICA_ID = pjur.ID INNER JOIN PRELACION pprel  ON a.PRELACION_ID = pprel.ID WHERE  a.PERSONA_JURIDICA_ID = :personaJuridicaId AND pprel.STATUS_ID NOT IN  (7, 8 , 10)  ",nativeQuery=true)
    List<PrelacionPredio>  findByFolioLibrePersonaJuridica(@Param("personaJuridicaId") Long personaJuridicaId);
    
    @Query(value="SELECT a.* FROM PRELACION_PREDIO a INNER JOIN folio_seccion_auxiliar paux ON a.FOLIO_SECCION_AUXILIAR_ID = paux.ID INNER JOIN PRELACION pprel  ON a.PRELACION_ID = pprel.ID WHERE  a.FOLIO_SECCION_AUXILIAR_ID = :auxiliarId AND pprel.STATUS_ID NOT IN  (7, 8 , 10)  ",nativeQuery=true)
    List<PrelacionPredio>  findByFolioLibreAuxiliar(@Param("auxiliarId") Long auxiliarId);
    
    @Query(value="SELECT a.* FROM PRELACION_PREDIO a INNER JOIN MUEBLE pmub ON a.MUEBLE_ID = pmub.ID INNER JOIN PRELACION pprel  ON a.PRELACION_ID = pprel.ID WHERE  a.MUEBLE_ID = :muebleId AND pprel.STATUS_ID NOT IN  (7, 8 , 10)  ",nativeQuery=true)
    List<PrelacionPredio>  findByFolioLibreMueble(@Param("muebleId") Long muebleId);
    
    @Query(value="SELECT a.* FROM PRELACION_PREDIO a INNER JOIN PREDIO ppred ON a.PREDIO_ID = ppred.ID INNER JOIN PRELACION pprel  ON a.PRELACION_ID = pprel.ID WHERE  a.PREDIO_ID = :predioId AND pprel.STATUS_ID IN  (2, 3, 4, 5, 6)  ",nativeQuery=true)
    List<PrelacionPredio>  findByFolioLibrePredio(@Param("predioId") Long predioId);

    @Query(value="select pp from PrelacionPredio pp where pp.predio.id=:predioId and pp.tipoFolio.id=:idTF")
    List<PrelacionPredio> findPrelacionPredioByPredio(@Param("predioId") Long predioId, @Param("idTF") Long idTF);

    @Query(value="select pp from PrelacionPredio pp where pp.predio.id=:predioId")
    List<PrelacionPredio> findPrelacionPredioByPredioId(@Param("predioId") Long predioId);
    
    void deleteByPrelacionId(Long prelacionId);
    
    List<PrelacionPredio> findAllByPrelacionConsecutivoLessThanAndPrelacionAnioAndPrelacionStatusIdInAndPredioIdIn(Integer consecutivo,Integer anio, List<Long> statusIdList, List<Long> prediosIdList);
    
    List<PrelacionPredio> findAllByPrelacionAnioLessThanAndPrelacionStatusIdInAndPredioIdIn(Integer anio, List<Long> statusIdList, List<Long> prediosIdList);

    @Query(value="SELECT Pp.predio.noFolio FROM PrelacionPredio Pp where Pp.prelacion.id =:prelacionId") 
    public Long  findPredioIdByPrelacionId(@Param("prelacionId") Long  prelacionId);
}
