package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Vialidad;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Vialidad entity.
 */
@SuppressWarnings("unused")
public interface VialidadRepository extends JpaRepository<Vialidad,Long> {

	public Vialidad findById(Long id);
	
	public List<Vialidad> findByMunicipioIdAndTipoVialidadId(Long municipioId, Long tipoVialidadId);

}
