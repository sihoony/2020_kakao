package com.kakao.problem.distribution.application;

import com.kakao.problem.assets.entrypoints.RequestHeader;
import com.kakao.problem.assets.token.TokenGenerator;
import com.kakao.problem.distribution.application.request.DistributionAcquireRequest;
import com.kakao.problem.distribution.application.request.DistributionCreateRequest;
import com.kakao.problem.distribution.application.request.DistributionFindRequest;
import com.kakao.problem.distribution.application.response.DistributionAcquireResponse;
import com.kakao.problem.distribution.application.response.DistributionCreateResponse;
import com.kakao.problem.distribution.application.response.DistributionFindResponse;
import com.kakao.problem.distribution.application.support.DistributionConvert;
import com.kakao.problem.distribution.application.support.DistributionValidator;
import com.kakao.problem.distribution.domain.Distribution;
import com.kakao.problem.distribution.domain.DistributionReceiver;
import com.kakao.problem.distribution.domain.DistributionRepository;
import com.kakao.problem.distribution.domain.DomainService;
import com.kakao.problem.distribution.domain.ReceiverStatus;
import com.kakao.problem.distribution.exptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistributionService {

	private final DistributionRepository distributionRepository;
	private final DomainService domainService;

	private final TokenGenerator tokenGenerator;

	private final DistributionConvert distributionConvert;
	private final DistributionValidator validator;

	@Transactional
	public DistributionCreateResponse distributionCreate(DistributionCreateRequest createRequest, RequestHeader requestHeader){

		Distribution distribution = distributionConvert.to(createRequest, tokenGenerator.randomToken(), requestHeader);

		domainService.randomPrices(distribution);
		distributionRepository.save(distribution);

		return new DistributionCreateResponse(distribution.getToken());
	}

	@Transactional
	public DistributionAcquireResponse distributionAcquire( final DistributionAcquireRequest acquireRequest, final RequestHeader requestHeader){

		final Long userId = requestHeader.getXUserId();

		Distribution distribution = distributionRepository.findByTokenAndRoomId(acquireRequest.getToken(), requestHeader.getXRoomId());
		validator.createValid(distribution, userId);

		DistributionReceiver distributionReceiver = distribution.getReceivers()
			.stream()
			.filter(DistributionReceiver::isWait)
			.findFirst()
			.orElseThrow(DistributionCompleteException::new);

		distributionReceiver.setUserId(userId);
		distributionReceiver.setStatus(ReceiverStatus.COMPLETE);

		return new DistributionAcquireResponse(distributionReceiver.getAmount());
	}

	@Transactional(readOnly = true)
	public DistributionFindResponse distributionFind(final DistributionFindRequest findRequest, final RequestHeader requestHeader){

		Distribution distribution = distributionRepository.findByTokenAndRoomId(findRequest.getToken(), requestHeader.getXRoomId());
		validator.findValid(distribution, requestHeader.getXUserId());

		return new DistributionFindResponse(
				distribution.getReceivers()
						.stream()
						.filter(DistributionReceiver::isComplete)
						.collect(Collectors.toList()),
				distribution.getAmount(),
				distribution.getCreatedDate()
		);
	}
}
