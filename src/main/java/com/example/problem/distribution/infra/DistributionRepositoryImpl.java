package com.example.problem.distribution.infra;

import com.example.problem.distribution.domain.Distribution;
import com.example.problem.distribution.domain.DistributionRepository;
import com.example.problem.distribution.infra.read.DistributionReadAccess;
import com.example.problem.distribution.infra.write.DistributionWriteAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
