package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ActoRequisito;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Requisito;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ActoRequisito entity.
 */
@SuppressWarnings("unused")
public interface ActoRequisitoRepository extends JpaRepository<ActoRequisito,Long> {


   public    ActoRequisito     findByActoAndRequisitoAndArchivoIsNull(Acto a, Requisito r);

   public    List<ActoRequisito>     findByActoAndRequisito(Acto a, Requisito r);

   public    List<ActoRequisito>   findAllByActo(Acto a);

     @Query(value="  select  ar.* from ACTO_REQUISITO  ar "+
                " join  ACTO a on a.ID=ar.ACTO_ID" +
                " WHERE a.PRELACION_ID=:idPrelacion"+
                " and a.vf != true "
                ,nativeQuery=true
	)
    public List<ActoRequisito> findAllByPrelacion(@Param("idPrelacion") Long idPrelacion);
    
     @Query(value="SELECT AR FROM ActoRequisito AR where AR.acto.id =:actoId")
    public List<ActoRequisito> findByActoId(@Param("actoId") Long actoId);
     
     public ActoRequisito findByActoIdAndRequisitoIdAndArchivoId(Long actoId,Long requisitoId,Long archivoId);
}
