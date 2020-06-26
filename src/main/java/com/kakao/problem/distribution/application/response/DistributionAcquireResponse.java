package com.kakao.problem.distribution.application.response;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class DistributionAcquireResponse implements Serializable {

	private static final long serialVersionUID = -2640755653211360381L;

	private final Long amount;

	public DistributionAcquireResponse(Long amount) {
		this.amount = amount;
	}
}
