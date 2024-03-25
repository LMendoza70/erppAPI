package com.gisnet.erpp.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PersonaJuridica;

import com.gisnet.erpp.domain.QPersonaJuridica;
import com.gisnet.erpp.repository.PersonaJuridicaRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;

public class PersonaJuridicaRepositoryImpl extends QueryDslRepositorySupport implements PersonaJuridicaRepositoryCustom  {


	EntityManager em;

	PersonaJuridicaRepositoryImpl() {
		super(PersonaJuridica.class);
	}
	

	private JPQLQuery<PersonaJuridica> getQueryFrom(QPersonaJuridica qEntity) {
		return from(qEntity);
	}
	
	@Override
	public List<PersonaJuridica> findByDenominacion(Persona persona,Long oficinaId) {
	 QPersonaJuridica qPj =  QPersonaJuridica.personaJuridica;
	 JPQLQuery<PersonaJuridica> query = getQueryFrom(qPj);
	 BooleanBuilder where = new BooleanBuilder();
	 where.and(qPj.oficina.id.eq(oficinaId));
	 where.and(qPj.denominacionNombre.toUpperCase().containsIgnoreCase(persona.getNombre().toUpperCase()));
	 query.where(where);
	 return query.fetch();	
	}

}
