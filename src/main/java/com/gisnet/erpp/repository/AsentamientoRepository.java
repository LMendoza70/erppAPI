package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.Asentamiento;

/**
 * Spring Data JPA repository for the Asentamiento entity.
 */
@SuppressWarnings("unused")
public interface AsentamientoRepository extends JpaRepository<Asentamiento,Long>, AsentamientoRepositoryCustom{
	
	public List<Asentamiento> findByMunicipioIdAndTipoAsentId(Long municipioId, Long tipoAsentId);

}
