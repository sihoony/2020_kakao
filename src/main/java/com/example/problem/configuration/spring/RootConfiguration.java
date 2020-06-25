package com.example.problem.configuration.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.problem.assets.token.TokenGenerator;

@Configuration
public class RootConfiguration {

	@Bean
	public TokenGenerator tokenGenerator(){
		return new TokenGenerator();
	}

}
