package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.Observacion;
import com.gisnet.erpp.domain.Orientacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Spring Data JPA repository for the Orientacion entity.
 */
@SuppressWarnings("unused")
public interface ObservacionRepository extends JpaRepository<Observacion,Long> {

    Observacion findOneByNombre(String nombre);
    Set<Observacion> findAllByArea(Area area);
}
