package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


import com.gisnet.erpp.domain.PredioBitacoraColindancia;

@SuppressWarnings("unused")
public interface PredioBitacoraColindanciaRepository extends JpaRepository<PredioBitacoraColindancia,Long> {

	public PredioBitacoraColindancia findOne(Long id);
	
	
	@Query(value="SELECT pbc FROM PredioBitacoraColindancia pbc where pbc.predioBitacora.id=:id")
	public List<PredioBitacoraColindancia> findByPredioBitacoraId(@Param("id") Long id);
}
