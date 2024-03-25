package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.ServCotizTipoActo;


/**
 * Spring Data JPA repository for the Seccion entity.
 */
@SuppressWarnings("unused")
public interface ServCotizTipoActoRepository extends JpaRepository<ServCotizTipoActo,Long> {   	 
	public ServCotizTipoActo findByTipoActoId(long tipoActoId);
}
