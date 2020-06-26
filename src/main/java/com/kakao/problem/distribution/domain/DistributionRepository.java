package com.kakao.problem.distribution.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributionRepository extends JpaRepository<Distribution, Long> {

  Distribution findByTokenAndRoomId(String token, String roomId);

}
