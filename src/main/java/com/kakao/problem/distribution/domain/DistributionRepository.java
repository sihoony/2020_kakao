package com.kakao.problem.distribution.domain;

public interface DistributionRepository{

  Distribution save(Distribution distribution);

  Long preemptiveReceiverId(String roomId, String token);

  Distribution findByTokenAndRoomId(String token, String roomId);

}
