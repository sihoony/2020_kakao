package com.kakao.problem.distribution.application.response;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class DistributionCreateResponse implements Serializable {

	private static final long serialVersionUID = 5386536939642542203L;

	private final String token;

	public DistributionCreateResponse(String token) {
		this.token = token;
	}

}
