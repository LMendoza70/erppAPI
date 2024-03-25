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

import com.gisnet.erpp.domain.QJuez;
import com.gisnet.erpp.domain.QMateria;
import com.gisnet.erpp.domain.Juez;
import com.gisnet.erpp.repository.JuezRepositoryCustom;
import com.gisnet.erpp.repository.JuezRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class JuezRepositoryImpl extends QueryDslRepositorySupport implements JuezRepositoryCustom{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    JuezRepositoryImpl() {
        super(Juez.class);
    }
    

    private JPQLQuery<Juez> getQueryFrom(QJuez qEntity) {
        return from(qEntity);
    }

    public Page<Juez> findAllByNombre(String paterno, String materno, String nombre, Integer noJuez, Pageable pageable) { //, Long materiaId,
        QJuez juez = QJuez.juez;
        JPQLQuery<Juez> query = getQueryFrom(juez);
        BooleanBuilder where = new BooleanBuilder();
        if (paterno != null && paterno.length()>0) {
            where.and(juez.paterno.containsIgnoreCase(paterno));
        }
        
        if (materno != null && materno.length()>0) {
            where.and(juez.materno.containsIgnoreCase(materno));
        }

        if (nombre != null && nombre.length()>0) {
            where.and(juez.nombre.containsIgnoreCase(nombre));
        }
        
/*        if (materiaId!=null){
        	where.and(juez.materia.id.eq(materiaId));
        }*/
        
        if (noJuez!=null){
        	where.and(juez.noJuez.eq(noJuez));
        }

        query.where(where);
        long totalFound = query.fetchCount();
        
        setOrder(query, pageable, juez);
        
        query.orderBy(juez.id.asc());
        List<Juez> results = query.fetch();
        return new PageImpl<Juez>(results, pageable, totalFound);
    }
    
    private void setOrder(JPQLQuery<Juez> query, Pageable pageable, QJuez juez){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "noJuez":
                            query.orderBy(new OrderSpecifier<Integer>(direction, juez.noJuez));
                            break;                        
                        case "persona.paterno":
                            query.orderBy(new OrderSpecifier<String>(direction, juez.paterno));
                            break;
                        case "persona.materno":
                            query.orderBy(new OrderSpecifier<String>(direction, juez.materno));
                            break;
                        case "persona.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, juez.nombre));
                            break;                            
                        /*case "materia.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, juez.materia.nombre));
                            break;*/                        
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 


}
