package com.kakao.problem.assets.token;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.BDDAssertions.then;

class TokenGeneratorTest {

	private static final int TARGET_STRING_LENGTH = 3;

	private final TokenGenerator tokenGenerator = new TokenGenerator();

	@RepeatedTest(3)
	@DisplayName("token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다. - 생성 반복 테스트")
	void generator(){

		//given
		//when
		final String token = tokenGenerator.randomToken();

		then(token).isNotNull();
		then(token).isNotBlank();
		then(token.length()).isEqualTo(TARGET_STRING_LENGTH);
	}

}