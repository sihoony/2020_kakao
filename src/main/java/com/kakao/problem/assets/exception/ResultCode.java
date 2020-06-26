package com.kakao.problem.assets.exception;

public enum ResultCode {

  OK(200),

  BAD_REQUEST(400),

  RUNTIME_EXCEPTION(500),

  INVALID_PARAMETERS(502),

  ACQUIRE_DENIED(10001),

  DISTRIBUTION_COMPLETE(10002),

  DUPLICATION_ACQUIRE(10003),

  EXPIRED_REQUEST(10004),

  NOT_FOUND_DISTRIBUTION(10005),

  READ_RESTRICTION(10006);

  private final int code;

  ResultCode(int code) {
    this.code = code;
  }

  public int code() {
    return code;
  }

  public String type(){
    return this.name();
  }
}
