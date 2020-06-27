package com.kakao.problem.distribution.ui;

import com.kakao.problem.assets.entrypoints.RequestHeader;
import com.kakao.problem.assets.exception.ResultCode;
import com.kakao.problem.distribution.application.DistributionService;
import com.kakao.problem.distribution.application.request.DistributionAcquireRequest;
import com.kakao.problem.distribution.application.request.DistributionCreateRequest;
import com.kakao.problem.distribution.application.request.DistributionFindRequest;
import com.kakao.problem.distribution.application.response.DistributionAcquireResponse;
import com.kakao.problem.distribution.application.response.DistributionCreateResponse;
import com.kakao.problem.distribution.application.response.DistributionFindResponse;
import com.kakao.problem.distribution.domain.DistributionReceiver;
import com.kakao.problem.distribution.domain.ReceiverStatus;
import com.kakao.problem.distribution.exptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DisplayName("Entrypoints API")
class DistributionControllerTest {

  private static final String X_ROOM_ID = "X-ROOM-ID";
  private static final String X_USER_ID = "X-USER-ID";

  private static final Long AMOUNT = 1002L;

  private static final String ROOM_ID = "20200626";
  private static final Long USER_ID = 1004L;


  private static final int SUCCESS_CODE = 200;
  private static final String SUCCESS_MESSAGE = "OK";

  private final String TOKEN = "aTc";

  private final List<DistributionReceiver> distributionReceivers = new ArrayList<>();
  private final List<Long> FIXTURE_RECEIVER_AMOUNT = Arrays.asList(121L, 552L, 10L);

  @MockBean
  private DistributionService distributionService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {

    mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .build();

    for (Long amount : FIXTURE_RECEIVER_AMOUNT) {

      DistributionReceiver distributionReceiver = new DistributionReceiver(amount);
      distributionReceiver.setStatus(ReceiverStatus.COMPLETE);
      distributionReceiver.setUserId(USER_ID + amount);

      distributionReceivers.add(distributionReceiver);
    }

  }

  @Nested
  @DisplayName("뿌리기 API")
  class DistributionCreate {

    @Test
    @DisplayName(" - 정상 등록")
    void create() throws Exception {

      //given
      DistributionCreateResponse distributionCreateResponse = new DistributionCreateResponse(TOKEN);
      given(distributionService.distributionCreate(isA(DistributionCreateRequest.class), any(RequestHeader.class)))
              .willReturn(distributionCreateResponse);


      final ResultActions actions = mockMvc.perform(post("/distribution")
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON)
              .content("{ \"amount\": 1002, \"people\": 4 } "))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(SUCCESS_CODE))
              .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
              .andExpect(jsonPath("$.data").isNotEmpty())
              .andExpect(jsonPath("$.data.token").value(TOKEN));
    }

    @Test
    @DisplayName(" - 필수 정보가 없는 경우")
    void invalid_paramter_create() throws Exception {

      //given
      final ResultActions actions = mockMvc.perform(post("/distribution")
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON)
              .content("{ \"amount\": 1002} "))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(ResultCode.BAD_REQUEST.code()))
              .andExpect(jsonPath("$.message").value(ResultCode.BAD_REQUEST.type()))
              .andExpect(jsonPath("$.data").isNotEmpty());
    }
  }

  @Nested
  @DisplayName("받기 API")
  class DistributionAcquire {

    @Test
    @DisplayName(" - 정상 받기")
    void acquire() throws Exception {

      //given
      DistributionAcquireResponse distributionAcquireResponse = new DistributionAcquireResponse(FIXTURE_RECEIVER_AMOUNT.get(0));
      given(distributionService.distributionAcquire(isA(DistributionAcquireRequest.class), any(RequestHeader.class)))
              .willReturn(distributionAcquireResponse);


      final ResultActions actions = mockMvc.perform(put("/distribution/{token}", TOKEN)
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(SUCCESS_CODE))
              .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
              .andExpect(jsonPath("$.data").isNotEmpty())
              .andExpect(jsonPath("$.data.amount").value(FIXTURE_RECEIVER_AMOUNT.get(0)));
    }

    @Test
    @DisplayName(" - 조회하려는 데이터가 없는 경우")
    void not_found_distribution_test() throws Exception {

      //given
      given(distributionService.distributionAcquire(isA(DistributionAcquireRequest.class), any(RequestHeader.class)))
              .willThrow(new NotFoundDistributionException());


      final ResultActions actions = mockMvc.perform(put("/distribution/{token}", TOKEN)
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(ResultCode.NOT_FOUND_DISTRIBUTION.code()))
              .andExpect(jsonPath("$.message").value(ResultCode.NOT_FOUND_DISTRIBUTION.type()));
    }

    @Test
    @DisplayName(" - 뿌린지 10분이 지난 경우")
    void expired_request_test() throws Exception {

      //given
      given(distributionService.distributionAcquire(isA(DistributionAcquireRequest.class), any(RequestHeader.class)))
              .willThrow(new ExpiredRequestException());


      final ResultActions actions = mockMvc.perform(put("/distribution/{token}", TOKEN)
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(ResultCode.EXPIRED_REQUEST.code()))
              .andExpect(jsonPath("$.message").value(ResultCode.EXPIRED_REQUEST.type()));
    }

    @Test
    @DisplayName(" - 등록한 사용자의 경우")
    void acquire_denied_test() throws Exception {

      //given
      given(distributionService.distributionAcquire(isA(DistributionAcquireRequest.class), any(RequestHeader.class)))
              .willThrow(new AcquireDeniedException());


      final ResultActions actions = mockMvc.perform(put("/distribution/{token}", TOKEN)
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(ResultCode.ACQUIRE_DENIED.code()))
              .andExpect(jsonPath("$.message").value(ResultCode.ACQUIRE_DENIED.type()));
    }

    @Test
    @DisplayName(" - 중복 참여인 경우")
    void duplication_acquire_test() throws Exception {

      //given
      given(distributionService.distributionAcquire(isA(DistributionAcquireRequest.class), any(RequestHeader.class)))
              .willThrow(new DuplicationAcquireException());


      final ResultActions actions = mockMvc.perform(put("/distribution/{token}", TOKEN)
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(ResultCode.DUPLICATION_ACQUIRE.code()))
              .andExpect(jsonPath("$.message").value(ResultCode.DUPLICATION_ACQUIRE.type()));
    }
  }

  @Nested
  @DisplayName("조회 API")
  class DistributionFind {

    @Test
    @DisplayName(" - 정상 조회")
    void find() throws Exception {

      //given
      DistributionFindResponse distributionFindResponse = new DistributionFindResponse(distributionReceivers, AMOUNT, LocalDateTime.now());
      given(distributionService.distributionFind(isA(DistributionFindRequest.class), any(RequestHeader.class)))
              .willReturn(distributionFindResponse);


      final ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/distribution/{token}", TOKEN)
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(SUCCESS_CODE))
              .andExpect(jsonPath("$.message").value(SUCCESS_MESSAGE))
              .andExpect(jsonPath("$.data").isNotEmpty())
              .andExpect(jsonPath("$.data.createdDate").isNotEmpty())
              .andExpect(jsonPath("$.data.totalAmount").value(AMOUNT))
              .andExpect(jsonPath("$.data.completionAmount").value(
                      FIXTURE_RECEIVER_AMOUNT
                              .stream()
                              .mapToLong(Long::longValue)
                              .sum()))
              .andExpect(jsonPath("$.data.recivers").isArray())
              .andExpect(jsonPath("$.data.recivers[0].userId").value(USER_ID + FIXTURE_RECEIVER_AMOUNT.get(0)))
              .andExpect(jsonPath("$.data.recivers[0].amount").value(FIXTURE_RECEIVER_AMOUNT.get(0)));
    }

    @Test
    @DisplayName(" - X-ROOM-ID가 없는 경우")
    void remove_x_room_id_test() throws Exception {

      //given
      //when
      final ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/distribution/{token}", TOKEN)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(ResultCode.RUNTIME_EXCEPTION.code()))
              .andExpect(jsonPath("$.message").value(ResultCode.RUNTIME_EXCEPTION.type()));
    }

    @Test
    @DisplayName(" - 조회하려는 데이터가 없는 경우")
    void not_found_distribution_test() throws Exception {

      //given
      given(distributionService.distributionFind(isA(DistributionFindRequest.class), any(RequestHeader.class)))
              .willThrow(new NotFoundDistributionException());

      //when
      final ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/distribution/{token}", TOKEN)
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(ResultCode.NOT_FOUND_DISTRIBUTION.code()))
              .andExpect(jsonPath("$.message").value(ResultCode.NOT_FOUND_DISTRIBUTION.type()));
    }

    @Test
    @DisplayName(" - 조회 권한이 없는 경우")
    void not_creator_test() throws Exception {

      //given
      given(distributionService.distributionFind(isA(DistributionFindRequest.class), any(RequestHeader.class)))
              .willThrow(new NotCreatorException());

      //when
      final ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/distribution/{token}", TOKEN)
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(ResultCode.NOT_CREATOR.code()))
              .andExpect(jsonPath("$.message").value(ResultCode.NOT_CREATOR.type()));
    }

    @Test
    @DisplayName(" - 뿌리기를 실행한지 7일이 지난 경우")
    void read_restriction_test() throws Exception {

      //given
      given(distributionService.distributionFind(isA(DistributionFindRequest.class), any(RequestHeader.class)))
              .willThrow(new ReadRestrictionException());

      //when
      final ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/distribution/{token}", TOKEN)
              .header(X_ROOM_ID, ROOM_ID)
              .header(X_USER_ID, USER_ID)
              .contentType(MediaType.APPLICATION_JSON))
              .andDo(print());


      actions.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.result").value(ResultCode.READ_RESTRICTION.code()))
              .andExpect(jsonPath("$.message").value(ResultCode.READ_RESTRICTION.type()));
    }
  }
}