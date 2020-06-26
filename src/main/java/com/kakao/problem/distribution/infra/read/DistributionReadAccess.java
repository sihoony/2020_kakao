package com.kakao.problem.distribution.infra.read;

import com.kakao.problem.distribution.domain.Distribution;

public interface DistributionReadAccess {

	Distribution findByTokenAndRoomId(String token, String roomId);

}
