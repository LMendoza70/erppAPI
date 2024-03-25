package com.gisnet.erpp.repository;


import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.TipoDocTipoActo;
import com.gisnet.erpp.domain.TipoDocumento;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoDocTipoActo entity.
 */
@SuppressWarnings("unused")
public interface TipoDocTipoActoRepository extends JpaRepository<TipoDocTipoActo,Long> {

	public abstract List<TipoDocTipoActo> findBytipoActoId(Long id);
	
}
