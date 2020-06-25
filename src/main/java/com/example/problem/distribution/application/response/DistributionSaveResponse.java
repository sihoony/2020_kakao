package com.example.problem.distribution.application.response;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class DistributionSaveResponse implements Serializable {

	private static final long serialVersionUID = 5386536939642542203L;

	private final String token;

	public DistributionSaveResponse(String token) {
		this.token = token;
	}

}
