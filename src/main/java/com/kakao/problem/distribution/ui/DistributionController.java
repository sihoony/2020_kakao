package com.kakao.problem.distribution.ui;

import com.kakao.problem.assets.entrypoints.RequestHeader;
import com.kakao.problem.assets.entrypoints.RestApiResponse;
import com.kakao.problem.distribution.application.DistributionService;
import com.kakao.problem.distribution.application.request.DistributionAcquireRequest;
import com.kakao.problem.distribution.application.request.DistributionCreateRequest;
import com.kakao.problem.distribution.application.response.DistributionAcquireResponse;
import com.kakao.problem.distribution.application.response.DistributionCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class DistributionController {

	private final DistributionService distributionService;

	@PostMapping(value = "/distribution")
	public RestApiResponse<DistributionCreateResponse> distributionSave(@RequestBody @Valid DistributionCreateRequest distributionCreateRequest,
																																			RequestHeader requestHeader){

		DistributionCreateResponse result = distributionService.distributionCreate(distributionCreateRequest, requestHeader);
		return RestApiResponse.ok(result);
	}

	@PutMapping(value = "/distribution/{token}")
	public RestApiResponse<DistributionAcquireResponse> distributionSave(@Valid DistributionAcquireRequest distributionAcquireRequest,
																																			RequestHeader requestHeader){

		DistributionAcquireResponse result = distributionService.distributionAcquire(distributionAcquireRequest, requestHeader);
		return RestApiResponse.ok(result);
	}

}