package com.gisnet.erpp.repository.impl;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.QModulo;
import com.gisnet.erpp.repository.ModuloRepositoryCustom;
import com.gisnet.erpp.domain.Modulo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class ModuloRepositoryImpl  extends QueryDslRepositorySupport implements ModuloRepositoryCustom{
	ModuloRepositoryImpl() {
        super(Modulo.class);
    }
    
    private JPQLQuery<Modulo> getQueryFrom(QModulo qEntity) {
        return from(qEntity);
    }

    @Transactional(readOnly = true)
	public Page<Modulo> getAllModulos(Pageable pageable,  String nombre) {
        QModulo modulo= QModulo.modulo;
        JPQLQuery<Modulo> query = getQueryFrom(modulo);
        BooleanBuilder where = new BooleanBuilder();
        if (nombre != null && nombre.length()>0) {
            where.and(modulo.nombre.containsIgnoreCase(nombre));
        }
        
        query.where(where);
        long totalFound = query.fetchCount();
        
        setOrder(query, pageable, modulo);
        
        query.orderBy(modulo.id.asc());
        List<Modulo> results = query.fetch();
        return new PageImpl<Modulo>(results, pageable, totalFound);
	}

    
    private void setOrder(JPQLQuery<Modulo> query, Pageable pageable, QModulo modulo){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    switch(((order.getProperty()))) {
                        case "nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, modulo.nombre));
                            break;
                        case "comportamientoModulo":
                            query.orderBy(new OrderSpecifier<Integer>(direction, modulo.comportamientoModulo));
                            break;
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    }
}
