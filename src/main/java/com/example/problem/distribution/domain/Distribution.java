package com.example.problem.distribution.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.example.problem.assets.entity.BaseTimeEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.*;

@Data
@Entity
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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "distribution_id")
	private List<DistributionReceiver> receivers = new ArrayList<>();

	public void distributionOperation(){

		receivers.add(new DistributionReceiver(30L));
		receivers.add(new DistributionReceiver(10L));
		receivers.add(new DistributionReceiver(50L));
	}

	public boolean isExpireTime(){
		return this.getCreatedDate()
						.plusMinutes(EXPTIRE_MINUS_MINUTE)
						.isAfter(
										LocalDateTime.now(ZoneId.systemDefault())
						);
	}

	public boolean isReadRestriction(){
		return this.getCreatedDate()
						.plusDays(READ_LIMIT_DAYS)
						.isAfter(
										LocalDateTime.now(ZoneId.systemDefault())
						);
	}
}
