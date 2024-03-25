package com.gisnet.erpp.repository;


import com.gisnet.erpp.domain.Busqueda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public interface BusquedaRepository extends JpaRepository<Busqueda, Long> {

    HashSet<Busqueda> findByPrelacionIdAndTipoBusqueda(Long prelacionId, Integer tipoBusqueda);
    List<Busqueda> findByPrelacionId(Long prelacionId);
}
