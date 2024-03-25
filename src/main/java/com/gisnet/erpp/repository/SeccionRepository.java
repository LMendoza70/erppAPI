package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Seccion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Seccion entity.
 */
@SuppressWarnings("unused")
public interface SeccionRepository extends JpaRepository<Seccion,Long> {
   	 
   	 Seccion findByNombre(String nombre);

   	 List<Seccion> findBySeccionesPorOficinaOficinaId(Long idOficina);
   	List<Seccion> findBySeccionesPorOficinaOficinaIdOrderByIdAsc(Long idOficina);
   	 
   	 Seccion findById(Long Id);
}
