package com.gisnet.erpp.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.RecursoInconformidad;

public interface RecursoInconformidadRepository extends JpaRepository<RecursoInconformidad,Long> {
  
	Long deleteByActoId(Long actoId);
	
	Optional<RecursoInconformidad> findFirstByActoId(Long actoId);
	
	@Query(value="SELECT ri FROM RecursoInconformidad ri join fetch ri.acto a join fetch a.prelacion p where  extract(year from p.fechaEnvioFirma) =:a and  extract(month from p.fechaEnvioFirma) =:m and  extract(day from p.fechaEnvioFirma) =:d and p.oficina.id =:oficinaId and p.status.id in (7,8,10) and ri.confirmaDenegacion = true and ri.prelacion is not null and ri.tipoInconformidad.id =:tipoInconformidadId ")
	List<RecursoInconformidad> findAllByFechaAndOficinaDenegados(@Param("a") Integer a,@Param("m") Integer m,@Param("d") Integer d, @Param("oficinaId") Long oficinaId,@Param("tipoInconformidadId") Long tipoInconformidad);
}
