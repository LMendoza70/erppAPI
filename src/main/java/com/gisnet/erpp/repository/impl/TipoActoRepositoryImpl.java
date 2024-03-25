package com.gisnet.erpp.repository.impl;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.QTipoActo;
import com.gisnet.erpp.repository.TipoActoRepositoryCustom;
import com.gisnet.erpp.domain.TipoActo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class TipoActoRepositoryImpl extends QueryDslRepositorySupport implements TipoActoRepositoryCustom{

    TipoActoRepositoryImpl() {
        super(TipoActo.class);
    }
    
    private JPQLQuery<TipoActo> getQueryFrom(QTipoActo qEntity) {
        return from(qEntity);
    }

    @Transactional(readOnly = true)
	public Page<TipoActo> getAllTiposActos(Pageable pageable,  String nombre) {
        QTipoActo tipoActo= QTipoActo.tipoActo;
        JPQLQuery<TipoActo> query = getQueryFrom(tipoActo);
        BooleanBuilder where = new BooleanBuilder();
        if (nombre != null && nombre.length()>0) {
            where.and(tipoActo.nombre.containsIgnoreCase(nombre));
        }
        where.and(tipoActo.activo.isTrue());
        
        query.where(where);
        long totalFound = query.fetchCount();
        
        setOrder(query, pageable, tipoActo);
        
        query.orderBy(tipoActo.id.asc());
        List<TipoActo> results = query.fetch();
        return new PageImpl<TipoActo>(results, pageable, totalFound);
	}

    private void setOrder(JPQLQuery<TipoActo> query, Pageable pageable, QTipoActo tipoActo){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    switch(((order.getProperty()))) {
	                    case "id":
	                        query.orderBy(new OrderSpecifier<Long>(direction, tipoActo.id));
	                        break;
                        case "nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, tipoActo.nombre));
                            break;
                        case "activo":
                            query.orderBy(new OrderSpecifier<Boolean>(direction, tipoActo.activo));
                            break;
                        case "ponderacion":
                            query.orderBy(new OrderSpecifier<Integer>(direction, tipoActo.ponderacion));
                            break;
                        case "prediosEntrada":
                            query.orderBy(new OrderSpecifier<String>(direction, tipoActo.prediosEntrada));
                            break;
                        case "prediosSalida":
                            query.orderBy(new OrderSpecifier<String>(direction, tipoActo.prediosSalida));
                            break;
                        case "primerRegistro":
                            query.orderBy(new OrderSpecifier<Boolean>(direction, tipoActo.primerRegistro));
                            break;
                        case "requiereDocumento":
                            query.orderBy(new OrderSpecifier<Boolean>(direction, tipoActo.requiereDocumento));
                            break;
                        case "tipoCampo.nombre":
                          query.orderBy(new OrderSpecifier<String>(direction, tipoActo.clasifActo.nombre));
                          break;
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 
}
