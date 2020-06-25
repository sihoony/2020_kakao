package com.example.problem.distribution.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.*;

@Data
@Embeddable
public class ExpireTime {

	private static final int EXPTIRE_MINUS_MINUTE = 10;

	@Column
	private LocalDateTime expireTime;

	public boolean isExpireTime(){

		return this.expireTime.isAfter(LocalDateTime
			.now(ZoneId.systemDefault())
		);
	}
}
