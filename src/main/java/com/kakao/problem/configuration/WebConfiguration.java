package com.kakao.problem.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kakao.problem.configuration.resolver.HeaderArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

	@Override
	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new HeaderArgumentResolver());
	}

	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		ObjectMapper objectMapper = new ObjectMapper()
						.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS,
										DeserializationFeature.USE_LONG_FOR_INTS,
										DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
										DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
										DeserializationFeature.READ_ENUMS_USING_TO_STRING,
										DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
						.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
						.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
						.enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
						.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
						.setSerializationInclusion(JsonInclude.Include.NON_NULL)
						.registerModule(new JavaTimeModule());

		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);

		converters.add(mappingJackson2HttpMessageConverter);

		super.configureMessageConverters(converters);
	}
}
