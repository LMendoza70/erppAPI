package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.StatusActo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StatusActo entity.
 */
@SuppressWarnings("unused")
public interface StatusActoRepository extends JpaRepository<StatusActo,Long> {
    public StatusActo findOneByNombre (String nombre);
}
