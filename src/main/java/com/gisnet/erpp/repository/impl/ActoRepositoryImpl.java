package com.gisnet.erpp.repository.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.QActo;
import com.gisnet.erpp.domain.QActoPredio;
import com.gisnet.erpp.domain.QFolioSeccionAuxiliar;
import com.gisnet.erpp.domain.QMueble;
import com.gisnet.erpp.domain.QPersonaJuridica;
import com.gisnet.erpp.domain.QPredio;
import com.gisnet.erpp.domain.QTipoActo;
import com.gisnet.erpp.domain.QTipoActoTipoActo;
import com.gisnet.erpp.repository.ActoRepositoryCustom;
import com.gisnet.erpp.util.Constantes;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

@Transactional(readOnly = true)
public class ActoRepositoryImpl extends QueryDslRepositorySupport implements ActoRepositoryCustom {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	ActoRepositoryImpl() {
		super(Acto.class);
	}

	private JPQLQuery<Acto> getQueryFrom(QActo qEntity) {
		return from(qEntity);
	}

	@Transactional(readOnly = true)
	public List<Acto> findActosByTipoActoPadreEnTipoActoTipoActo(Long tipoActoId, Long folioId, Constantes.ETipoFolio tipoFolio, Constantes.StatusActo statusActo) {
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");

		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");
		QTipoActo ta = new QTipoActo("ta");
		QPredio p = new QPredio("p");
		QMueble m = new QMueble("m");
		QFolioSeccionAuxiliar fsa = new QFolioSeccionAuxiliar("fsa");
		QPersonaJuridica pj = new QPersonaJuridica("pj");

		QTipoActoTipoActo tata = new QTipoActoTipoActo("tata");

		JPQLQuery<Acto> query = getQueryFrom(a);

		query.join(a.actoPrediosParaActos, ap);
		query.join(a.tipoActo, ta);
		query.join(ta.tipoActoTipoActoParaTipoActoPadre, tata);
		query.leftJoin(ap.predio, p);
		query.leftJoin(ap.personaJuridica);
		query.leftJoin(ap.folioSeccionAuxiliar);
		query.leftJoin(ap.mueble);

		BooleanBuilder where = new BooleanBuilder();

		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2).where(a2.id.eq(a.id))));
		where.and(a.statusActo.id.eq(statusActo.getIdStatusActo()));

		switch (tipoFolio) {
		case PREDIO:
			where.and(p.id.eq(folioId));
			break;
		case MUEBLE:
			where.and(m.id.eq(folioId));
		case PERSONAS_AUXILIARES:
			where.and(fsa.id.eq(folioId));
		case PERSONAS_JURIDICAS:
			where.and(pj.id.eq(folioId));
		default:
			break;
		}

		where.and(tata.tipoActo.id.eq(tipoActoId));

		query.where(where);

		return query.fetch();
	}
	
	public Page<Acto> findAllByPrelacionIdAndVfFalse(Long prelacionId,Pageable pageable){
		QActo a = new QActo("a");
		JPQLQuery<Acto> query = getQueryFrom(a);
		BooleanBuilder where = new BooleanBuilder();
		
		where.andAnyOf(a.vf.isNull()).or(a.vf.eq(false));
		where.andAnyOf((a.clon.isNull()).or(a.clon.eq(false)));
		where.and(a.prelacion.id.eq(prelacionId));
		
		query.where(where);
		query.offset(pageable.getOffset()).limit(pageable.getPageSize());
		long totalFound = query.fetchCount();
		List<Acto> result = query.fetch();  
		return  new PageImpl<Acto>(result,pageable,totalFound);
	}
	
	
	@Transactional(readOnly = true)
	public List<Acto> findActos(Long[] folioId, Constantes.ETipoFolio tipoFolio, Constantes.StatusActo statusActo, boolean isReplicaPorFusion) {
		QActoPredio ap = new QActoPredio("ap1");
		QActo a = new QActo("a1");

 		QActoPredio ap2 = new QActoPredio("ap2");
		QActo a2 = new QActo("a2");
		QTipoActo ta = new QTipoActo("ta");

 		JPQLQuery<Acto> query = getQueryFrom(a);

 		query.join(a.actoPrediosParaActos, ap);
		query.join(a.tipoActo, ta);

 		BooleanBuilder where = new BooleanBuilder();

 		where.and(ap.version.eq(JPAExpressions.select(ap2.version.max()).from(ap2).join(ap2.acto, a2).where(a2.id.eq(a.id))));
		where.and(a.statusActo.id.eq(statusActo.getIdStatusActo()));

 		switch (tipoFolio) {
		case PREDIO:
			where.and(ap.predio.id.in(folioId));
			break;
		case MUEBLE:
			where.and(ap.mueble.id.in(folioId));
			break;
		case PERSONAS_AUXILIARES:
			where.and(ap.folioSeccionAuxiliar.id.in(folioId));
			break;
		case PERSONAS_JURIDICAS:
			where.and(ap.personaJuridica.id.in(folioId));
			break;
		default:
			break;
		}

 		if (isReplicaPorFusion) {
			where.and(ta.replicaPorFusion.isTrue());
		}

 		query.where(where);

 		return query.fetch();
	}

}
