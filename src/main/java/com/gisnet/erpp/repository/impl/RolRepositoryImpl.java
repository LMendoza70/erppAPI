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

import com.gisnet.erpp.domain.QRol;
import com.gisnet.erpp.repository.RolRepositoryCustom;
import com.gisnet.erpp.domain.Rol;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class RolRepositoryImpl extends QueryDslRepositorySupport implements RolRepositoryCustom{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    RolRepositoryImpl() {
        super(Rol.class);
    }

    private JPQLQuery<Rol> getQueryFrom(QRol qEntity) {
        return from(qEntity);
    }
    
   
    @Transactional(readOnly = true)
	public Page<Rol> findAllBy(Pageable pageable,  String nombre, Boolean activo) {
    QRol rol = QRol.rol;
    JPQLQuery<Rol> query = getQueryFrom(rol);
    BooleanBuilder where = new BooleanBuilder();

    if (nombre != null && nombre.length()>0) {
        where.and(rol.nombre.containsIgnoreCase(nombre));
    }

    if (activo != null) {
        where.and(rol.activo.eq(activo));
    }
    
    query.where(where);
    long totalFound = query.fetchCount();
    log.debug("Total de rols encontrados="+ totalFound);
    
    setOrder(query, pageable, rol);
    
    query.orderBy(rol.id.asc());
    List<Rol> results = query.fetch();
    return new PageImpl<Rol>(results, pageable, totalFound);
	}
    
    private void setOrder(JPQLQuery<Rol> query, Pageable pageable, QRol rol){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, rol.nombre));
                            break;                                                                  
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 


}
