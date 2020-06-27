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
	private static final int DISTRIBUTION_MINIMUM = 1;

	private static final Random RANDOM = new Random();

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

	public void distributionOperation(){

		if(this.amount.equals(this.people)){

			receivers.addAll(
							LongStream.range(0, this.people)
											.mapToObj(operand -> new DistributionReceiver(this.amount/this.people))
											.collect(Collectors.toList())
			);

			return;
		}

		long totalAmount = this.amount;

		Long[] distributionMoney = new Long[this.people.intValue()];
		for (int i = 0; i < this.people - 1; i++) {
			long distributionRandomMoney = RANDOM.nextInt((int) totalAmount - (this.people.intValue() - i)) + DISTRIBUTION_MINIMUM;

			totalAmount -= distributionRandomMoney;
			distributionMoney[i] = distributionRandomMoney;
		}

		distributionMoney[distributionMoney.length - 1] = totalAmount;

		receivers.addAll(
						Stream.of(distributionMoney)
						.map(DistributionReceiver::new)
						.collect(Collectors.toList())
		);
	}

	public boolean isExpireTime(){
		System.out.println(this.getCreatedDate());
		System.out.println(this.getCreatedDate()
						.plusMinutes(EXPTIRE_MINUS_MINUTE));
		System.out.println(LocalDateTime.now(ZoneId.systemDefault()));

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
