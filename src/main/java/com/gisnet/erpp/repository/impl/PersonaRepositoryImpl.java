package com.gisnet.erpp.repository.impl;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.QPersona;
import com.gisnet.erpp.repository.PersonaRepositoryCustom;
import com.gisnet.erpp.domain.Persona;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class PersonaRepositoryImpl extends QueryDslRepositorySupport implements PersonaRepositoryCustom{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    PersonaRepositoryImpl() {
        super(Persona.class);
    }
    

    private JPQLQuery<Persona> getQueryFrom(QPersona qEntity) {
        return from(qEntity);
    }

@Transactional(readOnly = true)
		public Page<Persona> getAllPersonas(Pageable pageable,  String paterno, String materno, String nombre, Long tipoPersonaId, Long isPublica) {
        QPersona persona = QPersona.persona;
        JPQLQuery<Persona> query = getQueryFrom(persona);
        BooleanBuilder where = new BooleanBuilder();
        if (paterno != null && paterno.length()>0) {
            where.and(persona.paterno.containsIgnoreCase(paterno));
        }
        
        if (materno != null && materno.length()>0) {
            where.and(persona.materno.containsIgnoreCase(materno));
        }

        if (nombre != null && nombre.length()>0) {
            where.and(persona.nombre.containsIgnoreCase(nombre));
        }
        if (tipoPersonaId != null) {
            where.and(persona.tipoPersona.id.eq(tipoPersonaId));
        }
        if (isPublica != null) {
            where.and(persona.publica.eq(isPublica.equals(1L) ? true: false));
        }
        
        query.where(where);
        long totalFound = query.fetchCount();
        log.debug("Total de personas encontrados="+ totalFound);
        
        setOrder(query, pageable, persona);
        
        query.orderBy(persona.id.asc());
        List<Persona> results = query.fetch();
        return new PageImpl<Persona>(results, pageable, totalFound);
		}

@Transactional(readOnly = true)
    public Page<Persona> findAllByNombre(String paterno, String materno, String nombre, Pageable pageable) {
        QPersona persona = QPersona.persona;
        JPQLQuery<Persona> query = getQueryFrom(persona);
        BooleanBuilder where = new BooleanBuilder();
        if (paterno != null && paterno.length()>0) {
            where.and(persona.paterno.containsIgnoreCase(paterno));
        }
        
        if (materno != null && materno.length()>0) {
            where.and(persona.materno.containsIgnoreCase(materno));
        }

        if (nombre != null && nombre.length()>0) {
            where.and(persona.nombre.containsIgnoreCase(nombre));
        }
        
        query.where(where);
        long totalFound = query.fetchCount();
        log.debug("Total de personas encontrados="+ totalFound);
        
        setOrder(query, pageable, persona);
        
        query.orderBy(persona.id.asc());
        List<Persona> results = query.fetch();
        return new PageImpl<Persona>(results, pageable, totalFound);
    }
    
    private void setOrder(JPQLQuery<Persona> query, Pageable pageable, QPersona persona){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "paterno":
                            query.orderBy(new OrderSpecifier<String>(direction, persona.paterno));
                            break;
                        case "materno":
                            query.orderBy(new OrderSpecifier<String>(direction, persona.materno));
                            break;
                        case "nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, persona.nombre));
                            break;                                                                  
                        case "tipoPersona.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, persona.tipoPersona.nombre));
                            break;                        
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 
    
    @Transactional(readOnly = true)
	public List<Persona> findPersonaByParams(Persona personaBusqu) {
    	
    QPersona persona = QPersona.persona;
    JPQLQuery<Persona> query = getQueryFrom(persona);
    BooleanBuilder where = new BooleanBuilder();
    
    if (personaBusqu.getPaterno() != null && personaBusqu.getPaterno().length()>0) {
        where.and(persona.paterno.containsIgnoreCase(personaBusqu.getPaterno()));
        where.or(persona.paterno.like("%" + personaBusqu.getPaterno() + "%"));
    }/*else if (personaBusqu.getCurp() == null) {
    	where.and(persona.paterno.isNull());
    }*/
    
    if (personaBusqu.getMaterno() != null && personaBusqu.getMaterno().length()>0) {
        where.and(persona.materno.containsIgnoreCase(personaBusqu.getMaterno()));
        where.or(persona.materno.like("%" + personaBusqu.getMaterno() + "%"));
    }

    if (personaBusqu.getNombre() != null && personaBusqu.getNombre().length()>0) {
        where.and(persona.nombre.containsIgnoreCase(personaBusqu.getNombre()));
        where.or(persona.nombre.like("%" + personaBusqu.getNombre() + "%"));
    }
    if (personaBusqu.getTipoPersona() != null) {
        where.and(persona.tipoPersona.id.eq(personaBusqu.getTipoPersona().getId()));
        where.or(persona.tipoPersona.id.like("%" + personaBusqu.getTipoPersona().getId() + "%"));
    }
    
    if (personaBusqu.getRfc() != null) {
        where.and(persona.rfc.eq(personaBusqu.getRfc()));
    }
    
    if (personaBusqu.getCurp() != null) {
        where.and(persona.curp.eq(personaBusqu.getCurp()));
    }
    
    query.where(where);
    long totalFound = query.fetchCount();
    log.debug("Total de personas encontrados="+ totalFound);
    
    
    query.orderBy(persona.id.asc());
    List<Persona> results = query.fetch();
    return results;
	}

@Transactional(readOnly = true)
    public List<Persona> findPersonasByNombre(Persona personaBusqu) {

        QPersona persona = QPersona.persona;
        JPQLQuery<Persona> query = getQueryFrom(persona);
        BooleanBuilder where = new BooleanBuilder();

        if (personaBusqu.getNombre() != null && personaBusqu.getNombre().length()>0) {
            where.and(persona.nombre.containsIgnoreCase(personaBusqu.getNombre()));
        }

        query.where(where);
        long totalFound = query.fetchCount();
        log.debug("Total de personas encontrados="+ totalFound);


        query.orderBy(persona.id.asc());
        List<Persona> results = query.fetch();
        return results;
    }

    @Transactional(readOnly = true)
    public List<Persona> findPersonaByApellidoPaterno(Persona personaBusqu) {

        QPersona persona = QPersona.persona;
        JPQLQuery<Persona> query = getQueryFrom(persona);
        BooleanBuilder where = new BooleanBuilder();

        if (personaBusqu.getPaterno() != null && personaBusqu.getPaterno().length()>0) {
            where.and(persona.paterno.containsIgnoreCase(personaBusqu.getPaterno()));
        }

        query.where(where);
        long totalFound = query.fetchCount();
        log.debug("Total de personas encontrados="+ totalFound);


        query.orderBy(persona.id.asc());
        List<Persona> results = query.fetch();
        return results;
    }

    @Transactional(readOnly = true)
    public List<Persona> findPersonaByApellidoMaterno(Persona personaBusqu) {

        QPersona persona = QPersona.persona;
        JPQLQuery<Persona> query = getQueryFrom(persona);
        BooleanBuilder where = new BooleanBuilder();

        if (personaBusqu.getMaterno() != null && personaBusqu.getMaterno().length()>0) {
            where.and(persona.materno.containsIgnoreCase(personaBusqu.getMaterno()));
        }

        query.where(where);
        long totalFound = query.fetchCount();
        log.debug("Total de personas encontrados="+ totalFound);


        query.orderBy(persona.id.asc());
        List<Persona> results = query.fetch();
        return results;
    }

    @Transactional(readOnly = true)
    public List<Persona> findPersonaByCurp(Persona personaBusqu) {

        QPersona persona = QPersona.persona;
        JPQLQuery<Persona> query = getQueryFrom(persona);
        BooleanBuilder where = new BooleanBuilder();

        if (personaBusqu.getCurp() != null) {
            where.and(persona.curp.eq(personaBusqu.getCurp()));
        }

        query.where(where);
        long totalFound = query.fetchCount();
        log.debug("Total de personas encontrados="+ totalFound);


        query.orderBy(persona.id.asc());
        List<Persona> results = query.fetch();
        return results;
    }
}
