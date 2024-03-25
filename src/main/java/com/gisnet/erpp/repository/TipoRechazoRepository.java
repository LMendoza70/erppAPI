package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Motivo;
import com.gisnet.erpp.domain.TipoRechazo;

import org.springframework.data.jpa.repository.*; 

import java.util.List;

/**
 * Spring Data JPA repository for the TipoMotivo entity.
 */
@SuppressWarnings("unused")
public interface TipoRechazoRepository extends JpaRepository<TipoRechazo,Long> {
    
	public TipoRechazo findById(Long id);
	
	public List<TipoRechazo> findAllByTipoMotivoNombre(String nombreTipoMotivo);

	
}
