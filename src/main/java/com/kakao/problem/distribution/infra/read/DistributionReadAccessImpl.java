package com.kakao.problem.distribution.infra.read;


import com.kakao.problem.distribution.domain.Distribution;
import com.kakao.problem.distribution.domain.QDistribution;
import com.kakao.problem.distribution.domain.QDistributionReceiver;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.querydsl.jpa.JPAExpressions.select;

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
