package com.example.problem.assets.token;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TokenGenerator {

	private static final int START_POSITION = 65;
	private static final int END_POSITION = 122;

	private static final int TARGET_STRING_LENGTH = 3;

	private static final Random RANDOM = new Random();

	public String randomToken(){

		return RANDOM.ints(START_POSITION, END_POSITION + 1)
			.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
			.limit(TARGET_STRING_LENGTH)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
	}
}
