package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.AvisoActoValorTipoActo;

public interface AvisoActoValorTipoActoRepository extends JpaRepository<AvisoActoValorTipoActo, Long> {

	List<AvisoActoValorTipoActo> findByCampoValoresId(Long campoValoresId);

}
