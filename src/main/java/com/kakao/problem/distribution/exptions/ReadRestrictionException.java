package com.kakao.problem.distribution.exptions;

import com.kakao.problem.assets.exception.BaseException;
import com.kakao.problem.assets.exception.ResultCode;

public class ReadRestrictionException extends BaseException {

	private static final long serialVersionUID = -3526158699897650853L;

	public ReadRestrictionException() {
		super(ResultCode.READ_RESTRICTION);
	}
}
