package com.kakao.problem.distribution.domain;

import com.kakao.problem.configuration.spring.EmbeddedRedisConfig;
import com.kakao.problem.configuration.spring.RootConfiguration;
import com.kakao.problem.distribution.infra.DistributionRepositoryImpl;
import com.kakao.problem.distribution.infra.read.DistributionReadAccessImpl;
import com.kakao.problem.distribution.infra.write.DistributionWriteAccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import static org.assertj.core.api.BDDAssertions.then;

@Import(EmbeddedRedisConfig.class)
@SpringBootTest
@Profile("local")
@DisplayName("Distribution Repository Test,")
class DistributionRepositoryTest {

  private final String ROOM_ID = "20200626";
  private final String FIXTURE_ROOM_ID = "20203626";

  private final Long USER_ID = 1004L;
  private final Long AMOUNT = 1002L;
  private final Long PEOPLE = 4L;

  private final String TOKEN = "aTc";

  @Autowired
  private DistributionRepository distributionRepository;

  @Autowired
  private DomainService domainService;

  @BeforeEach
  public void setup() {

    Distribution distribution = createDomain();
    distribution.setRoomId(FIXTURE_ROOM_ID);

    distributionRepository.save(distribution);
  }

  @Test
  @DisplayName(" - 저장 확인")
  void save(){

    //given
    Distribution distribution = createDomain();


    //when
    Distribution result = distributionRepository.save(distribution);


    //then
    then(result).isNotNull();
    then(result.getUserId()).isEqualTo(USER_ID);
    then(result.getToken()).isEqualTo(TOKEN);
    then(result.getRoomId()).isEqualTo(ROOM_ID);
    then(result.getAmount()).isEqualTo(AMOUNT);
    then(result.getPeople()).isEqualTo(PEOPLE);
    then(result.getCreatedDate()).isNotNull();
    then(result.getModifiedDate()).isNotNull();
    then(result.getReceivers()).isNotNull();
    then(result.getReceivers().size()).isEqualTo(PEOPLE.intValue());

    then(result.getReceivers().stream()
            .map(DistributionReceiver::getReceiverId)
            .collect(Collectors.toList()))
            .doesNotContainNull();
    then(result.getReceivers().stream()
            .map(DistributionReceiver::getUserId)
            .collect(Collectors.toList()))
            .containsNull();
    then(result.getReceivers().stream()
            .map(DistributionReceiver::getStatus)
            .collect(Collectors.toList()))
            .contains(ReceiverStatus.WAIT);
  }

  @Test
  @DisplayName(" - Distribution 정보 확인")
  void find_token_and_roomid(){

    //given
    //when
    Distribution result = distributionRepository.findByTokenAndRoomId(TOKEN, FIXTURE_ROOM_ID);


    //then
    then(result).isNotNull();
    then(result.getUserId()).isEqualTo(USER_ID);
    then(result.getToken()).isEqualTo(TOKEN);
    then(result.getRoomId()).isEqualTo(FIXTURE_ROOM_ID);
    then(result.getAmount()).isEqualTo(AMOUNT);
    then(result.getPeople()).isEqualTo(PEOPLE);
    then(result.getCreatedDate()).isNotNull();
    then(result.getModifiedDate()).isNotNull();
    then(result.getReceivers()).isNotNull();
    then(result.getReceivers().size()).isEqualTo(PEOPLE.intValue());

  }

  @Test
  @DisplayName(" - 받은 정보 업데이트")
  void update_receivers_info(){

    //given
    Random random = new Random();

    Distribution changeData = distributionRepository.findByTokenAndRoomId(TOKEN, FIXTURE_ROOM_ID);
    changeData.getReceivers()
            .forEach(receiver -> {
              receiver.setStatus(ReceiverStatus.COMPLETE);
              receiver.setUserId((long)random.nextInt(USER_ID.intValue()));
            });


    //when
    Distribution result = distributionRepository.findByTokenAndRoomId(TOKEN, FIXTURE_ROOM_ID);


    //then
    then(result).isNotNull();
    then(result.getUserId()).isEqualTo(USER_ID);
    then(result.getToken()).isEqualTo(TOKEN);
    then(result.getRoomId()).isEqualTo(FIXTURE_ROOM_ID);
    then(result.getAmount()).isEqualTo(AMOUNT);
    then(result.getPeople()).isEqualTo(PEOPLE);
    then(result.getCreatedDate()).isNotNull();
    then(result.getModifiedDate()).isNotNull();
    then(result.getReceivers()).isNotNull();
    then(result.getReceivers().size()).isEqualTo(PEOPLE.intValue());
    then(result.getReceivers().stream()
            .map(DistributionReceiver::isComplete)
            .count())
            .isEqualTo(PEOPLE.intValue());

    then(result.getReceivers().stream()
            .map(DistributionReceiver::getCreatedDate)
            .collect(Collectors.toList()))
            .doesNotContainNull();
    then(result.getReceivers().stream()
            .map(DistributionReceiver::getModifiedDate)
            .collect(Collectors.toList()))
            .doesNotContainNull();
  }

  Distribution createDomain(){

    Distribution distribution = new Distribution();
    distribution.setUserId(USER_ID);
    distribution.setToken(TOKEN);
    distribution.setRoomId(ROOM_ID);
    distribution.setAmount(AMOUNT);
    distribution.setPeople(PEOPLE);

    domainService.randomPrices(distribution);

    return distribution;
  }

}