package com.kakao.problem.assets.entrypoints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakao.problem.assets.exception.ResultCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import java.io.Serializable;

@Getter
@NoArgsConstructor
public class RestApiResponse<T> implements Serializable {

  private static final long serialVersionUID = -5533098721608417474L;

  private int result;
  private String message;
  private T data;

  @JsonIgnore
  public static <T> RestApiResponse<T> ok(@Nonnull T data){
    RestApiResponse res = new RestApiResponse();
    res.data = data;
    res.message = ResultCode.OK.type();
    res.result = ResultCode.OK.code();
    return res;
  }

  @JsonIgnore
  public static <T> RestApiResponse<T> error(@Nonnull ResultCode resultCode){
    RestApiResponse res = new RestApiResponse();
    res.message = resultCode.type();
    res.result = resultCode.code();
    return res;
  }

  @JsonIgnore
  public static <T> RestApiResponse<T> error(@Nonnull T data, @Nonnull ResultCode resultCode){
    RestApiResponse res = new RestApiResponse();
    res.data = data;
    res.message = resultCode.type();
    res.result = resultCode.code();
    return res;
  }

}
