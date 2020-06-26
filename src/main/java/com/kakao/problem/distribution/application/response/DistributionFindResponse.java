package com.kakao.problem.distribution.application.response;

import com.kakao.problem.distribution.domain.DistributionReceiver;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DistributionFindResponse implements Serializable {

	private static final long serialVersionUID = 8915944669674189579L;

	private final Long totalAmount;

	private final Long completionAmount;

	private final LocalDateTime createdDate;

	private final List<Reciver> recivers;

	public DistributionFindResponse(final List<DistributionReceiver> distributionReceivers, final Long totalAmount, final LocalDateTime createdDate) {

		this.totalAmount = totalAmount;
		this.createdDate = createdDate;
		this.completionAmount = distributionReceivers.stream()
						.mapToLong(DistributionReceiver::getAmount)
						.sum();

		this.recivers = distributionReceivers
						.stream()
						.map(distributionReceiver -> new Reciver(distributionReceiver.getUserId(), distributionReceiver.getAmount()))
						.collect(Collectors.toList());
	}

	@Getter
	public static class Reciver implements Serializable{

		private static final long serialVersionUID = -4068947638169172827L;

		private final Long userId;

		private final Long amount;

		public Reciver(Long userId, Long amount) {
			this.userId = userId;
			this.amount = amount;
		}
	}
}
