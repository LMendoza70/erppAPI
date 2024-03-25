package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.EstadoCivil;
import com.gisnet.erpp.domain.ParametroCotizador;
import com.gisnet.erpp.domain.ParametroCotizadorCosto;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the EstadoCivil entity.
 */
@SuppressWarnings("unused")
public interface EstadoCivilRepository extends JpaRepository<EstadoCivil,Long> {
    EstadoCivil findDistinctByNombreEquals(String nombre);
    
    @Query(value= "SELECT ec FROM EstadoCivil ec WHERE REPLACE(ec.nombre, ' ', '') =:EC")
    List<EstadoCivil>findByEstadoCivil(@Param("EC") String EC);
   // List<EstadoCivil>findAllByEstadoCivil(EstadoCivil estadoCivil);
    Optional<EstadoCivil> findOneByCampoValoresId(Long campoValorId);
}
