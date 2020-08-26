package com.kakao.problem.distribution.infra.read;


import com.kakao.problem.distribution.domain.Distribution;
import com.kakao.problem.distribution.domain.QDistribution;
import com.kakao.problem.distribution.domain.QDistributionReceiver;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class DistributionReadAccessImpl extends QuerydslRepositorySupport implements DistributionReadAccess{

	public JPAQueryFactory jpaQueryFactory;

	public DistributionReadAccessImpl() {
		super(Distribution.class);
	}

	@Autowired
	public void setJpaQueryFactory(JPAQueryFactory jpaQueryFactory) {
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public Distribution findByTokenAndRoomId(String token, String roomId) {

		QDistribution distribution = QDistribution.distribution;

		return jpaQueryFactory.selectFrom(distribution)
						.where(
							distribution.token.eq(token),
							distribution.roomId.eq(roomId))
						.fetchFirst();
	}
}
