package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.FoliosFracCond;
import com.gisnet.erpp.domain.Colindancia;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Predio entity.
 */
@SuppressWarnings("unused")
public interface FoliosFracCondRepository extends JpaRepository<FoliosFracCond,Long> {
    Long deleteByPredioId(long predioId);
    
    Long deleteByActoId(long actoId);

    @Query("SELECT f FROM FoliosFracCond f WHERE f.id = :id")
    FoliosFracCond findById(@Param("id") Long id);
    @Query("SELECT f FROM FoliosFracCond f WHERE f.acto.id = :id")
	List<FoliosFracCond> findByIdActo(@Param("id") Long id);
    
    List<FoliosFracCond> findByPaseFracCondId(Long paseId);
    @Query("SELECT MAX(f.noPredio) FROM FoliosFracCond f WHERE f.paseFracCond.id = :paseId")
    Integer findMaxFolio(@Param("paseId") Long paseId);
    List<FoliosFracCond> findByPredioId(Long predioId);
}
