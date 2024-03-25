package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ReqClasifActo;
import com.gisnet.erpp.domain.TipoActo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReqClasifActo entity.
 */
@SuppressWarnings("unused")
public interface ReqClasifActoRepository extends JpaRepository<ReqClasifActo,Long> {
	public abstract List<ReqClasifActo> findByClasifActoId(Long id);
}
