package com.gisnet.erpp.repository.impl;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.QCampo;
import com.gisnet.erpp.repository.CampoRepositoryCustom;
import com.gisnet.erpp.domain.Campo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class CampoRepositoryImpl extends QueryDslRepositorySupport implements CampoRepositoryCustom{

    CampoRepositoryImpl() {
        super(Campo.class);
    }
    
    private JPQLQuery<Campo> getQueryFrom(QCampo qEntity) {
        return from(qEntity);
    }

    	@Transactional(readOnly = true)
	public Page<Campo> getAllCampos(Pageable pageable,  String nombre) {
        QCampo campo = QCampo.campo;
        JPQLQuery<Campo> query = getQueryFrom(campo);
        BooleanBuilder where = new BooleanBuilder();
        if (nombre != null && nombre.length()>0) {
            where.and(campo.nombre.containsIgnoreCase(nombre));
        }
        
        query.where(where);
        long totalFound = query.fetchCount();
        
        setOrder(query, pageable, campo);
        
        query.orderBy(campo.id.asc());
        List<Campo> results = query.fetch();
        return new PageImpl<Campo>(results, pageable, totalFound);
	}

    private void setOrder(JPQLQuery<Campo> query, Pageable pageable, QCampo campo){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    switch(((order.getProperty()))) {
                        case "nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, campo.nombre));
                            break;
                        case "activo":
                            query.orderBy(new OrderSpecifier<Boolean>(direction, campo.activo));
                            break;
                        case "minima":
                            query.orderBy(new OrderSpecifier<Integer>(direction, campo.minima));
                            break;
                        case "maxima":
                            query.orderBy(new OrderSpecifier<Integer>(direction, campo.maxima));
                            break;
                        case "tabla":
                            query.orderBy(new OrderSpecifier<String>(direction, campo.tabla));
                            break;
                        case "tablaCampo":
                            query.orderBy(new OrderSpecifier<String>(direction, campo.tablaCampo));
                            break;
                        case "tipoCampo.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, campo.tipoCampo.nombre));
                            break;
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 
}
