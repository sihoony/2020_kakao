package com.kakao.problem.distribution.application;

import com.kakao.problem.assets.entrypoints.RequestHeader;
import com.kakao.problem.distribution.domain.Distribution;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class BaseApplicationFixture {

  private static final String X_ROOM_ID = "X-ROOM-ID";
  private static final String X_USER_ID = "X-USER-ID";

  protected LocalDateTime nowTime;

  protected RequestHeader requestHeader;
  protected Distribution distribution;

  protected final Long AMOUNT = 1002L;
  protected final Long PEOPLE = 4L;

  protected final String ROOM_ID = "20200626";
  protected final Long USER_ID = 1004L;

  protected final String TOKEN = "aTc";

  @BeforeEach
  void before(){

    nowTime = LocalDateTime.now(ZoneId.systemDefault());

    distribution = new Distribution();
    distribution.setAmount(AMOUNT);
    distribution.setPeople(PEOPLE);
    distribution.setRoomId(ROOM_ID);
    distribution.setUserId(USER_ID);

    MockHttpServletRequest mockRequest = new MockHttpServletRequest();
    mockRequest.addHeader(X_ROOM_ID, ROOM_ID);
    mockRequest.addHeader(X_USER_ID, USER_ID);

    requestHeader = RequestHeader.builder()
            .request(mockRequest)
            .build();

    ReflectionTestUtils.setField(distribution, "createdDate", nowTime);
  }
}
