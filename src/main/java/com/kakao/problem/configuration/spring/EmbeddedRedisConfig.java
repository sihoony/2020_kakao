package com.kakao.problem.configuration.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

  @Value("${spring.redis.port}")
  private int redisPort;

  @Bean
  public RedisServerBean redisServer() {
    return new RedisServerBean();
  }

  class RedisServerBean implements InitializingBean, DisposableBean {
    private RedisServer redisServer;

    public void afterPropertiesSet() throws Exception {
      redisServer = new RedisServer(redisPort);
      redisServer.start();
    }

    public void destroy() throws Exception {
      if (redisServer != null) {
        redisServer.stop();
      }
    }
  }
}
