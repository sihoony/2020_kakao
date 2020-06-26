package com.example.problem.distribution.application;

import com.example.problem.distribution.application.request.DistributionAcquireRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.problem.assets.http.RequestHeader;
import com.example.problem.assets.token.TokenGenerator;
import com.example.problem.distribution.application.request.DistributionCreateRequest;
import com.example.problem.distribution.domain.*;
import com.example.problem.distribution.exptions.DistributionCompleteException;
import com.example.problem.distribution.exptions.ExpiredRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistributionService {

	private final DistributionRepository distributionRepository;

	private final TokenGenerator tokenGenerator;

	@Transactional
	public Distribution distributionCreate(DistributionCreateRequest createRequest, RequestHeader requestHeader){

		final Long amount = createRequest.getAmount();
		final Long people = createRequest.getPeople();
		final Long userId = requestHeader.getXUserId();
		final String roomId = requestHeader.getXRoomId();

		final String token = tokenGenerator.randomToken();


		Distribution distribution = new Distribution();
		distribution.setAmount(amount);
		distribution.setPeople(people);
		distribution.setUserId(userId);
		distribution.setRoomId(roomId);
		distribution.setToken(token);

		distribution.distributionOperation();

		return distributionRepository.save(distribution);
	}


	@Transactional
	public DistributionReceiver distributionAcquire(DistributionAcquireRequest acquireRequest, RequestHeader requestHeader){

		final String token = acquireRequest.getToken();

		final Long userId = requestHeader.getXUserId();
		final String roomId = requestHeader.getXRoomId();

		Distribution distribution = distributionRepository.findByTokenAndRoomId(token, roomId);
		if(distribution.isExpireTime()){
			throw new ExpiredRequestException();
		}

		DistributionReceiver distributionReceiver = distribution.getReceivers()
			.stream()
			.filter(DistributionReceiver::isWait)
			.findFirst()
			.orElseThrow(DistributionCompleteException::new);

		distributionReceiver.setUserId(userId);
		distributionReceiver.setStatus(ReceiverStatus.COMPLETE);

		return distributionReceiver;
	}

}
