package com.kakao.problem.distribution.application.response;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class DistributionAcquireResponse implements Serializable {

	private final Long amount;

	public DistributionAcquireResponse(Long amount) {
		this.amount = amount;
	}
}
