package com.kakao.problem.distribution.infra;

import com.kakao.problem.distribution.domain.Distribution;
import com.kakao.problem.distribution.domain.DistributionRepository;
import com.kakao.problem.distribution.infra.read.DistributionReadAccess;
import com.kakao.problem.distribution.infra.write.DistributionWriteAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DistributionRepositoryImpl implements DistributionRepository {

  private final DistributionWriteAccess distributionWriteAccess;

  private final DistributionReadAccess distributionReadAccess;


  @Override
  public Distribution save(Distribution distribution) {

    return distributionWriteAccess.save(distribution);
  }

  @Override
  public Distribution findByTokenAndRoomId(String token, String roomId) {

    return distributionReadAccess.findByTokenAndRoomId(token, roomId);
  }
}
