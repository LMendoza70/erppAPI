package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.ControlTipoRelPersona;

public interface ControlTipoRelPersonaRepository  extends JpaRepository<ControlTipoRelPersona,Long>{

	ControlTipoRelPersona findFirstByCampoValoresId(Long campoValorId);
	
}
