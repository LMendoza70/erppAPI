package com.gisnet.erpp.repository;

import java.util.List;


import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PersonaJuridica;

public interface PersonaJuridicaRepositoryCustom {
	List<PersonaJuridica> findByDenominacion(Persona persona,Long oficinaId);
}
