package com.kakao.problem.distribution.exptions;

import com.kakao.problem.assets.exception.BaseException;
import com.kakao.problem.assets.exception.ResultCode;

public class DistributionCompleteException extends BaseException {

	private static final long serialVersionUID = 4040525634985345380L;

	public DistributionCompleteException() {
		super(ResultCode.DISTRIBUTION_COMPLETE);
	}
}
