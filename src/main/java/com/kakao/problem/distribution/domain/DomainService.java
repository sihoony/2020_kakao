package com.kakao.problem.distribution.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainService {

  private static final int DISTRIBUTION_MINIMUM = 1;

  private static final Random RANDOM = new Random();

  public void randomPrices(Distribution distribution){

    List<DistributionReceiver> receivers = new ArrayList<>();
    if(distribution.getAmount().equals(distribution.getPeople())){

      receivers.addAll(
          LongStream.range(0, distribution.getPeople())
              .mapToObj(operand -> new DistributionReceiver(distribution.getAmount()/distribution.getPeople()))
              .collect(Collectors.toList())
      );

      distribution.setReceivers(receivers);
      return;
    }

    long totalAmount = distribution.getAmount();

    Long[] distributionMoney = new Long[distribution.getPeople().intValue()];
    for (int i = 0; i < distribution.getPeople() - 1; i++) {
      long distributionRandomMoney = RANDOM.nextInt((int) totalAmount - (distribution.getPeople().intValue() - i)) + DISTRIBUTION_MINIMUM;

      totalAmount -= distributionRandomMoney;
      distributionMoney[i] = distributionRandomMoney;
    }

    distributionMoney[distributionMoney.length - 1] = totalAmount;

    receivers.addAll(
        Stream.of(distributionMoney)
            .map(DistributionReceiver::new)
            .collect(Collectors.toList())
    );

    distribution.setReceivers(receivers);
  }
}
