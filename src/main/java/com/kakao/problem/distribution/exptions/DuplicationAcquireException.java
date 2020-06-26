package com.kakao.problem.distribution.exptions;

import com.kakao.problem.assets.exception.BaseException;
import com.kakao.problem.assets.exception.ResultCode;

public class DuplicationAcquireException extends BaseException {

	private static final long serialVersionUID = -426473057356150551L;

	public DuplicationAcquireException() {
		super(ResultCode.DUPLICATION_ACQUIRE);
	}
}
