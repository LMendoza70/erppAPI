package com.gisnet.erpp.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.AuxiliarPersona;
import com.gisnet.erpp.domain.MueblePersona;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.QActo;
import com.gisnet.erpp.domain.QActoPredio;
import com.gisnet.erpp.domain.QFolioSeccionAuxiliar;
import com.gisnet.erpp.domain.QMueble;
import com.gisnet.erpp.domain.QPersona;
import com.gisnet.erpp.domain.QPersonaJuridica;
import com.gisnet.erpp.domain.QPredio;
import com.gisnet.erpp.domain.QPrelacion;
import com.gisnet.erpp.domain.QTipoActo;
import com.gisnet.erpp.domain.QTipoActoTipoActo;
import com.gisnet.erpp.repository.ActoPredioRepositoryCustom;
import com.gisnet.erpp.repository.FolioSeccionAuxiliarRepository;
import com.gisnet.erpp.repository.PersonaAuxiliarRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.PersonaRepositoryCustom;
import com.gisnet.erpp.repository.MuebleRepository;
import com.gisnet.erpp.repository.PersonaJuridicaRepository;
import com.gisnet.erpp.repository.FolioSeccionAuxiliarRepository;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.Constantes.ETipoFolio;
import com.gisnet.erpp.util.Constantes.TipoEntrada;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class ActoPredioRepositoryImpl extends QueryDslRepositorySupport implements ActoPredioRepositoryCustom {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	ActoPredioRepositoryImpl() {
		super(ActoPredio.class);
	}
	
	private JPQLQuery<ActoPredio> getQueryFrom(QActoPredio qEntity) {
		return from(qEntity);
	}

	@Autowired
	private PredioRepository predioRepository;

	@Autowired
	PersonaJuridicaRepository personaJuridicaRepository;

	@Autowired
	FolioSeccionAuxiliarRepository folioSeccionAuxliarRepository;

	@Autowired
	MuebleRepository muebleRepository;

	private Set<ActoPredio> getActosPrediosByPredioId(Long id) {
		return predioRepository.findOne(id).getActoPrediosParaPredios();
	}

	private Set<ActoPredio> getActosPrediosByPersonaJuridicaId(Long id) {
		return personaJuridicaRepository.findOne(id).getActoPrediosParaPersonasJuridicas();
	}

	private Set<ActoPredio> getActosPrediosByFolioSeccionAuxiliarId(Long id) {
		return folioSeccionAuxliarRepository.findOne(id).getActoPrediosParaFolioSeccionAuxiliares();
	}

	private Set<ActoPredio> getActosPrediosByMuebleId(Long id) {
		return muebleRepository.findOne(id).getActoPrediosParaMuebles();
	}

	public Set<PredioPersona> findPersonasbyPredioId(Long id) {

		return getActosPrediosByPredioId(id).stream().map(ActoPredio::getPredioPersonasParaActoPredios).flatMap(Collection::stream).collect(Collectors.toSet());
	}

	public Set<PjPersona> findPersonasbyPersonaJuridicaId(Long id) {

		return getActosPrediosByPersonaJuridicaId(id).stream().map(ActoPredio::getPersonaJuridicaPersonasParaActoPredios).flatMap(Collection::stream).collect(Collectors.toSet());
	}

	public Set<AuxiliarPersona> findPersonasbyFolioSeccionAuxiliarId(Long id) {

		return getActosPrediosByFolioSeccionAuxiliarId(id).stream().map(ActoPredio::getFolioSeccionAuxiliarPersonasParaActoPredios).flatMap(Collection::stream).collect(Collectors.toSet());
	}

	public Set<MueblePersona> findPersonasbyMuebleId(Long id) {

		return getActosPrediosByMuebleId(id).stream().map(ActoPredio::getMueblePersonasParaActoPredios).flatMap(Collection::stream).collect(Collectors.toSet());
	}
	
	@Transactional(readOnly = true)
	public List<ActoPredio> findByFolioIdAndTipoFolioAndPrelacionIdAndTipoEntradaId(Long folioId, Constantes.ETipoFolio tipoFolio, Long prelacionId, Constantes.TipoEntrada tipoEntrada) {
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");
		QPrelacion prelacion = new QPrelacion("prelacion");

		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");

		JPQLQuery<ActoPredio> query = getQueryFrom(ap);

		query.join(ap.acto, a);
		query.join(a.prelacion, prelacion);

		query.leftJoin(ap.predio);
		query.leftJoin(ap.personaJuridica);
		query.leftJoin(ap.folioSeccionAuxiliar);
		query.leftJoin(ap.mueble);

		BooleanBuilder where = new BooleanBuilder();
		//Maxima version para el tipo de entrada
		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2).where(a2.id.eq(a.id).and(ap2.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada())).and(a2.statusActo.nombre.notEqualsIgnoreCase("ELIMINADO")))));
		where.and(prelacion.id.eq(prelacionId));
		where.and(ap.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada()));
		
		switch (tipoFolio) {
		case PERSONAS_JURIDICAS:
			where.and(ap.personaJuridica.id.eq(folioId));
			break;
		case PERSONAS_AUXILIARES:
			where.and(ap.folioSeccionAuxiliar.id.eq(folioId));
			break;
		case MUEBLE:
			where.and(ap.mueble.id.eq(folioId));
			break;
		case PREDIO:
			where.and(ap.predio.id.eq(folioId));
			break;
		default:
			break;
		}

		query.where(where);

		return query.fetch();
	}
	
	@Transactional(readOnly = true)
	public List<ActoPredio> findByFolioIdAndPrelacionIdAndTipoEntradaId(Long folioId, Long prelacionId, Constantes.TipoEntrada tipoEntrada) {
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");
		QPrelacion prelacion = new QPrelacion("prelacion");

		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");

		JPQLQuery<ActoPredio> query = getQueryFrom(ap);

		query.join(ap.acto, a);
		query.join(a.prelacion, prelacion);


		BooleanBuilder where = new BooleanBuilder();
		//Maxima version para el tipo de entrada
		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2).where(a2.id.eq(a.id).and(ap2.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada())).and(a2.statusActo.nombre.notEqualsIgnoreCase("ELIMINADO")))));
		where.and(prelacion.id.eq(prelacionId));
		where.and(ap.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada()));
		where.and(ap.personaJuridica.id.eq(folioId).or(ap.folioSeccionAuxiliar.id.eq(folioId)).or(ap.mueble.id.eq(folioId).or(ap.predio.id.eq(folioId))));
		
		query.where(where);

		return query.fetch();
	}
	
	@Transactional(readOnly = true)
	public List<ActoPredio> findBydPrelacionIdAndTipoEntradaIdPredioIsNull(Long prelacionId, Constantes.TipoEntrada tipoEntrada) {
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");
		QPrelacion prelacion = new QPrelacion("prelacion");

		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");

		JPQLQuery<ActoPredio> query = getQueryFrom(ap);

		query.join(ap.acto, a);
		query.join(a.prelacion, prelacion);

		BooleanBuilder where = new BooleanBuilder();
		//Maxima version para el tipo de entrada
		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2).where(a2.id.eq(a.id).and(ap2.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada())).and(a2.statusActo.nombre.notEqualsIgnoreCase("ELIMINADO")))));
		where.and(prelacion.id.eq(prelacionId));
		where.and(ap.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada()));
		where.and(ap.predio.isNull());
		
		query.where(where);

		return query.fetch();
	}

	@Transactional(readOnly = true)
	public List<ActoPredio> findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaId(Long folioId, Constantes.ETipoFolio tipoFolio, Constantes.StatusActo statusActo, Constantes.TipoEntrada tipoEntrada ) {
		return this.findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaIdAndPrimerRegistro(folioId, tipoFolio, statusActo, tipoEntrada, null);
	}
	
	@Transactional(readOnly = true)
	public List<ActoPredio> findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaIdAndPrimerRegistro(Long folioId, Constantes.ETipoFolio tipoFolio, Constantes.StatusActo statusActo, Constantes.TipoEntrada tipoEntrada, Boolean primerRegistro) {
		System.out.println("predio Id "+folioId);
		System.out.println("status A"+statusActo);
		System.out.println("tipoEntrada "+tipoEntrada);
		System.out.println("primerRegistro "+primerRegistro);
		
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");
		QPrelacion prelacion = new QPrelacion("prelacion");

		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");

		JPQLQuery<ActoPredio> query = getQueryFrom(ap);

		query.join(ap.acto, a);
		query.leftJoin(a.prelacion, prelacion);

		query.leftJoin(ap.predio);
		query.leftJoin(ap.personaJuridica);
		query.leftJoin(ap.folioSeccionAuxiliar);
		query.leftJoin(ap.mueble);

		BooleanBuilder where = new BooleanBuilder();
		//Maxima version para el tipo de entrada
		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2).where(a2.id.eq(a.id).and(ap2.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada())))));
		where.and(a.statusActo.id.eq(statusActo.getIdStatusActo()));
		where.and(ap.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada()));
		
/* 	  	if (primerRegistro !=null && primerRegistro) {
				where.and(a.primerRegistro.eq(true));
			}   
		 */
		switch (tipoFolio) {
		case PERSONAS_JURIDICAS:
			where.and(ap.personaJuridica.id.eq(folioId));
			break;
		case PERSONAS_AUXILIARES:
			where.and(ap.folioSeccionAuxiliar.id.eq(folioId));
			break;
		case MUEBLE:
			where.and(ap.mueble.id.eq(folioId));
			break;
		case PREDIO:
			where.and(ap.predio.id.eq(folioId));
			break;
		default:
			break;
		}

		query.where(where);
	
		return query.fetch();
	}

	
	@Transactional(readOnly = true)
	public List<ActoPredio> findAllByPrelacionAndVf(Long prelacionId, boolean validacionFolios,Constantes.TipoEntrada tipoEntrada) {
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");
		QPrelacion prelacion = new QPrelacion("prelacion");

		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");

		JPQLQuery<ActoPredio> query = getQueryFrom(ap);

		query.join(ap.acto, a);
		query.join(a.prelacion, prelacion);
		
		BooleanBuilder where = new BooleanBuilder();
		//Maxima version para el tipo de entrada
		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2).where(a2.id.eq(a.id).and(ap2.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada())).and(a2.statusActo.nombre.notEqualsIgnoreCase("ELIMINADO")))));
		where.and(prelacion.id.eq(prelacionId));
		where.and(ap.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada()));
		where.and(a.statusActo.nombre.notEqualsIgnoreCase("ELIMINADO"));
		if (validacionFolios) {
			where.and(a.vf.eq(true));
		} else {
			where.and(a.vf.isNull().or(a.vf.eq(false)));
		}
		query.where(where);
		System.out.println(query);
		return query.fetch();
	}
	
	@Transactional(readOnly = true)
	public ActoPredio findAllByActoAndVf(Long actoId, boolean validacionFolios,
			Constantes.TipoEntrada tipoEntrada) {
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");
		QPrelacion prelacion = new QPrelacion("prelacion");

		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");

		JPQLQuery<ActoPredio> query = getQueryFrom(ap);

		query.join(ap.acto, a);
		query.join(a.prelacion, prelacion);

		BooleanBuilder where = new BooleanBuilder();
		// Maxima version para el tipo de entrada
		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2)
				.where(a2.id.eq(a.id).and(ap2.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada())))));

		//where.and(prelacion.id.eq(prelacionId));
		where.and(a.id.eq(actoId));
		where.and(ap.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada()));
		if (validacionFolios) {
			where.and(a.vf.eq(true));
		} else {
			where.and(a.vf.isNull().or(a.vf.eq(false)));
		}
		query.where(where);
		List<ActoPredio> result  = query.fetch();
		return result!=null && result.size()>0 ?  result.get(0) : null;
	}


	@Transactional(readOnly = true)
	public List<ActoPredio> findAllByActoIdAndTipoEntrada(Long actoId,Constantes.TipoEntrada tipoEntrada) {
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");
		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");

		JPQLQuery<ActoPredio> query = getQueryFrom(ap);

		query.join(ap.acto, a);		
		BooleanBuilder where = new BooleanBuilder();
		//Maxima version para el tipo de entrada
		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2).where(a2.id.eq(a.id).and(ap2.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada())).and(a2.statusActo.nombre.notEqualsIgnoreCase("ELIMINADO")))));
		where.and(a.id.eq(actoId));
		where.and(ap.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada()));		
		query.where(where);

		return query.fetch();
	}
	
	@Override
	public List<ActoPredio> findByFolioIdAndTipoFolioAndTipoEntradaIdAndPrimerRegistro(Long folioId,
			ETipoFolio tipoFolio, TipoEntrada tipoEntrada, Boolean primerRegistro) {
		
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");
		QPrelacion prelacion = new QPrelacion("prelacion");

		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");

		JPQLQuery<ActoPredio> query = getQueryFrom(ap);

		query.join(ap.acto, a);
		query.leftJoin(a.prelacion, prelacion);

		query.leftJoin(ap.predio);
		query.leftJoin(ap.personaJuridica);
		query.leftJoin(ap.folioSeccionAuxiliar);
		query.leftJoin(ap.mueble);

		BooleanBuilder where = new BooleanBuilder();
		//Maxima version para el tipo de entrada
		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2).where(a2.id.eq(a.id).and(ap2.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada())))));
		
		where.and(ap.tipoEntrada.id.eq(tipoEntrada.getIdTipoEntrada()));
		
		if (primerRegistro !=null && primerRegistro) {
			where.and(a.primerRegistro.eq(true));
		}
		
		switch (tipoFolio) {
		case PERSONAS_JURIDICAS:
			where.and(ap.personaJuridica.id.eq(folioId));
			break;
		case PERSONAS_AUXILIARES:
			where.and(ap.folioSeccionAuxiliar.id.eq(folioId));
			break;
		case MUEBLE:
			where.and(ap.mueble.id.eq(folioId));
			break;
		case PREDIO:
			where.and(ap.predio.id.eq(folioId));
			break;
		default:
			break;
		}

		query.where(where);

		return query.fetch();
		
		
		
		
		
	}
	
	@Transactional(readOnly = true)
	public List<ActoPredio> findAllByPrelacion(Long prelacionId) {
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");
		QPrelacion prelacion = new QPrelacion("prelacion");

		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");

		JPQLQuery<ActoPredio> query = getQueryFrom(ap);

		query.join(ap.acto, a);
		query.join(a.prelacion, prelacion);

		BooleanBuilder where = new BooleanBuilder();
		// Maxima version para el tipo de entrada
		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2)
				.where(a2.id.eq(a.id))));
		where.and(prelacion.id.eq(prelacionId));
		where.and(a.vf.isNull().or(a.vf.eq(false)));
		query.where(where);

		return query.fetch();
	}



}
