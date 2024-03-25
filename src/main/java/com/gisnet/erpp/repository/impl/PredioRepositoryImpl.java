package com.gisnet.erpp.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.vo.BusquedaPersonaVO;
import com.gisnet.erpp.vo.BusquedaPredioVO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;
import com.gisnet.erpp.repository.PredioRepositoryCustom;
import com.gisnet.erpp.repository.PredioRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.springframework.util.ObjectUtils.isEmpty;

@Transactional(readOnly = true)
public class PredioRepositoryImpl extends QueryDslRepositorySupport implements PredioRepositoryCustom {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    EntityManager em;


    PredioRepositoryImpl() {
        super(Predio.class);
    }


    private JPQLQuery<Predio> getQueryFrom(QPredio qEntity) {
        return from(qEntity);
    }

    public Page<Predio> findAllPageable(Integer noFolio, Pageable pageable,Long oficinaId) {
        QPredio           predio = QPredio.predio;
        JPQLQuery<Predio> query  = getQueryFrom(predio);
        BooleanBuilder    where  = new BooleanBuilder();

        if (noFolio != null) {
            where.and(predio.noFolio.eq(noFolio));
        }
        where.and(predio.oficina.id.eq(oficinaId));
        query.where(where);
        long totalFound = query.fetchCount();

        setOrder(query, pageable, predio);

        query.orderBy(predio.id.asc());
        List<Predio> results = query.fetch()
        							.stream()
        							.filter(item -> item.getNoFolio() != null && item.getNoFolio() > 2)
        							.collect(Collectors.toList());
        
        return new PageImpl<Predio>(results, pageable, totalFound);
    }

    private void setOrder(JPQLQuery<Predio> query, Pageable pageable, QPredio predio) {
        if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    switch (((order.getProperty()))) {
                        case "noFolio":
                            query.orderBy(new OrderSpecifier<Integer>(direction, predio.noFolio));
                            break;
                        case "id":
                        	query.orderBy(new OrderSpecifier<Long>(direction, predio.id));
                            break;
                        case "noLote":
                        	query.orderBy(new OrderSpecifier<String>(direction, predio.noLote));
                            break;
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    }

    public Page<Predio> findAllPrediosFraccionamientoPageable(Long paseId, String manzana, String lote,
			Pageable pageable) {
		return  this.findAllPrediosFraccionamiento(null,null,manzana,lote,paseId,pageable);
	}
	
	public Page<Predio> findAllPrediosFraccionamientoPageable(Long acto, Integer noFolio, String manzana, String lote,
			Pageable pageable) {
		return  this.findAllPrediosFraccionamiento(acto,noFolio,manzana,lote,null,pageable);
	}

	private Page<Predio> findAllPrediosFraccionamiento(Long acto, Integer noFolio, String manzana, String lote,
			Long paseId, Pageable pageable) {
		QPredio predio = QPredio.predio;
		QFoliosFracCond foliosFracCond = QFoliosFracCond.foliosFracCond;
		JPQLQuery<Predio> query = getQueryFrom(predio);
		BooleanBuilder where = new BooleanBuilder();

		if (acto != null) {
			where.and(foliosFracCond.acto.id.eq(acto));
		}

		if (paseId != null) {
			where.and(foliosFracCond.paseFracCond.id.eq(paseId));
		}
		if (noFolio != null) {
			where.and(predio.noFolio.eq(noFolio));
		}
		if (manzana != null) {
			where.and(predio.manzana.eq(manzana));
		}
		if (lote != null) {
			where.and(predio.noLote.eq(lote));
		}
		query.leftJoin(predio.foliosFracCondParaPredios, foliosFracCond);

		query.where(where);
		long totalFound = query.fetchCount();

		setOrder(query, pageable, predio);
		List<Predio> results = query.fetch();
		System.out.println(results);
		return new PageImpl<Predio>(results, pageable, totalFound);
	}


    @Override
    public Page<Predio> findPrediosByBusquedaVO(BusquedaPredioVO det, Set<BusquedaPersonaVO> busquedaPersonaVO, Set<BusquedaPersonaVO> personaMoral,Long oficinaId, Pageable pageable) {

        QPredio qPredio = QPredio.predio;

        List<PredioPersona> pp = new ArrayList<>();

		if (busquedaPersonaVO != null)
			pp = buscarTitulares(busquedaPersonaVO);
		if (personaMoral != null)
			pp = buscarTitulares(personaMoral);

		List<Long> idsPrediosTitulares = new ArrayList<>();
		if (!isEmpty(pp)) {
			for (PredioPersona pper : pp) {				
				if(pper.getActoPredio().getPredio()!=null) {
					idsPrediosTitulares.add(pper.getActoPredio().getPredio().getId());
				}
			}
			log.debug("Titulares de predios ==> {}", pp);
		}

        JPQLQuery<Predio> query = getQueryFrom(qPredio);

        //log.debug("Restriccion por linderos => {}", det.getLinderos());

        if (det != null && !isEmpty(det.getLinderos())) {

            for (Map.Entry<String, String> lindero : det.getLinderos().entrySet()) {
                QColindancia qColindancia = QColindancia.colindancia;

                query.innerJoin(qPredio.colindanciasParaPredios, qColindancia)
                        .on((qColindancia.orientacion.id.eq(Long.valueOf(lindero.getKey())))
                                .and(qColindancia.nombre.equalsIgnoreCase(lindero.getValue()))
                        );
            }
        }

        List<Predio> values = new ArrayList<>();



        BooleanBuilder wheres = buildQueryFromVo(det, idsPrediosTitulares, qPredio,oficinaId);

        log.debug(" === Cantidad de restricciones => {}", wheres.getValue());

        if (wheres.hasValue())  {
            query.where(wheres);

            Long totalFound = query.fetchCount();

            setOrder(query, pageable, qPredio);
            values = query.fetch();


            return new PageImpl<Predio>(values, pageable, totalFound);

        }

        return new PageImpl<Predio>(values, pageable, 0);



    }
    
    @Override
    public List<Predio> findPrediosByBusqueda(BusquedaPredioVO det) {

        QPredio qPredio = QPredio.predio;


        JPQLQuery<Predio> query = getQueryFrom(qPredio);


        if (!isEmpty(det.getLinderos())) {

            for (Map.Entry<String, String> lindero : det.getLinderos().entrySet()) {
                QColindancia qColindancia = QColindancia.colindancia;

                query.innerJoin(qPredio.colindanciasParaPredios, qColindancia)
                        .on((qColindancia.orientacion.id.eq(Long.valueOf(lindero.getKey())))
                                .and(qColindancia.nombre.equalsIgnoreCase(lindero.getValue()))
                        );
            }
        }

        List<Predio> values = new ArrayList<>();



        BooleanBuilder wheres = buildQueryFromVo(det, null, qPredio,null);

        log.debug(" === Cantidad de restricciones => {}", wheres.getValue());

        if (wheres.hasValue())  {
            query.where(wheres);


           
            values = query.fetch();


            return values;

        }

        return values;



    }


    public BooleanBuilder buildQueryFromVo(BusquedaPredioVO det, List<Long> idsPrediosTitulares , QPredio qPredio,Long oficinaId) {
        BooleanBuilder where = new BooleanBuilder();
      if(!det.isNull()) {
    	    
        if (det.getTipoVialidad() != null) {
            where.and(qPredio.tipoVialidad.id.eq(det.getTipoVialidad().getId()));
        }
        if (!isEmpty(det.getVialidad())) {
            where.and(qPredio.vialidad.containsIgnoreCase(det.getVialidad()));
        }
        if (!isEmpty(det.getNumExt())) {
            where.and(qPredio.numExt.equalsIgnoreCase(det.getNumExt()));
        }
        if (!isEmpty(det.getNumInt())) {
            where.and(qPredio.numInt.equalsIgnoreCase(det.getNumInt()));
        }
        if (!isEmpty(det.getEdificio())) {
            where.and(qPredio.edificio.equalsIgnoreCase(det.getEdificio()));
        }
        if (!isEmpty(det.getNivel())) {
            where.and(qPredio.nivel.id.eq(det.getNivel().getId()));
        }
        if (!isEmpty(det.getAsentamiento())) {
            where.and(qPredio.asentamiento.equalsIgnoreCase(det.getAsentamiento()));
            where.or(qPredio.asentamiento.like("%"+det.getAsentamiento()+"%"));
        }
        if (!isEmpty(det.getEstado())) {
            where.and(qPredio.municipio.estado.id.eq(det.getEstado().getId()));
        }
        if (!isEmpty(det.getCodigoPostal())) {
            where.and(qPredio.codigoPostal.eq(det.getCodigoPostal()));
        }
        if (!isEmpty(det.getClaveCatastral())) {
            where.and(qPredio.claveCatastral.eq(det.getClaveCatastral()));
        }
        if (!isEmpty(det.getCuentaCatastral())) {
            where.and(qPredio.cuentaCatastral.eq(det.getCuentaCatastral()));
        }
        if (!isEmpty(det.getClaveCatastralEstandard())) {
            where.and(qPredio.claveCatastralEstandard.eq(det.getClaveCatastralEstandard()));
        }
        if (!isEmpty(det.getNoLote())) {
            where.and(qPredio.noLote.eq(det.getNoLote()));
        }
        if (!isEmpty(det.getLocalidadSector())) {
            where.and(qPredio.localidadSector.eq(det.getLocalidadSector()));
        }
        if (!isEmpty(det.getManzana())) {
            where.and(qPredio.manzana.eq(det.getManzana()));
        }
        if (!isEmpty(det.getZona())) {
            where.and(qPredio.zona.eq(det.getZona()));
        }
        if (!isEmpty(det.getSuperficie())) {
            where.and(qPredio.superficie.eq(det.getSuperficie()));
        }
        if (!isEmpty(det.getUnidadMedida())) {
            where.and(qPredio.unidadMedida.id.eq(det.getUnidadMedida().getId()));
        }
        if (!isEmpty(det.getUsoSuelo())) {
            where.and(qPredio.usoSuelo.id.eq(det.getUsoSuelo().getId()));
        }
        if (!isEmpty(det.getTipoInmueble())) {
            where.and(qPredio.tipoInmueble.id.eq(det.getTipoInmueble().getId()));
        }
        if (!isEmpty(det.getFraccion())) {
            where.and(qPredio.fraccion.eq(det.getFraccion()));
        }
        if (!isEmpty(det.getMacroManzana())) {
            where.and(qPredio.macroManzana.eq(det.getMacroManzana()));
        }
        if (!isEmpty(det.getTipoVialidad2())) {
            where.and(qPredio.tipoVialidad2.id.eq(det.getTipoVialidad2().getId()));
        }
        if (!isEmpty(det.getVialidad2())) {
            where.and(qPredio.vialidad2.eq(det.getVialidad2()));
        }
        if (!isEmpty(det.getNumExt2())) {
            where.and(qPredio.numExt2.eq(det.getNumExt2()));
        }
        if (!isEmpty(det.getEnlaceVialidad())) {
            where.and(qPredio.enlaceVialidad.id.eq(det.getEnlaceVialidad().getId()));
        }
        if (!isEmpty(det.getFracOCondo())) {
            where.and(qPredio.fracOCondo.id.eq(det.getFracOCondo().getId()));
        }
        if (!isEmpty(det.getNombreFracOCondo())) {
            where.and(qPredio.nombreFracOCondo.equalsIgnoreCase("%" +det.getNombreFracOCondo()+"%" ));
            where.or(qPredio.nombreFracOCondo.like("%" +det.getNombreFracOCondo()+"%" ));
        }
        if (!isEmpty(det.getEtapaOSeccion())) {
            where.and(qPredio.etapaOSeccion.id.eq(det.getEtapaOSeccion().getId()));
        }
        
        if (!isEmpty(oficinaId)) {
            where.and(qPredio.oficina.id.eq(oficinaId));
        }
        
        
      }

        if(idsPrediosTitulares!=null  && idsPrediosTitulares.size()>0) {
        	if (!isEmpty(oficinaId)) {
	        	//long oficinaId = Long.parseLong(det.getOficina());
	            where.and(qPredio.oficina.id.eq(oficinaId));
	        }
        }
        
        queryMunYVariantes(det, qPredio, where);

        queryAsentamientoYVariante(det, qPredio, where);


        if (!isEmpty(idsPrediosTitulares)) {
            log.debug(" === Agregando restricci'on de titulares = {} personas " , idsPrediosTitulares.size());
            where.and(qPredio.id.in(idsPrediosTitulares));
        }

        return where;
    }

    private void queryMunYVariantes(BusquedaPredioVO det, QPredio qPredio, BooleanBuilder where) {
        if (!isEmpty(det.getMunicipio()) && !isEmpty(det.getVarianteMunicipio())) {
            where.and((qPredio.municipio.id.eq(det.getMunicipio().getId())).or(qPredio.municipio.id.eq(det.getVarianteMunicipio().getId())));
        } else {
            if (!isEmpty(det.getMunicipio())) {
                where.and(qPredio.municipio.id.eq(det.getMunicipio().getId()));
            }
            if (!isEmpty(det.getVarianteMunicipio())) {
                where.and(qPredio.municipio.id.eq(det.getVarianteMunicipio().getId()));
            }
        }
    }

    private void queryAsentamientoYVariante(BusquedaPredioVO det, QPredio qPredio, BooleanBuilder where) {

        if (!isEmpty(det.getTipoAsent()) && !isEmpty(det.getVarianteTipoAsent())) {
            where.and((qPredio.tipoAsent.id.eq(det.getTipoAsent().getId())).or(qPredio.tipoAsent.id.eq(det.getVarianteTipoAsent().getId())));
        } else {
            if (!isEmpty(det.getTipoAsent())) {
                where.and(qPredio.tipoAsent.id.eq(det.getTipoAsent().getId()));
            }
            if (!isEmpty(det.getVarianteTipoAsent())) {
                where.and(qPredio.tipoAsent.id.eq(det.getVarianteTipoAsent().getId()));
            }
        }
    }


    private List<PredioPersona> buscarTitulares(Set<BusquedaPersonaVO> busquedaPersonaVO) {

        List<PredioPersona> pp = null;
        List<PredioPersona>  ppOk = new ArrayList<>();
        if (!isEmpty(busquedaPersonaVO) && busquedaPersonaVO.size() > 0) {
            QPredioPersona qPredioPersona = QPredioPersona.predioPersona;
            QActoPredio    qActoPredio    = QActoPredio.actoPredio;

            
            JPQLQuery<PredioPersona> subQuery = from(qPredioPersona);

            for (BusquedaPersonaVO personaVO : busquedaPersonaVO) {
                BooleanBuilder wherePF = new BooleanBuilder();

                if (!isEmpty(personaVO.getNombre()))// LIKE
                    wherePF.and(qPredioPersona.persona.nombre.trim().containsIgnoreCase(personaVO.getNombre().trim().toLowerCase()));
                                                      
                if (!isEmpty(personaVO.getPaterno()))
                    wherePF.and(qPredioPersona.persona.paterno.trim().containsIgnoreCase(personaVO.getPaterno().trim().toLowerCase()));

                if (!isEmpty(personaVO.getMaterno()))
                    wherePF.and(qPredioPersona.persona.materno.trim().containsIgnoreCase(personaVO.getMaterno().trim().toLowerCase()));

                if (!isEmpty(personaVO.getRfc()))
                    wherePF.and(qPredioPersona.persona.rfc.equalsIgnoreCase(personaVO.getRfc().trim().toLowerCase()));

                if (!isEmpty(personaVO.getCurp()))
                    wherePF.and(qPredioPersona.persona.curp.equalsIgnoreCase(personaVO.getCurp().trim().toLowerCase()));

                if (!isEmpty(personaVO.getFechaNac())) {
                    DateTime dtIni= new DateTime(personaVO.getFechaNac());
                    DateTime dtFin = dtIni.plusDays(1);
                    wherePF.and(  qPredioPersona.persona.fechaNac.between(dtIni.toDate(), dtFin.toDate()) );
                }


                if (!isEmpty(personaVO.getNacionalidad()))
                    wherePF.and(qPredioPersona.nacionalidad.id.eq(personaVO.getNacionalidad().getId()));

                if (!isEmpty(personaVO.getEstadoCivil()))
                    wherePF.and(qPredioPersona.estadoCivil.id.eq(personaVO.getEstadoCivil().getId()));

                if (!isEmpty(personaVO.getRegimen()))
                    wherePF.and(qPredioPersona.regimen.id.eq(personaVO.getRegimen().getId()));

                //query.innerJoin(qPredio.actoPrediosParaPredios, qActoPredio);
                subQuery.innerJoin(qPredioPersona.actoPredio, qActoPredio);
                subQuery.where(wherePF);

            }

            pp = subQuery.fetch();
            
            System.out.println("SIZE "+pp.size());

            if(pp!=null &&pp.size()>0){                            
                for(PredioPersona p:pp){
                        if(p.getActoPredio().getActo().getStatusActo().getId()==1L){      
                        ppOk.add(p);
                        } 
                    };
            }
        }
        return ppOk;
    }
}