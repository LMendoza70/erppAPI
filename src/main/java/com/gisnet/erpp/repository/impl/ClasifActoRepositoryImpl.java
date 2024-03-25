package com.gisnet.erpp.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.QClasifActo;
import com.gisnet.erpp.domain.ClasifActo;
import com.gisnet.erpp.repository.ClasifActoRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class ClasifActoRepositoryImpl extends QueryDslRepositorySupport implements ClasifActoRepositoryCustom {
	ClasifActoRepositoryImpl() {
		super(ClasifActo.class);
	}
  
	private JPQLQuery<ClasifActo> getQueryFrom(QClasifActo qEntity) {
		return from(qEntity);
	}
	
	@Transactional(readOnly = true)
	public Page<ClasifActo> getAllClasifActos(Pageable pageable,  String nombre) {
      QClasifActo clasifActo = QClasifActo.clasifActo;
      JPQLQuery<ClasifActo> query = getQueryFrom(clasifActo);
      BooleanBuilder where = new BooleanBuilder();
      if (nombre != null && nombre.length()>0) {
          where.and(clasifActo.nombre.containsIgnoreCase(nombre));
      }
      
      query.where(where);
      long totalFound = query.fetchCount();
      
      setOrder(query, pageable, clasifActo);
      
      query.orderBy(clasifActo.id.asc());
      List<ClasifActo> results = query.fetch();
      return new PageImpl<ClasifActo>(results, pageable, totalFound);
	}

  	private void setOrder(JPQLQuery<ClasifActo> query, Pageable pageable, QClasifActo clasifActo){    	    	
  		if (pageable != null) {
          if (pageable.getSort() != null) {
              for (Sort.Order order : pageable.getSort()) {
                  Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                  switch(((order.getProperty()))) {
                      case "nombre":
                          query.orderBy(new OrderSpecifier<String>(direction, clasifActo.nombre));
                          break;
                      case "activo":
                          query.orderBy(new OrderSpecifier<Boolean>(direction, clasifActo.activo));
                          break;
                  }
              }
          }
          query.offset(pageable.getOffset()).limit(pageable.getPageSize());
  		}
  	} 
}
