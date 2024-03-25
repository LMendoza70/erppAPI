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

import com.gisnet.erpp.domain.QIdentificacion;
import com.gisnet.erpp.domain.Identificacion;
import com.gisnet.erpp.domain.Estado;
import com.gisnet.erpp.domain.Municipio;
import com.gisnet.erpp.repository.IdentificacionRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class IdentificacionRepositoryImpl extends QueryDslRepositorySupport implements IdentificacionRepositoryCustom{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    IdentificacionRepositoryImpl() {
        super(Identificacion.class);
    }

    private JPQLQuery<Identificacion> getQueryFrom(QIdentificacion qEntity) {
        return from(qEntity);
    }

		public Page<Identificacion> findAllByNombre(Pageable pageable, String valor, Long personaId, Long tipoIdenId ) {
			System.out.println("  " + valor + "  " + personaId + "  " + tipoIdenId);	

        QIdentificacion identificacion = QIdentificacion.identificacion;
        JPQLQuery<Identificacion> query = getQueryFrom(identificacion);
        BooleanBuilder where = new BooleanBuilder();
        if (valor != null && valor.length()>0) {
            where.and(identificacion.valor.containsIgnoreCase(valor));
        }

        if (personaId!=null){
        	where.and(identificacion.persona.id.eq(personaId));
        }
        
        if (tipoIdenId!=null){
        	where.and(identificacion.tipoIden.id.eq(tipoIdenId));
        }
        
        query.where(where);
        long totalFound = query.fetchCount();

        setOrder(query, pageable, identificacion);
        

        query.orderBy(identificacion.id.asc());
        
        List<Identificacion> results = query.fetch();
        return new PageImpl<Identificacion>(results, pageable, totalFound);
    }

    private void setOrder(JPQLQuery<Identificacion> query, Pageable pageable, QIdentificacion identificacion){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "valor":
                            query.orderBy(new OrderSpecifier<String>(direction, identificacion.valor));
                            break;                            
                        case "persona.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, identificacion.persona.nombre));
                            break;                        
                        case "persona.paterno":
                            query.orderBy(new OrderSpecifier<String>(direction, identificacion.persona.paterno));
                            break;
                        case "persona.materno":
                            query.orderBy(new OrderSpecifier<String>(direction, identificacion.persona.materno));
                            break;
                        case "tipoIden.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, identificacion.tipoIden.nombre));
                            break;
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 
}
