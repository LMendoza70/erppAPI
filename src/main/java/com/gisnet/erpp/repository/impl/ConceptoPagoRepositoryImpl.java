package com.gisnet.erpp.repository.impl;

import java.util.List;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.service.PrelacionService;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.repository.ConceptoPagoRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

import javax.persistence.EntityManager;

@Transactional(readOnly = true)
public class ConceptoPagoRepositoryImpl extends QueryDslRepositorySupport implements ConceptoPagoRepositoryCustom{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PrelacionService prelacionService;

    ConceptoPagoRepositoryImpl() {
        super(ConceptoPago.class);
    }

    private JPQLQuery<ConceptoPago> getQueryFrom(QConceptoPago qEntity) {
        return from(qEntity);
    }

    public Page<ConceptoPago> findAllBy(Pageable pageable, String nombre, Long areaId, Long clasifActoId) {

        QConceptoPago conceptoPago = QConceptoPago.conceptoPago;
        JPQLQuery<ConceptoPago> query = getQueryFrom(conceptoPago);
        BooleanBuilder where = new BooleanBuilder();
        if (nombre != null && nombre.length()>0) {
            where.and(conceptoPago.nombre.containsIgnoreCase(nombre));
        }

        if (areaId!=null){
        	where.and(conceptoPago.area.id.eq(areaId));
        }
        
        if (clasifActoId !=null){
        	where.and(conceptoPago.clasifActo.id.eq(clasifActoId));
        }

        query.where(where);
        long totalFound = query.fetchCount();

        setOrder(query, pageable, conceptoPago);

        query.orderBy(conceptoPago.id.asc());
        List<ConceptoPago> results = query.fetch();
        return new PageImpl<ConceptoPago>(results, pageable, totalFound);
    }


    public List<ConceptoPago> findAllByActosPrelacion(Long idPrelacion) {

        EntityManager entityManager;
        Querydsl          querydsl;

        Prelacion             prelacion = prelacionService.findOne (idPrelacion);
        QConceptoPago         conceptoPago = QConceptoPago.conceptoPago;
        QActo                 acto = QActo.acto;
        QConcPagoTipoActo     cpta = QConcPagoTipoActo.concPagoTipoActo;
        QTipoActo             tipoActo = QTipoActo.tipoActo;

        JPQLQuery<ConceptoPago> query = getQueryFrom(conceptoPago);

        BooleanBuilder where = new BooleanBuilder();

        query.innerJoin( conceptoPago, cpta.conceptoPago);
        query.innerJoin( cpta    .tipoActo,           tipoActo);
        query.innerJoin( tipoActo.actosParaTipoActos, acto);

        //where.and(acto.prelacion.id.eq(idPrelacion ));

        query.where(where);

        return query.fetch();

    }

    private void setOrder(JPQLQuery<ConceptoPago> query, Pageable pageable, QConceptoPago conceptoPago){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, conceptoPago.nombre));
                            break;                            
                        case "area.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, conceptoPago.area.nombre));
                            break;                        
                        case "clasifActo.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, conceptoPago.clasifActo.nombre));
                            break;
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 
}
