package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoRelPersona;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoRelPersona entity.
 */
@SuppressWarnings("unused")
public interface TipoRelPersonaRepository extends JpaRepository<TipoRelPersona,Long> {
    public abstract TipoRelPersona findByCampoValorId(Long campoValorId);
}
