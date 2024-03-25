package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.PredioAnte;
import com.gisnet.erpp.domain.Predio;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the PredioAnte entity.
 */
@SuppressWarnings("unused")
public interface PredioAnteRepository extends JpaRepository<PredioAnte,Long> {

    Optional<PredioAnte> findByPredioId(Long predioId);
    
    PredioAnte findByPredioIdAndAntecedenteId(Long predioId, Long antecedenteId);
    
    Optional<PredioAnte> findFirstByPredioIdOrderByIdDesc(Long predioId);

    Optional<PredioAnte> findFirstByPredioIdOrderByAntecedenteLibroAnioDesc(Long predioId);
    
    

}
