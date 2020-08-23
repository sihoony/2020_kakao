package com.kakao.problem.distribution.application.support;

import com.kakao.problem.assets.entrypoints.RequestHeader;
import com.kakao.problem.distribution.application.request.DistributionCreateRequest;
import com.kakao.problem.distribution.domain.Distribution;
import org.springframework.stereotype.Component;

@Component
public class DistributionConvert {

  public Distribution to(DistributionCreateRequest createRequest, String token, RequestHeader requestHeader){

    Distribution distribution = new Distribution();
    distribution.setAmount(createRequest.getAmount());
    distribution.setPeople(createRequest.getPeople());
    distribution.setUserId(requestHeader.getXUserId());
    distribution.setRoomId(requestHeader.getXRoomId());
    distribution.setToken(token);

    return distribution;
  }

}
