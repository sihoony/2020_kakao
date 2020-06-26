package com.kakao.problem.configuration.advisor;

import com.kakao.problem.assets.entrypoints.RestApiResponse;
import com.kakao.problem.assets.exception.BaseException;
import com.kakao.problem.assets.exception.ResultCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdvisor {


  @Order(10)
  @ExceptionHandler(value = {
          MethodArgumentNotValidException.class
  })
  @ResponseStatus(code = HttpStatus.OK)
  public RestApiResponse<String> methodArgumentNotValidException(MethodArgumentNotValidException exception){
    BindingResult bindingResult = exception.getBindingResult();

    StringBuilder builder = new StringBuilder();
    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      builder.append("['");
      builder.append(fieldError.getField());
      builder.append("' must ");
      builder.append(fieldError.getDefaultMessage());
      builder.append(" input value : '");
      builder.append(fieldError.getRejectedValue());
      builder.append("'] ");
    }

    return RestApiResponse.error(builder.toString(), ResultCode.BAD_REQUEST);
  }

  @Order(100)
  @ExceptionHandler(BindException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public RestApiResponse<String> bindException(BindException exception) {

    String result = exception.getFieldErrors()
            .stream()
            .map(fieldError -> String.join(" : ", fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.joining(", "));

    return  RestApiResponse.error(result, ResultCode.INVALID_PARAMETERS);
  }

  @Order(200)
  @ExceptionHandler(value = {
          BaseException.class
  })
  @ResponseStatus(code = HttpStatus.OK)
  public RestApiResponse<String> businessException(BaseException baseException){

    return RestApiResponse.error(baseException.getResultCode());
  }

  @Order
  @ExceptionHandler(value = {
          Exception.class
  })
  @ResponseStatus(code = HttpStatus.OK)
  public RestApiResponse<String> runtimeException(){

    return RestApiResponse.error(ResultCode.RUNTIME_EXCEPTION);
  }

}
