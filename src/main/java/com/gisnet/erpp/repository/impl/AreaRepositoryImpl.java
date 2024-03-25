package com.gisnet.erpp.repository.impl;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.QArea;
import com.gisnet.erpp.repository.AreaRepositoryCustom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class AreaRepositoryImpl  extends QueryDslRepositorySupport implements AreaRepositoryCustom {
	AreaRepositoryImpl() {
		super(Area.class);
	}
  
	private JPQLQuery<Area> getQueryFrom(QArea qEntity) {
		return from(qEntity);
	}

  	@Transactional(readOnly = true)
	public Page<Area> getAllAreas(Pageable pageable,  String nombre) {
      QArea area = QArea.area;
      JPQLQuery<Area> query = getQueryFrom(area);
      BooleanBuilder where = new BooleanBuilder();
      if (nombre != null && nombre.length()>0) {
          where.and(area.nombre.containsIgnoreCase(nombre));
      }
      
      query.where(where);
      long totalFound = query.fetchCount();
      
      setOrder(query, pageable, area);
      
      query.orderBy(area.id.asc());
      List<Area> results = query.fetch();
      return new PageImpl<Area>(results, pageable, totalFound);
	}

  	private void setOrder(JPQLQuery<Area> query, Pageable pageable, QArea area){    	    	
  		if (pageable != null) {
          if (pageable.getSort() != null) {
              for (Sort.Order order : pageable.getSort()) {
                  Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                  switch(((order.getProperty()))) {
                      case "nombre":
                          query.orderBy(new OrderSpecifier<String>(direction, area.nombre));
                          break;
                      case "nombre_largo":
                          query.orderBy(new OrderSpecifier<String>(direction, area.nombreLargo));
                          break;
                  }
              }
          }
          query.offset(pageable.getOffset()).limit(pageable.getPageSize());
  		}
  	} 
}
