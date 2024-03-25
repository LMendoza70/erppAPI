package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.PrelacionServicio;

import com.gisnet.erpp.service.PrelacionService;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PrelacionAnte entity.
 */
@SuppressWarnings("unused")
public interface PrelacionServicioRepository extends JpaRepository<PrelacionServicio,Long> {


    List<PrelacionServicio> findByPrelacionId(Long id);
    List<PrelacionServicio> findAllByPrelacionId(long prelacionId);
    @Query("SELECT ps FROM PrelacionServicio ps  WHERE ps.prelacion.id =:id")
    PrelacionServicio findByIdPrelacion(@Param("id") Long id);

}
