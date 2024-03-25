package com.gisnet.erpp.repository.impl;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.QStatus;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.repository.StatusRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;

@Repository
@Transactional(readOnly = true)
public class StatusRepositoryImpl extends QueryDslRepositorySupport implements StatusRepositoryCustom {


	StatusRepositoryImpl() {
		super(Status.class);
	}

	private JPQLQuery<Status> getQueryFrom(QStatus qEntity) {
		return from(qEntity);
	}

	public List<Status> getAllUsable() {
		QStatus status = QStatus.status;

		Integer[] idUsables = new Integer[] { 1, 2, 3, 4, 5, 6 };

		// OrderSpecifier<Long> orden = new OrderSpecifier<Long>(Order.ASC, status.id);
		// query.orderBy(orden);

		JPQLQuery<Status> query = getQueryFrom(status);
		BooleanBuilder where = new BooleanBuilder();
		where.and(status.id.in(idUsables));

		query.where(where);
		query.orderBy(status.id.asc());

		List<Status> results = query.fetch();

		return results;
	}
}
