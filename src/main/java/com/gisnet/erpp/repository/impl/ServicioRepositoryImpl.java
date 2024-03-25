package com.gisnet.erpp.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Servicio;
import com.gisnet.erpp.domain.QServicio;
import com.gisnet.erpp.repository.ServicioRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class ServicioRepositoryImpl extends QueryDslRepositorySupport implements ServicioRepositoryCustom {
	ServicioRepositoryImpl() {
		super(Servicio.class);
	}
  
	private JPQLQuery<Servicio> getQueryFrom(QServicio qEntity) {
		return from(qEntity);
	}
	
	@Transactional(readOnly = true)
	public Page<Servicio> getAllServicios(Pageable pageable,  String nombre) {
      QServicio servicio = QServicio.servicio;
      JPQLQuery<Servicio> query = getQueryFrom(servicio);
      BooleanBuilder where = new BooleanBuilder();
      if (nombre != null && nombre.length()>0) {
          where.and(servicio.nombre.containsIgnoreCase(nombre));
      }
      
      query.where(where);
      long totalFound = query.fetchCount();
      
      setOrder(query, pageable, servicio);
      
      query.orderBy(servicio.id.asc());
      List<Servicio> results = query.fetch();
      return new PageImpl<Servicio>(results, pageable, totalFound);
	}

  	private void setOrder(JPQLQuery<Servicio> query, Pageable pageable, QServicio servicio){    	    	
  		if (pageable != null) {
          if (pageable.getSort() != null) {
              for (Sort.Order order : pageable.getSort()) {
                  Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                  switch(((order.getProperty()))) {
                      case "nombre":
                          query.orderBy(new OrderSpecifier<String>(direction, servicio.nombre));
                          break;
                  }
              }
          }
          query.offset(pageable.getOffset()).limit(pageable.getPageSize());
  		}
  	} 
}
