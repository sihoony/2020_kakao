package com.kakao.problem.distribution.exptions;

import com.kakao.problem.assets.exception.BaseException;
import com.kakao.problem.assets.exception.ResultCode;

public class ExpiredRequestException extends BaseException {

	private static final long serialVersionUID = 6784300411717216078L;

	public ExpiredRequestException() {
		super(ResultCode.EXPIRED_REQUEST);
	}
}
