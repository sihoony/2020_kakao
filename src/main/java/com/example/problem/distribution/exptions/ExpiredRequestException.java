package com.example.problem.distribution.exptions;

public class ExpiredRequestException extends RuntimeException {

	public ExpiredRequestException() {
	}

	public ExpiredRequestException(String message) {
		super(message);
	}
}
