package com.kakao.problem.distribution.infra;

import com.kakao.problem.distribution.domain.Distribution;
import com.kakao.problem.distribution.infra.read.DistributionReadAccess;
import com.kakao.problem.distribution.infra.write.DistributionWriteAccess;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DistributionRepositoryImpl {

  private final DistributionWriteAccess distributionWriteAccess;

  private final DistributionReadAccess distributionReadAccess;


  public Distribution save(Distribution distribution) {

    return distributionWriteAccess.save(distribution);
  }

  public Distribution findByTokenAndRoomId(String token, String roomId) {

    return distributionReadAccess.findByTokenAndRoomId(token, roomId);
  }
}
