package com.example.problem.distribution.domain;

import com.example.problem.assets.entity.BaseTimeEntity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
