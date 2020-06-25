package com.example.problem.distribution.application.response;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class DistributionSaveResponse implements Serializable {

	private final String token;

	public DistributionSaveResponse(String token) {
		this.token = token;
	}

}
