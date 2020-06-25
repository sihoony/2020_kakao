package com.example.problem.distribution.domain;

import com.example.problem.assets.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DistributionReceiver extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;

  @Column(nullable = false)
  private Long amount;

  public DistributionReceiver(Long amount) {
    this.amount = amount;
  }

}
