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

import com.gisnet.erpp.domain.QAsentamiento;
import com.gisnet.erpp.domain.Asentamiento;
import com.gisnet.erpp.domain.Estado;
import com.gisnet.erpp.domain.Municipio;
import com.gisnet.erpp.repository.AsentamientoRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class AsentamientoRepositoryImpl extends QueryDslRepositorySupport implements AsentamientoRepositoryCustom{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    AsentamientoRepositoryImpl() {
        super(Asentamiento.class);
    }

    private JPQLQuery<Asentamiento> getQueryFrom(QAsentamiento qEntity) {
        return from(qEntity);
    }

		public Page<Asentamiento> findAllByNombre(String nombre, Long tipoAsentId, Pageable pageable, Long estadoId, Long municipioId) {

        QAsentamiento asentamiento = QAsentamiento.asentamiento;
        JPQLQuery<Asentamiento> query = getQueryFrom(asentamiento);
        BooleanBuilder where = new BooleanBuilder();
        if (nombre != null && nombre.length()>0) {
            where.and(asentamiento.nombre.containsIgnoreCase(nombre));
        }

        if (tipoAsentId!=null){
        	where.and(asentamiento.tipoAsent.id.eq(tipoAsentId));
        }
        
        if (municipioId !=null){
        	where.and(asentamiento.municipio.id.eq(municipioId));
        }
        
        if (estadoId !=null){
        	where.and(asentamiento.municipio.estado.id.eq(estadoId));
        }

        query.where(where);
        long totalFound = query.fetchCount();

        setOrder(query, pageable, asentamiento);

        query.orderBy(asentamiento.id.asc());
        List<Asentamiento> results = query.fetch();
        return new PageImpl<Asentamiento>(results, pageable, totalFound);
    }

    private void setOrder(JPQLQuery<Asentamiento> query, Pageable pageable, QAsentamiento asentamiento){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, asentamiento.nombre));
                            break;                            
                        case "tipoAsent.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, asentamiento.tipoAsent.nombre));
                            break;                        
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 
}
