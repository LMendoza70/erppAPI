package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.PrelacionUsuarioDef;

import org.springframework.data.jpa.repository.*;

import java.util.List;
/**
 * Spring Data JPA repository for the PrelacionUsuarioDef entity.
 */
@SuppressWarnings("unused")
public interface PrelacionUsuarioDefRepository extends JpaRepository<PrelacionUsuarioDef,Long> {


    PrelacionUsuarioDef findOneByPrelacionId(Long prelacionId);
    PrelacionUsuarioDef findFirstBySolicitudDevolucionId(Long solicitudDevolucionId);
}
