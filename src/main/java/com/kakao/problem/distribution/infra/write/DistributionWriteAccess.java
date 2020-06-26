package com.kakao.problem.distribution.infra.write;

import com.kakao.problem.distribution.domain.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistributionWriteAccess extends JpaRepository<Distribution, Long> {

	Distribution findByTokenAndRoomId(String token, String roomId);

}
