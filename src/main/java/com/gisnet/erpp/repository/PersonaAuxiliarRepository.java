package com.gisnet.erpp.repository;

import java.util.Set;

import com.gisnet.erpp.domain.AuxiliarPersona;
import com.gisnet.erpp.domain.PersonaJuridica;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PersonaJuridica entity.
 */
@SuppressWarnings("unused")
public interface PersonaAuxiliarRepository extends JpaRepository<AuxiliarPersona,Long>{

}
