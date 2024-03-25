package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Estado;
import com.gisnet.erpp.domain.Oficina;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Oficina entity.
 */
@SuppressWarnings("unused")
public interface OficinaRepository extends JpaRepository<Oficina,Long> {
Oficina findByNombre(String nombre);

Oficina findByNumOficina(String nombre);

Oficina findById(Long id);

public List<Oficina> findAllByOrderByNombre();

public List<Oficina> findAllByOrderByNumOficina();

@Query(value= " SELECT O.numOficina FROM Oficina O WHERE O.numOficina=:numOfice AND O.estado =:estado")
List <Oficina>findOficinaByEstadoAndNumOficina(@Param("numOfice") String numOfice, @Param("estado") Estado estado);

}
