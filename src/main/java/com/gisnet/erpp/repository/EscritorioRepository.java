package com.gisnet.erpp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.Escritorio;

public interface EscritorioRepository  extends JpaRepository<Escritorio,Long>{

	Long countByUsuarioOficinaIdAndDiaEscritorioAndRolId(Long idOficina,Date fechaActual,Long idRol);
	
	List<Escritorio> findByDiaEscritorioAndRolIdAndUsuarioIdInOrderByPonderacionAsc(Date fecha,Long idRol,List<Long> idsUsuarios);
}
