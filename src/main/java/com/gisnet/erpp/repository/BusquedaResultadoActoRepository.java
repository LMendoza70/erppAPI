package com.gisnet.erpp.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.BusquedaResultadoActo;

@SuppressWarnings("unused")
public interface BusquedaResultadoActoRepository extends JpaRepository<BusquedaResultadoActo,Long> {
 
	public Long deleteByBusquedaResultadoId(Long busquedaId);
	public Set<BusquedaResultadoActo> findByActoIdAndBusquedaResultadoId(Long actoId,Long busquedaResultadoId);
	public Set<BusquedaResultadoActo> findByBusquedaResultadoId(Long busquedaResultadoId);
}
