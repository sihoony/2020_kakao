package com.kakao.problem.distribution.infra;

import com.kakao.problem.distribution.domain.Distribution;
import com.kakao.problem.distribution.domain.DistributionRepository;
import com.kakao.problem.distribution.infra.read.DistributionReadAccess;
import com.kakao.problem.distribution.infra.write.DistributionWriteAccess;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DistributionRepositoryImpl implements DistributionRepository {

  private final DistributionWriteAccess distributionWriteAccess;

  private final DistributionReadAccess distributionReadAccess;

  private final RedisTemplate<String, Long> redisTemplate;


  @Override
  public Distribution save(Distribution distribution) {

    Distribution result = distributionWriteAccess.save(distribution);
    redisTemplate.opsForList().leftPushAll(getRedisKey(result), result.getReceiverIds());

    return result;
  }

  @Override
  public Long preemptiveReceiverId(String roomId, String token) {
    return redisTemplate.opsForList().leftPop(getRedisKey(roomId, token));
  }

  @Override
  public Distribution findByTokenAndRoomId(String token, String roomId) {
    return distributionReadAccess.findByTokenAndRoomId(token, roomId);
  }

  private String getRedisKey(Distribution distribution){
    return getRedisKey(distribution.getRoomId(), distribution.getToken());
  }

  private String getRedisKey(String roomId, String token){
    return roomId + ":" + token;
  }
}
