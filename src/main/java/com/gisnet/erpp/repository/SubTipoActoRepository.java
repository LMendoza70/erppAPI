package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.SubTipoActo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SubTipoActo entity.
 */
@SuppressWarnings("unused")
public interface SubTipoActoRepository extends JpaRepository<SubTipoActo,Long> {

}
