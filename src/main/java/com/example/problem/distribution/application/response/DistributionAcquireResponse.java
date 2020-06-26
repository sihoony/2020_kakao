package com.example.problem.distribution.application.response;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class DistributionAcquireResponse implements Serializable {

	private final Long amount;

	public DistributionAcquireResponse(Long amount) {
		this.amount = amount;
	}
}
