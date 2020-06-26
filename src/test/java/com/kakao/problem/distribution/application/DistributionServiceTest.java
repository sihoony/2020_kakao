package com.kakao.problem.distribution.application;

import com.kakao.problem.assets.entrypoints.RequestHeader;
import com.kakao.problem.assets.token.TokenGenerator;
import com.kakao.problem.distribution.application.request.DistributionAcquireRequest;
import com.kakao.problem.distribution.application.request.DistributionCreateRequest;
import com.kakao.problem.distribution.application.response.DistributionAcquireResponse;
import com.kakao.problem.distribution.application.response.DistributionCreateResponse;
import com.kakao.problem.distribution.domain.Distribution;
import com.kakao.problem.distribution.domain.DistributionReceiver;
import com.kakao.problem.distribution.domain.DistributionRepository;
import com.kakao.problem.distribution.domain.ReceiverStatus;
import com.kakao.problem.distribution.exptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
@DisplayName("카카오페이 뿌리기 기능 비즈니스 로직 테스트.")
class DistributionServiceTest extends BaseFixture{

  @Mock
  private DistributionRepository distributionRepository;

  @Mock
  private TokenGenerator tokenGenerator;

  @InjectMocks
  private DistributionService distributionService;

  @Nested
  @DisplayName(" 뿌리기 API ")
  class Create {

    @Test
    @DisplayName(" - 정상")
    void success(){

      //given
      DistributionCreateRequest createRequest = new DistributionCreateRequest();
      createRequest.setAmount(AMOUNT);
      createRequest.setPeople(PEOPLE);

      given(tokenGenerator.randomToken()).willReturn(TOKEN);
      given(distributionRepository.save(any(Distribution.class)))
              .willReturn(new Distribution());


      //when
      DistributionCreateResponse response = distributionService.distributionCreate(createRequest, requestHeader);


      //then
      then(response).isNotNull();
      then(response.getToken()).isEqualTo(TOKEN);
    }

  }

  @Nested
  @DisplayName(" 받기 API ")
  class Acquire {

    private final List<DistributionReceiver> distributionReceivers = new ArrayList<>();
    private final List<Long> FIXTURE_RECEIVER_AMOUNT = Arrays.asList(121L, 552L);

    private final Long ACQUIRE_FIXTURE_USER_ID = 8294L;

    private DistributionAcquireRequest acquireRequest;

    @BeforeEach
    void setUp() {


      acquireRequest = new DistributionAcquireRequest();
      acquireRequest.setToken(TOKEN);

      distributionReceivers.add(new DistributionReceiver(FIXTURE_RECEIVER_AMOUNT.get(0)));
      distributionReceivers.add(new DistributionReceiver(FIXTURE_RECEIVER_AMOUNT.get(1)));

      ReflectionTestUtils.setField(requestHeader, "xUserId", ACQUIRE_FIXTURE_USER_ID);

      distribution.setReceivers(distributionReceivers);

    }

    @Test
    @DisplayName(" - 정상")
    void success(){

      //given
      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(distribution);


      //when
      DistributionAcquireResponse response = distributionService.distributionAcquire(acquireRequest, requestHeader);


      //then
      then(response).isNotNull();
      then(response.getAmount()).isEqualTo(FIXTURE_RECEIVER_AMOUNT.get(0));

    }

    @Test
    @DisplayName(" - 동일한 token과 대화방에 속한 사용자만이 받을 수 있습니다.")
    void same_of_token_and_room_id_test(){

      //given
      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(null);

      //when
      //then
      thenThrownBy( () ->

              distributionService.distributionAcquire(acquireRequest, requestHeader)
      ).isExactlyInstanceOf(NotFoundDistributionException.class);
    }

    @Test
    @DisplayName(" - 자신이 뿌리기한 건은 자신이 받을 수 없습니다.")
    void same_user_test(){

      //given
      ReflectionTestUtils.setField(requestHeader, "xUserId", USER_ID);
      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(distribution);

      //when
      //then
      thenThrownBy( () ->

              distributionService.distributionAcquire(acquireRequest, requestHeader)
      ).isExactlyInstanceOf(AcquireDeniedException.class);
    }

    @Test
    @DisplayName(" - 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.")
    void duplication_test(){

      //given
      distributionReceivers.get(0).setUserId(ACQUIRE_FIXTURE_USER_ID);
      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(distribution);

      //when
      //then
      thenThrownBy( () ->

              distributionService.distributionAcquire(acquireRequest, requestHeader)
      ).isExactlyInstanceOf(DuplicationAcquireException.class);
    }

    @Test
    @DisplayName(" - 완료된 뿌리기는 받기를 실행 할 수 없습니다.")
    void distribution_complete_test(){

      //given
      distributionReceivers.forEach(distributionReceiver -> distributionReceiver.setStatus(ReceiverStatus.COMPLETE));
      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(distribution);

      //when
      //then
      thenThrownBy( () ->

              distributionService.distributionAcquire(acquireRequest, requestHeader)
      ).isExactlyInstanceOf(DistributionCompleteException.class);
    }

    @Test
    @DisplayName(" - 뿌린 건은 10분간만 유효합니다.")
    void expire_time_test(){

      //given
      final int excessRange = 11;
      ReflectionTestUtils.setField(distribution, "createdDate", nowTime.minusMinutes(excessRange));

      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(distribution);

      //when
      //then
      thenThrownBy( () ->

              distributionService.distributionAcquire(acquireRequest, requestHeader)
      ).isExactlyInstanceOf(ExpiredRequestException.class);
    }
  }

  @Nested
  @DisplayName(" 조회 API ")
  class Find {

    @Test
    @DisplayName(" - 정상")
    void success(){

    }
  }


}