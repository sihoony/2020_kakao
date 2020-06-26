package com.kakao.problem.assets.entrypoints;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

@DisplayName("요청한 사용자의 특정 값들은 HTTP Header로 전달됩니다.")
class RequestHeaderTest {

  MockHttpServletRequest mockRequest = new MockHttpServletRequest();

  private final String X_ROOM_ID = "X-ROOM-ID";
  private final String X_USER_ID = "X-USER-ID";

  private final String ROOM_ID = "20200626";
  private final Long USER_ID = 1004L;

  @Test
  @DisplayName(" - 정상 확인")
  void header_success(){

    //given
    mockRequest.addHeader(X_ROOM_ID, ROOM_ID);
    mockRequest.addHeader(X_USER_ID, USER_ID);


    //when
    RequestHeader requestHeader = RequestHeader.builder()
            .request(mockRequest)
            .build();


    //then
    then(requestHeader).isNotNull();
    then(requestHeader.getXRoomId()).isNotBlank();
    then(requestHeader.getXRoomId()).isEqualTo(ROOM_ID);

    then(requestHeader.getXUserId()).isNotNull();
    then(requestHeader.getXUserId()).isEqualTo(USER_ID);

  }

  @Test
  @DisplayName(" - RoomId가 없는 경우 Exception 발생")
  void null_x_room_id(){

    //given
    mockRequest.addHeader(X_USER_ID, USER_ID);

    //when
    //then
    thenThrownBy( () ->
            RequestHeader.builder()
                    .request(mockRequest)
                    .build()
    ).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName(" - UserId가 없는 경우 Exception 발생")
  void null_x_user_id(){

    //given
    mockRequest.addHeader(X_ROOM_ID, ROOM_ID);

    //when
    //then
    thenThrownBy( () ->
            RequestHeader.builder()
                    .request(mockRequest)
                    .build()
    ).isExactlyInstanceOf(NullPointerException.class);
  }

}