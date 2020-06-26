package com.kakao.problem.distribution.exptions;

import com.kakao.problem.assets.exception.BaseException;
import com.kakao.problem.assets.exception.ResultCode;

public class AcquireDeniedException extends BaseException {

	private static final long serialVersionUID = -2974849512810193094L;

	public AcquireDeniedException() {
		super(ResultCode.ACQUIRE_DENIED);
	}
}
