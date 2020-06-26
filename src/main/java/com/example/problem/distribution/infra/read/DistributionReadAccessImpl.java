package com.example.problem.distribution.infra.read;


import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.example.problem.distribution.domain.*;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectFrom;

@Repository
public class DistributionReadAccessImpl extends QuerydslRepositorySupport implements DistributionReadAccess{

	public DistributionReadAccessImpl() {
		super(Distribution.class);
	}

	@Override
	public Distribution findByTokenAndRoomId(String token, String roomId) {

		QDistribution distribution = QDistribution.distribution;
		QDistributionReceiver distributionReceiver = QDistributionReceiver.distributionReceiver;

		return
			select(distribution)
			.from(distribution)
			.where(
				distribution.token.eq(token),
				distribution.roomId.eq(roomId))
			.fetchOne();
	}
}
