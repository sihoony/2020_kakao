package com.example.problem.distribution.application;

import com.example.problem.assets.http.RequestHeader;
import com.example.problem.assets.token.TokenGenerator;
import com.example.problem.distribution.application.request.DistributionAcquireRequest;
import com.example.problem.distribution.application.request.DistributionCreateRequest;
import com.example.problem.distribution.application.response.DistributionAcquireResponse;
import com.example.problem.distribution.application.response.DistributionCreateResponse;
import com.example.problem.distribution.domain.Distribution;
import com.example.problem.distribution.domain.DistributionReceiver;
import com.example.problem.distribution.domain.DistributionRepository;
import com.example.problem.distribution.domain.ReceiverStatus;
import com.example.problem.distribution.exptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DistributionService {

	private final DistributionRepository distributionRepository;

	private final TokenGenerator tokenGenerator;

	@Transactional
	public DistributionCreateResponse distributionCreate(DistributionCreateRequest createRequest, RequestHeader requestHeader){

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

		distributionRepository.save(distribution);
		return new DistributionCreateResponse(token);
	}

	@Transactional
	public DistributionAcquireResponse distributionAcquire(DistributionAcquireRequest acquireRequest, RequestHeader requestHeader){

		final String token = acquireRequest.getToken();

		final Long userId = requestHeader.getXUserId();
		final String roomId = requestHeader.getXRoomId();

		Distribution distribution = distributionRepository.findByTokenAndRoomId(token, roomId);
		if(Objects.isNull(distribution)){
			throw new NotFoundDistributionException();
		}

		if(distribution.isExpireTime()){
			throw new ExpiredRequestException();
		}

		if(distribution.isCreator(userId)){
			throw new AcquireDeniedException();
		}

		if(distribution.isDuplicationAcquire(userId)){
			throw new DuplicationAcquireException();
		}

		DistributionReceiver distributionReceiver = distribution.getReceivers()
			.stream()
			.filter(DistributionReceiver::isWait)
			.findFirst()
			.orElseThrow(DistributionCompleteException::new);

		distributionReceiver.setUserId(userId);
		distributionReceiver.setStatus(ReceiverStatus.COMPLETE);

		return new DistributionAcquireResponse(distributionReceiver.getAmount());
	}
}
