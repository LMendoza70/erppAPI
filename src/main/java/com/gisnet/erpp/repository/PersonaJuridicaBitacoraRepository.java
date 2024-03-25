package com.gisnet.erpp.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.PersonaJuridicaBitacora;

public interface PersonaJuridicaBitacoraRepository extends JpaRepository<PersonaJuridicaBitacora,Long> {
 Set<PersonaJuridicaBitacora> findByActoId(Long actoId);
 Optional<PersonaJuridicaBitacora> findFirstByActoId(Long actoId);
 Long deleteByActoId(Long actoId);
}
