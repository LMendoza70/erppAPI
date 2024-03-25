package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.BitacoraDigitalizador;

/**
 * Spring Data JPA repository for the BitacoraDigitalizador entity.
 */
public interface BitacoraDigitalizadorRepository extends JpaRepository<BitacoraDigitalizador, Long> {
	List<BitacoraDigitalizador> findAllBitacoraDigitalizadorByPrelacionIdOrderByIdDesc (Long id);
	List<BitacoraDigitalizador> findAllBitacoraDigitalizadorByUsuarioId (Long id);
}
