package com.kakao.problem.distribution.exptions;

import com.kakao.problem.assets.exception.BaseException;
import com.kakao.problem.assets.exception.ResultCode;

public class NotFoundDistributionException extends BaseException {

	private static final long serialVersionUID = -6168284991175480362L;

	public NotFoundDistributionException() {
		super(ResultCode.NOT_FOUND_DISTRIBUTION);
	}
}
