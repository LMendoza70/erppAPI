package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Busqueda;
import com.gisnet.erpp.domain.DetalleBusqueda;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;
import java.util.List;

public interface DetalleBusquedaRepository extends JpaRepository<DetalleBusqueda, Long>{


    DetalleBusqueda findFirstByClaveAndBusquedaId(String key, Long busquedaId);

    HashSet<DetalleBusqueda> findAllByBusqueda (Busqueda busqueda);

    HashSet<DetalleBusqueda> findAllByValorOrValorIsNull(String valor);
}

