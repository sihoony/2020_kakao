package com.kakao.problem.distribution.application;

import com.kakao.problem.assets.token.TokenGenerator;
import com.kakao.problem.distribution.application.request.DistributionAcquireRequest;
import com.kakao.problem.distribution.application.request.DistributionCreateRequest;
import com.kakao.problem.distribution.application.request.DistributionFindRequest;
import com.kakao.problem.distribution.application.response.DistributionAcquireResponse;
import com.kakao.problem.distribution.application.response.DistributionCreateResponse;
import com.kakao.problem.distribution.application.response.DistributionFindResponse;
import com.kakao.problem.distribution.domain.Distribution;
import com.kakao.problem.distribution.domain.DistributionReceiver;
import com.kakao.problem.distribution.domain.DistributionRepository;
import com.kakao.problem.distribution.domain.ReceiverStatus;
import com.kakao.problem.distribution.exptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
@DisplayName("카카오페이 뿌리기 기능 비즈니스 로직 테스트.")
class DistributionServiceTest extends BaseApplicationFixture {

  @Mock
  private DistributionRepository distributionRepository;

  @Mock
  private TokenGenerator tokenGenerator;

  @InjectMocks
  private DistributionService distributionService;

  @Nested
  @DisplayName(" 뿌리기 Service ")
  class CreateService {

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
  @DisplayName(" 받기 Service ")
  class AcquireService {

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
      final int excessRange = 10;
      ReflectionTestUtils.setField(distribution, "createdDate", nowTime.minusMinutes(excessRange).minusSeconds(1));

      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(distribution);

      //when
      //then
      thenThrownBy( () ->

              distributionService.distributionAcquire(acquireRequest, requestHeader)
      ).isInstanceOf(ExpiredRequestException.class);
    }
  }

  @Nested
  @DisplayName(" 조회 Service ")
  class FindService {

    private final List<DistributionReceiver> distributionReceivers = new ArrayList<>();
    private final List<Long> FIXTURE_RECEIVER_AMOUNT = Arrays.asList(121L, 552L, 10L);

    private final Long FIND_FIXTURE_USER_ID = 8294L;
    private final Long FIXTURE_WAIT_AMOUNT = 200L;

    private DistributionFindRequest distributionFindRequest;

    @BeforeEach
    void setUp() {

      distributionFindRequest = new DistributionFindRequest();
      distributionFindRequest.setToken(TOKEN);

      for (Long amount : FIXTURE_RECEIVER_AMOUNT) {

        DistributionReceiver distributionReceiver = new DistributionReceiver(amount);
        distributionReceiver.setStatus(ReceiverStatus.COMPLETE);
        distributionReceiver.setUserId(USER_ID + amount);

        distributionReceivers.add(distributionReceiver);
      }

      distributionReceivers.add(new DistributionReceiver(FIXTURE_WAIT_AMOUNT));

      distribution.setReceivers(distributionReceivers);
    }

    @Test
    @DisplayName(" - 정상")
    void success(){

      //given
      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(distribution);


      //when
      DistributionFindResponse response = distributionService.distributionFind(distributionFindRequest, requestHeader);


      //then
      then(response).isNotNull();
      then(response.getTotalAmount()).isEqualTo(AMOUNT);
      then(response.getCompletionAmount())
              .isEqualTo(
                      FIXTURE_RECEIVER_AMOUNT
                              .stream()
                              .mapToLong(Long::longValue)
                              .sum()
              );

      then(response.getCreatedDate()).isNotNull();
      then(response.getRecivers()).isNotNull();
      then(response.getRecivers().size()).isEqualTo(FIXTURE_RECEIVER_AMOUNT.size());

      then(response.getRecivers()
              .stream()
              .map(DistributionFindResponse.Reciver::getAmount)
              .collect(Collectors.toList())
      ).doesNotContain(FIXTURE_WAIT_AMOUNT);

      then(response.getRecivers()
              .stream()
              .map(DistributionFindResponse.Reciver::getUserId)
              .collect(Collectors.toList())
      ).doesNotContainNull();

    }

    @Test
    @DisplayName(" - 다른사람의 뿌리기건이나 유효하지 않은 token은 조회할 수 없습니다.")
    void token_and_user_test(){

      //given
      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(null);

      //when
      //then
      thenThrownBy( () ->

              distributionService.distributionFind(distributionFindRequest, requestHeader)
      ).isExactlyInstanceOf(NotFoundDistributionException.class);
    }

    @Test
    @DisplayName(" - 뿌린 사람 자신만 조회를 할 수 있습니다.")
    void not_creator_test(){

      //given
      distribution.setUserId(FIND_FIXTURE_USER_ID);
      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(distribution);

      //when
      //then
      thenThrownBy( () ->

              distributionService.distributionFind(distributionFindRequest, requestHeader)
      ).isExactlyInstanceOf(NotCreatorException.class);
    }

    @Test
    @DisplayName(" - 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.")
    void read_restriction_test(){

      //given
      final int excessRange = 8;
      ReflectionTestUtils.setField(distribution, "createdDate", nowTime.minusDays(excessRange));
      given(distributionRepository.findByTokenAndRoomId(isA(String.class), isA(String.class)))
              .willReturn(distribution);

      //when
      //then
      thenThrownBy( () ->

              distributionService.distributionFind(distributionFindRequest, requestHeader)
      ).isExactlyInstanceOf(ReadRestrictionException.class);
    }
  }
}