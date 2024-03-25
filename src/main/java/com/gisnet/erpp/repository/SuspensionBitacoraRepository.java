package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.SuspensionBitacora;

@SuppressWarnings("unused")
public interface SuspensionBitacoraRepository extends JpaRepository<SuspensionBitacora,Long> {
  
	
	List<SuspensionBitacora> findAllByPrelacionIdOrderByIdDesc(Long prelacionId);
}
