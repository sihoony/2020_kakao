package com.example.problem.distribution.exptions;

public class DistributionCompleteException extends RuntimeException {

	public DistributionCompleteException() {
	}

	public DistributionCompleteException(String message) {
		super(message);
	}
}
