package com.kakao.problem.distribution.application.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DistributionCreateRequest implements Serializable {

	private static final long serialVersionUID = -7074035301961636962L;

	@Min(1)
	@NotNull
	private Long amount;

	@Min(1)
	@NotNull
	private Long people;

}
