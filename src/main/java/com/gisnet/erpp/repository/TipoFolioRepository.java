package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoFolio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

import javax.persistence.QueryHint;

/**
 * Spring Data JPA repository for the TipoEntrada entity.
 */
@SuppressWarnings("unused")
public interface TipoFolioRepository extends JpaRepository<TipoFolio,Long> {

	public TipoFolio findByNombreContaining (String nombre);
	
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true")})
	public List<TipoFolio> findAllByOrderByNombreAsc();
	
	public List<TipoFolio> findByIdIn(List<Long> ids);
}
