package com.gisnet.erpp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.PaseFracCond;

public interface PaseFracCondRepository extends JpaRepository<PaseFracCond, Long> {
	Optional<PaseFracCond> findFirstByConsecutivo(Integer consecutivo);

	List<PaseFracCond> findByUsuarioId(Long usuarioId);
	 
	@Query(value="SELECT pf FROM PaseFracCond pf WHERE pf.usuario.id  = :usuarioId AND pf.fechaCaduca > :fecha AND (pf.procesado is null OR pf.procesado = false)")
	List<PaseFracCond> findByUsuarioNoProcesadosAndVigentes(@Param("usuarioId") Long usuarioId,
			@Param("fecha") Date fecha );
	
	
	 @Query(value="select nextval('pase_fraccionamiento_seq')", nativeQuery = true)
	 Long getFolioPaseSequence();
	 
	 
}
