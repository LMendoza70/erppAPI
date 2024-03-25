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

import com.gisnet.erpp.domain.QNotario;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.repository.NotarioRepositoryCustom;
import com.gisnet.erpp.repository.NotarioRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class NotarioRepositoryImpl extends QueryDslRepositorySupport implements NotarioRepositoryCustom{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    NotarioRepositoryImpl() {
        super(Notario.class);
    }
    

    private JPQLQuery<Notario> getQueryFrom(QNotario qEntity) {
        return from(qEntity);
    }

    public Page<Notario> findAllByNombre(Long municipioId, Long estadoId, String paterno, String materno, String nombre, Integer noNotario, Pageable pageable) {
        QNotario notario = QNotario.notario;
        JPQLQuery<Notario> query = getQueryFrom(notario);
        BooleanBuilder where = new BooleanBuilder();
        if (paterno != null && paterno.length()>0) {
            where.and(notario.persona.paterno.containsIgnoreCase(paterno));
        }
        
        if (materno != null && materno.length()>0) {
            where.and(notario.persona.materno.containsIgnoreCase(materno));
        }

        if (nombre != null && nombre.length()>0) {
            where.and(notario.persona.nombre.containsIgnoreCase(nombre));
        }
        
        if (noNotario!=null){
        	where.and(notario.noNotario.eq(noNotario));
        }

        if (estadoId!=null){
        	where.and(notario.municipio.estado.id.eq(estadoId));
				}
        if (municipioId!=null){
        	where.and(notario.municipio.id.eq(municipioId));
				}
        
        where.and(notario.activo.eq(true));
        
        query.where(where);
        long totalFound = query.fetchCount();
        
        setOrder(query, pageable, notario);
        
        query.orderBy(notario.id.asc());
        List<Notario> results = query.fetch();
        return new PageImpl<Notario>(results, pageable, totalFound);
    }
    
    private void setOrder(JPQLQuery<Notario> query, Pageable pageable, QNotario notario){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "noNotario":
                            query.orderBy(new OrderSpecifier<Integer>(direction, notario.noNotario));
                            break;
                        case "municipio.nombre":
                    		query.orderBy(new OrderSpecifier<String>(direction, notario.municipio.nombre));
                        	break;
                        case "persona.paterno":
                            query.orderBy(new OrderSpecifier<String>(direction, notario.persona.paterno));
                            break;
                        case "persona.materno":
                            query.orderBy(new OrderSpecifier<String>(direction, notario.persona.materno));
                            break;
                        case "persona.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, notario.persona.nombre));
                            break;                            
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 


}
