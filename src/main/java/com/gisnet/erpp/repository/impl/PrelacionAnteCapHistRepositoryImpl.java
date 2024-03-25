package com.gisnet.erpp.repository.impl;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.repository.PrelacionAnteCapHistRepositoryCustom;
import com.gisnet.erpp.domain.PrelacionAnteCapHist;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.QPrelacionAnteCapHist;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class PrelacionAnteCapHistRepositoryImpl extends QueryDslRepositorySupport implements PrelacionAnteCapHistRepositoryCustom{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    PrelacionAnteCapHistRepositoryImpl() {
        super(PrelacionAnteCapHist.class);
    }
    

    private JPQLQuery<PrelacionAnteCapHist> getQueryFrom(QPrelacionAnteCapHist qEntity) {
        return from(qEntity);
    }
    
	@Override
	public PrelacionAnteCapHist findFirstCarga(String numLibro, Long seccionPorOficinaId, Integer anio,
			String volumen, String carga) {
		
		
		QPrelacionAnteCapHist prelacionAnteCapHist = QPrelacionAnteCapHist.prelacionAnteCapHist;
	    JPQLQuery<PrelacionAnteCapHist> query = getQueryFrom(prelacionAnteCapHist);
	    BooleanBuilder where = new BooleanBuilder();
	    BooleanBuilder whereIn = new BooleanBuilder();
	    List<Long> statusAnteIds =  new ArrayList<Long>();
	    statusAnteIds.add(1L); statusAnteIds.add(2L); statusAnteIds.add(3L);
	    
	    if (numLibro != null) {
	        where.and(prelacionAnteCapHist.libro.numLibro.eq(numLibro));
	    }
	    
	   if (seccionPorOficinaId != null) {
	        where.and(prelacionAnteCapHist.libro.seccionesPorOficina.id.eq(seccionPorOficinaId));
	    }
	   	
	  /*  if (libroBis != null && libroBis.length()>0) {
	        where.and(prelacionAnteCapHist.libro.libroBis.containsIgnoreCase(libroBis));
	    }
	    */
	    if (anio != null) {
	        where.and(prelacionAnteCapHist.libro.anio.eq(anio));
	    }
	    
	    if (volumen != null) {
	        where.and(prelacionAnteCapHist.libro.volumen.eq(volumen));
	    }
	       
	    if (carga != null) {
	    	where.and(prelacionAnteCapHist.cargaTrabajo.eq(carga));
	    }
	    
	    if (statusAnteIds.size() > 0) {
	    	for (Long status : statusAnteIds) {
	    		whereIn.or(prelacionAnteCapHist.statusAntecedente.id.eq(status));
	    	}
	    	where.and(whereIn.getValue());
	    }
	    
	    query.where(where);
	    
	    List<PrelacionAnteCapHist> results = query.fetch();
	    return  results != null && results.size() > 0 ? results.get(0) : null;
	}


@Transactional(readOnly = true)
	public Page<PrelacionAnteCapHist> findAllBy(String numLibro, String libroBis, Long seccionPorOficinaId, Integer anio, String volumen, Usuario usuarioCaptura, List<Long> statusAnteIds, String carga, Pageable pageable) {
		QPrelacionAnteCapHist prelacionAnteCapHist = QPrelacionAnteCapHist.prelacionAnteCapHist;
        JPQLQuery<PrelacionAnteCapHist> query = getQueryFrom(prelacionAnteCapHist);
        BooleanBuilder where = new BooleanBuilder();
        BooleanBuilder whereIn = new BooleanBuilder();
        if (numLibro != null) {
            where.and(prelacionAnteCapHist.libro.numLibro.eq(numLibro));
        }
        
       if (seccionPorOficinaId != null) {
            where.and(prelacionAnteCapHist.libro.seccionesPorOficina.id.eq(seccionPorOficinaId));
        }
       	
        if (libroBis != null && libroBis.length()>0) {
            where.and(prelacionAnteCapHist.libro.libroBis.containsIgnoreCase(libroBis));
        }
        
        if (anio != null) {
            where.and(prelacionAnteCapHist.libro.anio.eq(anio));
        }
        
        if (volumen != null) {
            where.and(prelacionAnteCapHist.libro.volumen.eq(volumen));
        }
        
        if (usuarioCaptura != null) {
        	where.and(prelacionAnteCapHist.usuarioCaptura.eq(usuarioCaptura));
        }
        
        if (carga != null) {
        	where.and(prelacionAnteCapHist.cargaTrabajo.eq(carga));
        }
        
        if (statusAnteIds.size() > 0) {
        	for (Long status : statusAnteIds) {
        		whereIn.or(prelacionAnteCapHist.statusAntecedente.id.eq(status));
        	}
        	where.and(whereIn.getValue());
        }
        
        query.where(where);        
        query.orderBy(prelacionAnteCapHist.cargaTrabajo.asc(),prelacionAnteCapHist.documento.asc(),prelacionAnteCapHist.numeroPredio.asc());
        long totalFound = query.fetchCount();
        log.debug("Total de capturas-masivas encontradas="+ totalFound);
        
        //setOrder(query, pageable, prelacionAnteCapHist);
        
        List<PrelacionAnteCapHist> results = query.fetch();
        return new PageImpl<PrelacionAnteCapHist>(results, pageable, totalFound);
    }
    
    /*private void setOrder(JPQLQuery<PrelacionAnteCapHist> query, Pageable pageable, QPrelacionAnteCapHist prelacionAnteCapHist){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "paterno":
                            query.orderBy(new OrderSpecifier<String>(direction, prelacionAnteCapHist.));
                            break;
                        case "materno":
                            query.orderBy(new OrderSpecifier<String>(direction, persona.materno));
                            break;
                        case "nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, persona.nombre));
                            break;                                                                  
                        case "tipoPersona.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, persona.tipoPersona.nombre));
                            break;                        
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
	} */

}
