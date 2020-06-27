package com.kakao.problem.distribution.domain;

public interface DistributionRepository{

  Distribution save(Distribution distribution);

  Distribution findByTokenAndRoomId(String token, String roomId);

}
