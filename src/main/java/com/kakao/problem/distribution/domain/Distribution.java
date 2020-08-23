package com.kakao.problem.distribution.domain;

import com.kakao.problem.assets.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Distribution extends BaseTimeEntity {

	private static final int EXPTIRE_MINUS_MINUTE = 10;
	private static final int READ_LIMIT_DAYS = 7;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long amount;

	@Column(nullable = false)
	private String token;

	@Column(nullable = false)
	private Long people;

	@Column(nullable = false)
	private String roomId;

	@JoinColumn(name = "distribution_id")
	@OneToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<DistributionReceiver> receivers = new ArrayList<>();

	public boolean isExpireTime(){
		return this.getCreatedDate()
						.plusMinutes(EXPTIRE_MINUS_MINUTE)
						.isBefore(
										LocalDateTime.now(ZoneId.systemDefault())
						);
	}

	public boolean isReadRestriction(){
		return this.getCreatedDate()
						.plusDays(READ_LIMIT_DAYS)
						.isBefore(
										LocalDateTime.now(ZoneId.systemDefault())
						);
	}

	public boolean isCreator(Long userId){
		return this.userId.equals(userId);
	}

	public boolean isNotCreator(Long userId){
		return !this.isCreator(userId);
	}

	public boolean isDuplicationAcquire(Long userId){
		return this.getReceivers()
						.stream()
						.filter(distributionReceiver -> Objects.nonNull(distributionReceiver.getUserId()))
						.anyMatch(distributionReceiver -> distributionReceiver.getUserId().equals(userId));
	}

}
