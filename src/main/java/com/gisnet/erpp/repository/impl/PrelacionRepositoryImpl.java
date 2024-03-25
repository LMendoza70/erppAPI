package com.gisnet.erpp.repository.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.BitacoraActoRechazoService;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.PrelacionRepositoryCustom;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.ConsultaPrelacionVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.util.CollectionUtils;

@Transactional(readOnly = true)
public class PrelacionRepositoryImpl extends QueryDslRepositorySupport implements PrelacionRepositoryCustom {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired 
	ActoRepository actoRepository;

    @Autowired
    BitacoraActoRechazoService bitacoraActoRechazoService;
	
    PrelacionRepositoryImpl() {
        super(Prelacion.class);
    }
    

    private JPQLQuery<Prelacion> getQueryFrom(QPrelacion qEntity) {
        return from(qEntity);
    }
    //JADV-SUSPENSION
    public Page<Prelacion> findAllByStatusAndUsuario(Usuario usuario,
    		Integer tipoUsuario,
    		Integer estado,
												   List<Constantes.Status> status,
                                                   Long folio,
                                                   Date fechaIngreso,
                                                   Date fechaEnvioFirma,
                                                   Date fechaBaja,
                                                   Usuario solicitante,
                                                   Pageable pageable) {
        QPrelacion prelacion = QPrelacion.prelacion;

        //Clear time part of date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        LocalDate fechaIngresoIni = null;
        LocalDate fechaEnvioFirmaIni = null;
        LocalDate fechaBajaIni = null;
        try {

            fechaIngresoIni= fechaIngreso!= null ? new LocalDateTime(fechaIngreso).toLocalDate() : null ;
            fechaEnvioFirmaIni = fechaEnvioFirma != null ? new LocalDateTime(fechaEnvioFirma).toLocalDate() : null;
            fechaBajaIni = fechaBaja != null ? new LocalDateTime(fechaBaja).toLocalDate() : null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        JPQLQuery<Prelacion> query = getQueryFrom(prelacion);
        query.distinct();
        BooleanBuilder where = new BooleanBuilder();

        where.and (prelacion.consecutivo.gt(0));
        where.and(prelacion.consecutivo.isNotNull());

        if (usuario != null) {
        	where   .or(prelacion.usuarioAnalista.id.longValue().eq(usuario.getId().longValue()))
			.or(prelacion.usuarioCalificador.id.longValue().eq(usuario.getId().longValue()));
        }
       /* 	if (tipoUsuario == 1)//orm
        			where.and(prelacion.usuarioAnalista.eq(usuario));
        	if (tipoUsuario == 2)
        			where.and(prelacion.usuarioCalificador.eq(usuario)); 
				*/
        if (folio != null ) {
            where.and(prelacion.consecutivo.longValue().eq(folio) );
        }
        
        if (fechaIngreso!= null ) {
            where.and(prelacion.fechaIngreso.between (fechaIngresoIni.toDate(), fechaIngresoIni.plusDays(1).toDate()));
        }

        if (fechaEnvioFirma != null ) {
            where.and(prelacion.fechaEnvioFirma.between (fechaEnvioFirmaIni.toDate(), fechaEnvioFirmaIni.plusDays(1).toDate()));
        }

        if (fechaBaja != null ) {
            where.and(prelacion.fechaBaja.between (fechaBajaIni.toDate(), fechaBajaIni.plusDays(1).toDate()));
        }

        if (solicitante != null) {
            where.and (prelacion.usuarioSolicitan.eq(solicitante));
        }

        //log.debug("== Estado ==");
        //log.debug(estado.toString());
        //log.debug(status.toString());

        if (status != null) {
        	if (estado == null || estado == 1) // Pendientes
                //where.and(prelacion.status.id.in (1,2,3,4,5,6,7,9,10,11));
        		 where.and(prelacion.status.id.in (4,5));//(11));  para la visualizacion del boton buscar para calificiadores y analista 
        	    //where.and(prelacion.status.id.loe(status.getIdStatus()));
        	else if (estado == 2) // Concluidos
                //where.and(prelacion.status.id.eq(Long.valueOf(8)) );
        	where.and(prelacion.status.id.in (8,10));
        		//where.and(prelacion.status.id.gt(status.getIdStatus()));
        }
        else {
        	where.and(prelacion.status.id.in (1,2,3,4,5,6,7,8,9,10,11));
        }

        query.where(where);
        long totalFound = query.fetchCount();
        
        setOrder(query, pageable, prelacion);
        
        query.orderBy(prelacion.id.asc());
       
        List<Prelacion> results = query.fetch();
       
        return new PageImpl<Prelacion>(results, pageable, totalFound);
    }
    
    @Override
    public List<Prelacion> findAllByStatusAndUsuarioCoordinador(
            List <Usuario> usuario,
            List <Constantes.Status> status,
            Long tipo,
            Long folio,
            Date fechaIngreso, Date fechaEnvioFirma,
            Usuario solicitante, Usuario notario, Usuario calificador,
            Integer estado, Long oficina, Pageable pageable) {


        QPrelacion prelacion = QPrelacion.prelacion;


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        LocalDate fechaIngresoIni = null;
        LocalDate fechaEnvioFirmaIni = null;
        try {

            fechaIngresoIni= fechaIngreso!= null ? new LocalDateTime(fechaIngreso).toLocalDate() : null ;
            fechaEnvioFirmaIni = fechaEnvioFirma != null ? new LocalDateTime(fechaEnvioFirma).toLocalDate() : null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        JPQLQuery<Prelacion> query = getQueryFrom(prelacion);
        query.distinct();
        BooleanBuilder where = new BooleanBuilder();

        where.and (prelacion.consecutivo.isNotNull());

        if (tipo != null ) {
            where.and(prelacion.prioridad.id.eq (tipo) );
        }

        if (folio != null ) {
            where.and(prelacion.consecutivo.longValue().eq(folio) );
        }

        if (fechaIngreso!= null ) {
            where.and(prelacion.fechaIngreso.between (fechaIngresoIni.toDate(), fechaIngresoIni.plusDays(1).toDate()));
        }

        if (fechaEnvioFirma != null ) {
            where.and(prelacion.fechaEnvioFirma.between (fechaEnvioFirmaIni.toDate(), fechaEnvioFirmaIni.plusDays(1).toDate()));
        }

        if (solicitante != null) {
            where.and (prelacion.usuarioSolicitan.eq(solicitante));
        }

        if (notario != null) {
            where.and (prelacion.usuarioNotario.eq(notario));
        }

        if (calificador != null) {
            where.and (prelacion.usuarioCalificador.eq(calificador));
        }
        
        /*if(solicitante == null && notario == null && calificador == null ) {
        	 where.and (prelacion.usuarioCalificador.in(usuario));
        	 where.and (prelacion.usuarioAnalista.in(usuario));
        }*/

        log.debug("== Estado ==");
        log.debug(status + " " + estado.toString());

        if (status != null) {
            //where.and(prelacion.status.id.eq(status.getIdStatus()) );
            //IHM PENDIENTE INTEGRAR List<Constantes.status> a model.status
            where.and(prelacion.status.id.in(6L,16L,Constantes.Status.PENDIENTE_DEVOLUCION_DOCUMENTO.getIdStatus()) );
            /*if (estado == 1) // Pendientes
                where.and(prelacion.status.id.in (1,2,3,4,5,6,7,9,10,11));
                //where.and(prelacion.status.id.loe(status.getIdStatus()));
            else if (estado == 2) // Concluidos
                where.and(prelacion.status.id.eq(Long.valueOf(8)) );
                */
            //where.and(prelacion.status.id.gt(status.getIdStatus()));
        }
        log.debug("== Oficina == "+ oficina);
        if (oficina != null) {
        	where.and(prelacion.oficina.id.eq(oficina));
        }
        /*if (status != null) {
            if (estado == 1) // Pendientes
                where.and(prelacion.status.id.eq(status.getIdStatus()));
            else if (estado == 2) // Concluidos
                where.and(prelacion.status.id.gt(status.getIdStatus()));
        }*/

        // where.and(prelacion.status.id.eq(status.getIdStatus()));

        query.where(where);
        long totalFound = query.fetchCount();

        //setOrder(query, pageable, prelacion);

        query.orderBy(prelacion.id.asc());

        List<Prelacion> results = query.fetch();
        return results; //new PageImpl<Prelacion>(results, pageable, totalFound);
    }

    private void setOrder(JPQLQuery<Prelacion> query, Pageable pageable, QPrelacion prelacion){
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "consecutivo":
                            query.orderBy(new OrderSpecifier<Long>(direction, prelacion.consecutivo.longValue()));
                            break;
                        case "fechaIgreso":
                        case "fechaRegistro":
                        	query.orderBy(new OrderSpecifier<Date>(direction, prelacion.fechaIngreso));
                            break;
                        case "fechaEnvioFirma":
                            query.orderBy(new OrderSpecifier<Date>(direction, prelacion.fechaEnvioFirma));
                            break;
                        case "fechaBaja":
                            query.orderBy(new OrderSpecifier<Date>(direction, prelacion.fechaBaja));
                            break;


                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    }

    @Override
    public Page<Prelacion> findAllByActosRechazados(Usuario usuario, Constantes.Status status, Long tipo, Long folio, Date fechaIngreso, Date fechaEnvioFirma, Usuario solicitante, Usuario notario, Usuario calificador, Pageable pageable) {
        QPrelacion prelacion = QPrelacion.prelacion;


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        LocalDate fechaIngresoIni = null;
        LocalDate fechaEnvioFirmaIni = null;
        try {

            fechaIngresoIni= fechaIngreso!= null ? new LocalDateTime(fechaIngreso).toLocalDate() : null ;
            fechaEnvioFirmaIni = fechaEnvioFirma != null ? new LocalDateTime(fechaEnvioFirma).toLocalDate() : null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        JPQLQuery<Prelacion> query = getQueryFrom(prelacion);
        query.distinct();
        BooleanBuilder where = new BooleanBuilder();

        where.and (prelacion.consecutivo.isNotNull());

        if (tipo != null ) {
            where.and(prelacion.prioridad.id.eq (tipo) );
        }

        if (folio != null ) {
            where.and(prelacion.consecutivo.longValue().eq(folio) );
        }

        if (fechaIngreso!= null ) {
            where.and(prelacion.fechaIngreso.between (fechaIngresoIni.toDate(), fechaIngresoIni.plusDays(1).toDate()));
        }

        if (fechaEnvioFirma != null ) {
            where.and(prelacion.fechaEnvioFirma.between (fechaEnvioFirmaIni.toDate(), fechaEnvioFirmaIni.plusDays(1).toDate()));
        }

        if (solicitante != null) {
            where.and (prelacion.usuarioSolicitan.eq(solicitante));
        }

        if (notario != null) {
            where.and (prelacion.usuarioNotario.eq(notario));
        }

        if (calificador != null) {
            where.and (prelacion.usuarioCalificador.eq(calificador));
        }

        log.debug("== Estado ==");
        log.debug(status.toString());

        where.and(prelacion.status.id.eq(status.getIdStatus()));
        query.where(where);
        long totalFound = query.fetchCount();

        setOrder(query, pageable, prelacion);

        query.orderBy(prelacion.id.asc());

        List<Prelacion> results = query.fetch();
        return new PageImpl<Prelacion>(results, pageable, totalFound);
    }
    
    @Override
    public Page<ConsultaPrelacionVO> findAllByRechazadas(
			Pageable pageable,
			Integer consecutivo, Date desdeFecha, Date hastaFecha, Integer escritura, 
			Long notarioId,
			Long contratanteId,
			Long tipoActoId,
			Long registradorId) {
    	
        LocalDate fechaDesde = null;
        LocalDate fechaHasta = null;
        try {
        	fechaDesde = desdeFecha != null ? new LocalDateTime( desdeFecha ).toLocalDate() : null;
        	fechaHasta = hastaFecha != null ? new LocalDateTime( hastaFecha ).toLocalDate() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	QPrelacion qPrelacion = QPrelacion.prelacion;
    	QActo qActo = QActo.acto;
		QActoDocumento qActoDocumento= QActoDocumento.actoDocumento;
    	QDocumento qDocumento = QDocumento.documento;
		QPrelacionContratante qPrelacionContratante = QPrelacionContratante.prelacionContratante;
		
    	JPQLQuery<Prelacion> query = getQueryFrom( qPrelacion );
        //query.distinct();
        BooleanBuilder where = new BooleanBuilder();
        
        // Status de rechazo
        where.and( qPrelacion.status.id.in(9L,10L) );
        
        if( consecutivo != null ) {
            where.and( qPrelacion.consecutivo.eq(consecutivo) );
        }
        if( fechaDesde != null && fechaHasta != null ) {
        	where.and( qPrelacion.fechaIngreso.between(fechaDesde.toDate(),fechaHasta.plusDays(1).toDate()) );
        } else if( fechaDesde != null ) {
        	where.and( qPrelacion.fechaIngreso.goe(fechaDesde.toDate()) );
        } else if( fechaHasta != null ) {
        	where.and( qPrelacion.fechaIngreso.loe(fechaHasta.plusDays(1).toDate()) );
        }        
        if( escritura != null || notarioId != null || tipoActoId != null ) {
        	query.innerJoin( qPrelacion.actosParaPrelacions, qActo );
            where.and( qActo.prelacion.id.eq(qPrelacion.id) );
        }
        if( escritura != null || notarioId != null ) {
        	query.innerJoin( qActo.actoDocumentosParaActos, qActoDocumento );
        	query.innerJoin( qActoDocumento.documento, qDocumento );
        	where.and( qActoDocumento.acto.id.eq(qActo.id) );
        	where.and( qDocumento.id.eq(qActoDocumento.documento.id) );
        }
        if( escritura != null ) {
        	where.and( qDocumento.numero.eq(escritura) );
        }
        if( notarioId != null ) {
        	where.and( qDocumento.notario.id.eq(notarioId) );
        }
        if( contratanteId != null ) {
        	query.innerJoin( qPrelacion.prelacionContratantesParaPrelacions, qPrelacionContratante );
        	where.and( qPrelacionContratante.prelacion.id.eq(qPrelacion.id) );
        	where.and( qPrelacionContratante.id.eq(contratanteId) );
        }
        if( tipoActoId != null ) {
            where.and( qActo.tipoActo.id.eq(tipoActoId) );
        }
        if( registradorId != null ) {
            where.and( qPrelacion.usuarioCalificador.id.eq(registradorId).
            		or(qPrelacion.usuarioAnalista.id.eq(registradorId)) );
        }
        
        query.where(where);
        long totalFound = query.fetchCount();
        
        setOrderRechazadas(query, pageable, qPrelacion);
        
        query.orderBy(qPrelacion.id.asc());

        List<Prelacion> results = query.fetch();
        results = getDistinct( results );
        
        Page<Prelacion> prelPage = new PageImpl<Prelacion>(results, pageable, totalFound);
        final Page<ConsultaPrelacionVO> prelacionVO = prelPage.map (this :: convertToConsultaPrelacionVO);
        return prelacionVO;
    }

	private List<Prelacion> getDistinct(List<Prelacion> results) {
		ArrayList<Prelacion> prelacionesList = new ArrayList<Prelacion>();
		
		HashSet<Long> prelacionesIds = new HashSet<Long>(); 
		for( Prelacion prelacion : results ) {
			if( prelacionesIds.add( prelacion.getId() ) ) {
				prelacionesList.add( prelacion );
			}
		}
		
		return prelacionesList;
	}


	private void setOrderRechazadas(JPQLQuery<Prelacion> query, Pageable pageable, QPrelacion qPrelacion) {
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "consecutivo":
                            query.orderBy(new OrderSpecifier<Long>(direction, qPrelacion.consecutivo.longValue()));
                            break;
                        case "fechaRegistro":
                            query.orderBy(new OrderSpecifier<Date>(direction, qPrelacion.fechaIngreso));
                            break;
                        case "strEscrituras":
                        	query.orderBy(new OrderSpecifier<Integer>(direction, qPrelacion.actosParaPrelacions.any().actoDocumentosParaActos.any().documento.numero ));
                            break;
                        case "notario":
                        	log.debug( "=1=> notario = "+qPrelacion.actosParaPrelacions.any().actoDocumentosParaActos.any().documento.notario );
                        	log.debug( "=2=> persona = "+qPrelacion.actosParaPrelacions.any().actoDocumentosParaActos.any().documento.notario.persona );
                        	log.debug( "=3=> nombre = "+ qPrelacion.actosParaPrelacions.any().actoDocumentosParaActos.any().documento.notario.persona.nombre );
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.actosParaPrelacions.any().actoDocumentosParaActos.any().documento.notario.persona.nombre ));
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.actosParaPrelacions.any().actoDocumentosParaActos.any().documento.notario.persona.paterno ));
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.actosParaPrelacions.any().actoDocumentosParaActos.any().documento.notario.persona.materno ));
                            break;
                        case "contratantes":
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.prelacionContratantesParaPrelacions.any().nombre ));
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.prelacionContratantesParaPrelacions.any().paterno ));
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.prelacionContratantesParaPrelacions.any().materno ));
                            break;
                        case "strActos":
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.actosParaPrelacions.any().tipoActo.nombre ));
                        	break;
                        case "registrador":
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.usuarioAnalista.persona.nombre ));
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.usuarioAnalista.persona.paterno ));
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.usuarioAnalista.persona.materno ));
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.usuarioCalificador.persona.nombre ));
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.usuarioCalificador.persona.paterno ));
                        	query.orderBy(new OrderSpecifier<String>(direction, qPrelacion.usuarioCalificador.persona.materno ));
                        	break;
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }		
	}


	@Override
	public Page<ConsultaPrelacionVO> findAllByStatus(Usuario usuario, Integer status, Long folio, Integer folioPredio, Date fechaI, Date fechaH, Date fechaEF,
			Date fechaB, Long solicitante, Long registrador,Long calificador, Pageable pageable,
			Long contratante,
			String referencia, Long region, Long area,
			Long coordinador,
			Long actoId,
			Long notarioId,
			String escritura,
			Boolean pantallaCoordinador,Integer prioridad) {
		
		log.debug( "====> referencia = "+referencia );
		log.debug( "====> region = "+region );
        log.debug( "====> area = "+area );
        log.debug( "====> calificador = "+calificador );
        log.debug( "====> registrador = "+registrador );
		log.debug( "====> coordinador = "+coordinador );
        log.debug( "====> contratante = "+contratante );
        log.debug( "====> pantallaCoordinador = "+pantallaCoordinador );        
        log.debug( "====> usuario = "+usuario );
		
		QPrelacion prelacion = QPrelacion.prelacion;
		QActo qActo = QActo.acto;
		QDocumento qDocumento = QDocumento.documento;
		QActoDocumento qActoDocumento= QActoDocumento.actoDocumento;

        //Clear time part of datew
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        LocalDate fechaIngresoIni = null;
        LocalDate fechaIngresoFin = null;
        LocalDate fechaEnvioFirmaIni = null;
        LocalDate fechaBajaIni = null;
        try {

            fechaIngresoIni= fechaI!= null ? new LocalDateTime(fechaI).toLocalDate() : null ;
            fechaIngresoFin = fechaH!= null ? new LocalDateTime(fechaH).toLocalDate() : null ;
            fechaEnvioFirmaIni = fechaEF != null ? new LocalDateTime(fechaEF).toLocalDate() : null;
            fechaBajaIni = fechaB != null ? new LocalDateTime(fechaB).toLocalDate() : null;

        } catch (Exception e) {
            e.printStackTrace();
        }

        JPQLQuery<Prelacion> query = getQueryFrom(prelacion);
        query.distinct();
        BooleanBuilder where = new BooleanBuilder();

        log.debug(prelacion.status.nombre.toString());
        where.and (prelacion.consecutivo.gt(0));

        if (usuario != null)
        	where   .or(prelacion.usuarioAnalista.eq(usuario))
        			.or(prelacion.usuarioCalificador.eq(usuario));

        if (folio != null ) {
            where.and(prelacion.consecutivo.longValue().eq(folio) );
        }
        
        
        
        System.out.println("PRIORIDAD  " + prioridad);
        if (prioridad != null ) {
            where.and(prelacion.prioridad.id.eq(prioridad.longValue()) );
        }
        
        if( folioPredio != null ) {
        	QPrelacionPredio prelacionPredio = QPrelacionPredio.prelacionPredio;
        	query.innerJoin(prelacion.prelacionPrediosParaPrelacions, prelacionPredio);
        	where.and( prelacionPredio.prelacion.id.eq( prelacion.id ) );
        	where.and( prelacionPredio.estatus.eq( 1 ) );
        	where.and( prelacionPredio.predio.noFolio.eq( folioPredio ) );
        }
        
        if( fechaI != null && fechaH != null ) {
        	where.and( prelacion.fechaIngreso.between(fechaIngresoIni.toDate(),fechaIngresoFin.plusDays(1).toDate()) );
        } else if( fechaI != null ) {
        	where.and( prelacion.fechaIngreso.goe(fechaIngresoIni.toDate()) );
        } else if( fechaH != null ) {
        	where.and( prelacion.fechaIngreso.loe(fechaIngresoFin.plusDays(1).toDate()) );
        }        

        if (fechaEF!= null ) {
            where.and(prelacion.fechaEnvioFirma.between (fechaEnvioFirmaIni.toDate(), fechaEnvioFirmaIni.plusDays(1).toDate()));
        }

        if (fechaB!= null ) {
            where.and(prelacion.fechaBaja.between (fechaBajaIni.toDate(), fechaBajaIni.plusDays(1).toDate()));
        }

        if (solicitante != null) {
            where.and (prelacion.usuarioSolicitan.id.eq(solicitante));
        }

        if (calificador != null) {
            where.and (prelacion.usuarioCalificador.id.eq(calificador).or(prelacion.usuarioAnalista.id.eq(calificador)));
        }
        
        if (registrador != null) {

            where.and (prelacion.usuarioCalificador.id.eq(registrador).or(prelacion.usuarioAnalista.id.eq(registrador)));
        }

        if( coordinador != null ) {
        	where.and( prelacion.usuarioCoordinador.id.eq(coordinador) );
        }
        
        if( contratante != null ) {
        	QPrelacionContratante prelacionContratante = QPrelacionContratante.prelacionContratante;
        	query.innerJoin(prelacion.prelacionContratantesParaPrelacions, prelacionContratante);
        	where.and (prelacionContratante.prelacion.id.eq(prelacion.id));
        	where.and (prelacionContratante.id.eq(contratante));
        }

        if (status != null && status != 0) {
            where.and(prelacion.status.id.eq(Long.valueOf(status )) );
        }
        else {
        	if( pantallaCoordinador ) {
        		where.and(prelacion.status.id.in ( 1,2,4,5,6,7,8,9,10,11,16,17,22,23,24,25 ) );
        	} else {
        		where.and(prelacion.status.id.in ( 1,2,3,4,5,6 ) );
        	}
        }

        if (actoId != null || escritura != null || notarioId != null || area != null) {
            query.innerJoin(prelacion.actosParaPrelacions, qActo);
            where.and (qActo.prelacion.id.eq(prelacion.id));            
        }
        
        if (actoId != null) {
            where.and (qActo.tipoActo.id.eq(actoId));
        }
        
        if (escritura != null || notarioId != null) {
        	query.innerJoin(qActo.actoDocumentosParaActos,qActoDocumento);
        	query.innerJoin(qActoDocumento.documento, qDocumento);
        	where.and(qActoDocumento.acto.id.eq(qActo.id));
        	
        }
        
        if (escritura != null) {
        	where.and(qDocumento.numero2.eq (escritura) );
        }
        
        if (notarioId != null ) {
        	where.and(qDocumento.notario.id.eq (notarioId) );
        }
        
        if( referencia != null ) {
        	QRecibo qRecibo = QRecibo.recibo;
        	query.innerJoin( prelacion.recibosParaPrelacions,qRecibo );
        	where.and( qRecibo.prelacion.id.eq( prelacion.id ) );
        	where.and( qRecibo.referencia.eq( referencia) );
        }

        if( region != null ) {
        	where.and( prelacion.oficina.id.eq( region ) );
        }
        
        if( area != null ) {
        	QTipoActo qTipoActo = QTipoActo.tipoActo;
        	QClasifActo qClasifActo = QClasifActo.clasifActo;
        	QAreaClasifActo qAreaClasifActo = QAreaClasifActo.areaClasifActo;
        	
        	query.innerJoin( qActo.tipoActo, qTipoActo );
        	query.innerJoin( qTipoActo.clasifActo, qClasifActo );
        	query.innerJoin( qClasifActo.areaClasifActosParaClasifActos, qAreaClasifActo );
        	
        	where.and( qTipoActo.id.eq( qActo.tipoActo.id ) );
        	where.and( qTipoActo.activo.isTrue() );
        	where.and( qClasifActo.id.eq( qTipoActo.clasifActo.id ) );
        	where.and( qClasifActo.id.eq( qAreaClasifActo.clasifActo.id ) );
        	where.and( qAreaClasifActo.area.id.eq( area ) );
        }
        
        query.where(where);
        long totalFound = query.fetchCount();
        
        setOrder(query, pageable, prelacion);
        //Punto 28 Se cambia a desc
        query.orderBy(prelacion.consecutivo.desc());
        query.orderBy(prelacion.id.asc());

        List<Prelacion> results = query.fetch();

        Long ENTREGADO_AL_USUARIO = 8L;

        results.forEach(prelacionEntity -> {

            if(prelacionEntity.getStatus().getId().equals(ENTREGADO_AL_USUARIO)) {

                //Esta logica es la que se encuentra en ActoService.actoRechazo(id); pero se puso aqui directo para evitar problemas de referencias circulares
                List<BitacoraActoRechazo> bitacora;
                BitacoraActoRechazo rechazado = null;
                List<Acto> actosRechazados =  actoRepository.findActosByPrelacionIdAndRechazados(prelacionEntity.getId());
                if(!CollectionUtils.isEmpty(actosRechazados)) {

                    bitacora = bitacoraActoRechazoService.findBitacoraByActo(actosRechazados.get(0).getId());
                    if(!CollectionUtils.isEmpty(bitacora)) {
                        rechazado= bitacora.get(0);
                    }
                }

                if (rechazado == null) {
                    prelacionEntity.getStatus().setNombre("ENTREGADO AL USUARIO (Favorable)");
                } else {
                    prelacionEntity.getStatus().setNombre("ENTREGADO AL USUARIO (Rechazado)");
                }
            }
        });
        Page<Prelacion> prelPage = new PageImpl<Prelacion>(results, pageable, totalFound);
        final Page<ConsultaPrelacionVO> prelacionVO = prelPage.map (this :: convertToConsultaPrelacionVO);
        return prelacionVO;
	}

	private ConsultaPrelacionVO convertToConsultaPrelacionVO(final Prelacion prel) {
	    final ConsultaPrelacionVO vo = new ConsultaPrelacionVO();

	    final List<Acto> actos = this.getActos(prel.getId());
	    final List <Documento > escrituras = this.getEscrituras (actos);
	    
	    List<Notario> notarios;
	    Notario notario = prel.getNotario();
	    if( notario == null ) {
	    	notarios = this.getNotarios(escrituras);
	    } else {
	    	notarios = new ArrayList<Notario>();
	    	notarios.add( notario );
	    }
	    Usuario registrador = null;
	    final Usuario calificador = prel.getUsuarioCalificador();
	    final Usuario analista = prel.getUsuarioAnalista();
	    final Usuario coordinador = prel.getUsuarioCoordinador();
	    if (prel.getStatus().getId() == 4 )
	        registrador = analista;
	    else if (prel.getStatus().getId() == 5)
	        registrador = calificador;
	    else if (prel.getStatus().getId() == 6)
            registrador = coordinador;
        else 
	    	registrador = analista!=null? analista: calificador;

	    vo.setId(prel.getId());
	    
	    vo.setPrioridad(prel.getPrioridad());
	    vo.setActos(actos);
	    vo.setConsecutivo(prel.getConsecutivo());
	    vo.setFechaRegistro(prel.getFechaIngreso());
	    vo.setFechaEnvioFirma(prel.getFechaEnvioFirma());
	    vo.setFechaBaja (prel.getFechaBaja());
	    vo.setEscrituras(escrituras);
	    vo.setNotarios(notarios);
	    vo.setRegistrador(registrador);
        vo.setUsuarioAnalista(prel.getUsuarioAnalista());
        vo.setRegistrador(prel.getUsuarioCalificador());
        //vo.setUsuarioCalificador(prel.getUsuarioCalificador());
        vo.setStatus(prel.getStatus());
        if(prel.getEs_digitalizado()!=null){
            vo.setEsDigitalizado(prel.getEs_digitalizado());
        }else {
            vo.setEsDigitalizado(null);
        }        
		vo.setMunicipio( prel.getMunicipio() );
	    vo.setUsuarioCoordinador( prel.getUsuarioCoordinador() );
	    vo.setId_entrada(prel.getId_entrada());
	    
	    Set<Recibo> recibos = prel.getRecibosParaPrelacions();
	    StringBuilder sb = new StringBuilder();
	    for( Recibo r: recibos ) {
	    	sb.append( r.getReferencia()+", " );
	    }
	    if( sb.length() > 2 ) {
	    	sb.delete( sb.length()-2, sb.length() );
	    }
	    
	    vo.setNumerosRecibos( sb.toString() );
	    
	    TipoFolio tipoFolio = null;
	    String tipoFolioNombre = null;
	    Set<PrelacionPredio> prelacionPredios = prel.getPrelacionPrediosParaPrelacions();
	    for( PrelacionPredio pp : prelacionPredios ) {
	    	tipoFolio = pp.getTipoFolio();
	    	if( tipoFolio != null ) {
		    	tipoFolioNombre = tipoFolio.getNombre();
		    	if( tipoFolioNombre != null ) {
		    		break;
		    	}
	    	}
	    }
	    
	    vo.setTipoFolio(tipoFolioNombre);
	    
	    sb = new StringBuilder();
	    Set<PrelacionContratante> contratantesList = prel.getPrelacionContratantesParaPrelacions();
	    for( PrelacionContratante pc : contratantesList ) {
	    	if( pc.getDd() == null && pc.getUv() == null ) {
				continue;
			}
	    	sb.append( pc.getNombreCompleto()+", " );
	    }
	    if( sb.length() > 2 ) {
	    	sb.delete( sb.length()-2, sb.length() );
	    }
	    
	    vo.setContratantes( sb.toString() );
	    
	    return vo;
	}


	
	private List<Notario> getNotarios(List <Documento > escrituras) {
		List<Notario> notarios = new ArrayList<Notario>();
		
		if (escrituras == null ) {
			return null;
		}
		if (escrituras.size() == 0 ) {
			return null;
		}
		
		for( Documento escritura : escrituras ) {
			notarios.add( escritura.getNotario() );
		}
		
		List<Notario> filtrados = notarios.stream().distinct().collect(Collectors.toList()); 
		
		return filtrados;
	}


	private List <Documento> getEscrituras(List<Acto> actos) {
		final List<Documento> documentos = new ArrayList<Documento>();
		if (actos != null && actos.size()>0) {
			for (Acto acto: actos) {
				if (acto.getActoDocumentosParaActos() != null ) {
					//documentos.add (acto.getActoDocumentosParaActos().stream().collect(collector)().max(doc -> doc.version));
					Optional<ActoDocumento> act = acto.getActoDocumentosParaActos().stream()
							.collect( Collectors.minBy(Comparator.comparing(ActoDocumento::getVersion) ) );
					if (act.isPresent())
					documentos.add (act.get().getDocumento() );
				}
			}
		}
		return documentos;
	}


	private List<Acto> getActos(Long id) {
		
//		List<Acto> actos = new ArrayList<Acto>();
		
//		List<TipoActo> tiposActo = new ArrayList<TipoActo>();
//		actos.addAll(actoRepository.findAllActosByPrelacionVf(id));
//		
//		for(Acto acto: actos) {
//			tiposActo.add(acto.getTipoActo());
//		}
//		
//		List<TipoActo> filtrados = tiposActo.stream().distinct().collect(Collectors.toList()); 
//		actos.clear();
//		
//		for(TipoActo tipoActo :filtrados) {
//			Acto act = new Acto();
//			act.setTipoActo(tipoActo);
//			act.setVf(false);
//			actos.add(act);
//		}

//		return actos;
		return actoRepository.findByPrelacionId(id);
	}
}
