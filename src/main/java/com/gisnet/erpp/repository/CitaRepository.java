package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Cita;

import org.springframework.data.jpa.repository.*;

import java.util.List;

import java.util.Date;
import org.springframework.data.repository.query.Param;
/**
 * Spring Data JPA repository for the Recibo entity.
 */
@SuppressWarnings("unused")
public interface CitaRepository extends JpaRepository<Cita,Long> {


  @Query(value="SELECT c FROM Cita c WHERE fecha=:fecha AND oficina.id=:oficinaId")
  List<Cita> buscarCitaByFechaAndOficina(@Param("fecha") Date fecha,@Param("oficinaId") Long oficinaId);

   Cita findByPrelacionId(Long id);

}
