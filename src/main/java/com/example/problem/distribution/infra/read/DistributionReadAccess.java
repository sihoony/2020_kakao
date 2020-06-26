package com.example.problem.distribution.infra.read;

import com.example.problem.distribution.domain.Distribution;

public interface DistributionReadAccess {

	Distribution findByTokenAndRoomId(String token, String roomId);

}
