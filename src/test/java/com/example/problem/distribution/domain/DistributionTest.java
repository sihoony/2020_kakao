package com.example.problem.distribution.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

class DistributionTest {

  private final LocalDateTime nowTime = LocalDateTime.now(ZoneId.systemDefault());

  private LocalDateTime expireTime;


  @Nested
  @DisplayName("뿌린 건은 10분간만 유효합니다.")
  class distributionOperationTest {

    private static final long PEOPLE_COUNT = 4L;

    @Test
    @DisplayName(" - 정상 분배")
    void amount_distribution(){

      //given
      Distribution distribution = new Distribution();
      distribution.setPeople(PEOPLE_COUNT);


      //when
      distribution.distributionOperation();


      //then
      List<DistributionReceiver> receivers = distribution.getReceivers();

      then(receivers).isNotNull();
      then(receivers.size()).isEqualTo(PEOPLE_COUNT);

    }

  }

  @Nested
  @DisplayName("뿌린 건은 10분간만 유효합니다.")
  class expireTimeTest {

    private static final int RANGE = 10;
    private static final int EXCESS_RANGE = 11;

    @Test
    @DisplayName(" - 정상 확인")
    void normal_time_check() {

      //given
      Distribution distribution = new Distribution();
      ReflectionTestUtils.setField(distribution, "createdDate", nowTime);


      //when
      final boolean isExpire = distribution.isExpireTime();


      then(isExpire).isFalse();
    }

    @Test
    @DisplayName(" - 정상 범위 확인")
    void range_check() {

      //given
      expireTime = nowTime.minusMinutes(RANGE);

      Distribution distribution = new Distribution();
      ReflectionTestUtils.setField(distribution, "createdDate", expireTime);


      //when
      final boolean isExpire = distribution.isExpireTime();


      then(isExpire).isFalse();
    }


    @Test
    @DisplayName(" - 만료 확인")
    void expire_time_check() {

      //given
      expireTime = nowTime.minusMinutes(EXCESS_RANGE);

      Distribution distribution = new Distribution();
      ReflectionTestUtils.setField(distribution, "createdDate", expireTime);


      //when
      final boolean isExpire = distribution.isExpireTime();


      then(isExpire).isTrue();
    }
  }

  @Nested
  @DisplayName("뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.")
  class readRestrictionTest {

    private static final int RANGE = 7;
    private static final int EXCESS_RANGE = 8;

    @Test
    @DisplayName(" - 정상 확인")
    void read_time_check() {

      //given
      Distribution distribution = new Distribution();
      ReflectionTestUtils.setField(distribution, "createdDate", nowTime);


      //when
      final boolean isExpire = distribution.isReadRestriction();


      then(isExpire).isFalse();
    }

    @Test
    @DisplayName(" - 정상 범위 확인")
    void range_check() {

      //given
      expireTime = nowTime.minusDays(RANGE);

      Distribution distribution = new Distribution();
      ReflectionTestUtils.setField(distribution, "createdDate", nowTime);


      //when
      final boolean isExpire = distribution.isReadRestriction();


      then(isExpire).isFalse();
    }

    @Test
    @DisplayName(" - 만료 확인")
    void not_read_check() {

      //given
      expireTime = nowTime.minusDays(EXCESS_RANGE);

      Distribution distribution = new Distribution();
      ReflectionTestUtils.setField(distribution, "createdDate", expireTime);


      //when
      final boolean isExpire = distribution.isReadRestriction();


      then(isExpire).isTrue();
    }
  }

}