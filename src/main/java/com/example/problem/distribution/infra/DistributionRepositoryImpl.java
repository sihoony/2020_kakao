package com.example.problem.distribution.infra;

import com.example.problem.distribution.domain.Distribution;
import com.example.problem.distribution.domain.DistributionRepository;
import com.example.problem.distribution.infra.write.DistributionWriteAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class DistributionRepositoryImpl implements DistributionRepository {

  private final DistributionWriteAccess distributionWriteAccess;

  @Override
  @Transactional(Transactional.TxType.REQUIRED)
  public Distribution save(Distribution distribution) {

    return distributionWriteAccess.save(distribution);
  }
}
