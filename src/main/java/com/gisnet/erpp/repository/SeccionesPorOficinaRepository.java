package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Seccion;
import com.gisnet.erpp.domain.SeccionPorOficina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Seccion entity.
 */
@SuppressWarnings("unused")
public interface SeccionesPorOficinaRepository extends JpaRepository<SeccionPorOficina,Long> {
   	 

   	 SeccionPorOficina findOneByOficinaIdAndSeccionId(long oficinaId, long seccionId);
   	 
   	 List<SeccionPorOficina> findAllWithSeccionByOficinaIdOrderBySeccionNombre(Long oficinaId);
   	 
   	 List<SeccionPorOficina> findByOficinaIdAndSeccionId(long oficinaId, long seccionId);
}
