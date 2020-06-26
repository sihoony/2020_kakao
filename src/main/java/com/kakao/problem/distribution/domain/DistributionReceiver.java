package com.kakao.problem.distribution.domain;

import com.kakao.problem.assets.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class DistributionReceiver extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;

  @Column(nullable = false)
  private Long amount;

  @Enumerated(EnumType.STRING)
  private ReceiverStatus status = ReceiverStatus.WAIT;

  public DistributionReceiver(Long amount) {
    this.amount = amount;
  }

  public boolean isComplete(){
    return this.status == ReceiverStatus.COMPLETE;
  }

  public boolean isWait(){
    return this.status == ReceiverStatus.WAIT;
  }
}
