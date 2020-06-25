package com.example.problem.distribution.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.*;

@Data
@Entity
@Table
public class Distribution {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long regMemberNumber;

	@Column(nullable = false)
	private Long money;

	@Column(nullable = false)
	private String token;

	@Column(nullable = false)
	private String roomId;

	@Embedded
	@Column(nullable = false)
	private ExpireTime expireTime;

	@LastModifiedDate
	private LocalDateTime updDate;

	@CreatedDate
	private LocalDateTime regDate;



}
