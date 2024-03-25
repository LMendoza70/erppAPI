package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;

import com.gisnet.erpp.domain.BitacoraReingreso;


public interface BitacoraReingresoRepository extends JpaRepository<BitacoraReingreso,Long>{

    List<BitacoraReingreso> findByPrelacionIdOrderByFechaDesc(Long prelacionId);

}