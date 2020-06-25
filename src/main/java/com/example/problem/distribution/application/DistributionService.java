package com.example.problem.distribution.application;

import org.springframework.stereotype.Service;

import com.example.problem.assets.http.RequestHeader;
import com.example.problem.assets.token.TokenGenerator;
import com.example.problem.distribution.application.request.DistributionSaveRequest;
import com.example.problem.distribution.domain.Distribution;
import com.example.problem.distribution.domain.DistributionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistributionService {

	private final DistributionRepository distributionRepository;

	private final TokenGenerator tokenGenerator;

	public String distributionSave(DistributionSaveRequest saveRequest, RequestHeader requestHeader){

		final Long money = saveRequest.getMoney();
		final Long people = saveRequest.getPeople();
		final Long userId = requestHeader.getXUserId();
		final String roomId = requestHeader.getXRoomId();

		final String token = tokenGenerator.randomToken();


		return token;
	}

}
