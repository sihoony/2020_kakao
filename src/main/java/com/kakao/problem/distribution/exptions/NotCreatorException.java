package com.kakao.problem.distribution.exptions;

import com.kakao.problem.assets.exception.BaseException;
import com.kakao.problem.assets.exception.ResultCode;

public class NotCreatorException extends BaseException {

	private static final long serialVersionUID = -1855230092940071563L;

	public NotCreatorException() {
		super(ResultCode.NOT_CREATOR);
	}
}
