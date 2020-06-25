package com.example.problem.configuration.resolver;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.problem.assets.http.RequestHeader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HeaderArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
      return parameter.getParameterType() == RequestHeader.class;
  }

	@Nonnull
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
    return RequestHeader.builder().request(request).build();
	}
}
