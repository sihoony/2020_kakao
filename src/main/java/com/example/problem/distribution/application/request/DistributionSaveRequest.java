package com.example.problem.distribution.application.request;

import java.io.Serializable;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class DistributionSaveRequest implements Serializable {

	@Min(1)
	private Long money;

	@Min(1)
	private Long people;

}
