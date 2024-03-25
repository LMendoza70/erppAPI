package com.gisnet.erpp.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.PrelacionContratante;
import com.gisnet.erpp.domain.QPrelacionContratante;
import com.gisnet.erpp.repository.PrelacionContratanteRepositoryCustom;
import com.gisnet.erpp.vo.PrelacionContratanteVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class PrelacionContratanteRepositoryImpl 
	extends QueryDslRepositorySupport implements PrelacionContratanteRepositoryCustom{

	private final Logger log = LoggerFactory.getLogger(this.getClass());

    PrelacionContratanteRepositoryImpl() {
        super(PrelacionContratante.class);
    }
    

    private JPQLQuery<PrelacionContratante> getQueryFrom(QPrelacionContratante qEntity) {
        return from(qEntity);
    }

    public Page<PrelacionContratanteVO> findAllByNombre(String paterno, String materno, String nombre, Pageable pageable) {
    	QPrelacionContratante contratante = QPrelacionContratante.prelacionContratante;
        JPQLQuery<PrelacionContratante> query = getQueryFrom(contratante);
        BooleanBuilder where = new BooleanBuilder();
        if (paterno != null && paterno.length()>0) {
            where.and(contratante.paterno.containsIgnoreCase(paterno));
        }
        
        if (materno != null && materno.length()>0) {
            where.and(contratante.materno.containsIgnoreCase(materno));
        }

        if (nombre != null && nombre.length()>0) {
            where.and(contratante.nombre.containsIgnoreCase(nombre));
        }

        query.where(where);
        long totalFound = query.fetchCount();
        
        setOrder(query, pageable, contratante);
        
        query.orderBy(contratante.id.asc());
        List<PrelacionContratante> results = query.fetch();
        
        List<PrelacionContratanteVO> listVos = transformToVos(results);
        
        return new PageImpl<PrelacionContratanteVO>(listVos, pageable, totalFound);
    }
    
    private List<PrelacionContratanteVO> transformToVos(List<PrelacionContratante> results) {
    	List<PrelacionContratanteVO> listVos = new ArrayList<PrelacionContratanteVO>();
    	
    	for( PrelacionContratante contratante : results ) {
    		listVos.add( new PrelacionContratanteVO( contratante.getId(),
    				contratante.getNombre(),
    				contratante.getPaterno(),
    				contratante.getMaterno()) );
    	}
    	
    	return listVos;
	}


	private void setOrder(JPQLQuery<PrelacionContratante> query, Pageable pageable, QPrelacionContratante contratante){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "paterno":
                            query.orderBy(new OrderSpecifier<String>(direction, contratante.paterno));
                            break;
                        case "materno":
                            query.orderBy(new OrderSpecifier<String>(direction, contratante.materno));
                            break;
                        case "nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, contratante.nombre));
                            break;                            
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 


}
