package com.example.problem.distribution.application.request;

import java.io.Serializable;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class DistributionCreateRequest implements Serializable {

	private static final long serialVersionUID = -7074035301961636962L;

	@Min(1)
	private Long amount;

	@Min(1)
	private Long people;

}
