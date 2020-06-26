package com.kakao.problem.assets.exception;

public class BaseException extends RuntimeException{

  private static final long serialVersionUID = -1766742917456023735L;

  private final ResultCode resultCode;

  public BaseException(ResultCode resultCode) {
    this.resultCode = resultCode;
  }

  public ResultCode getResultCode() {
    return resultCode;
  }
}
