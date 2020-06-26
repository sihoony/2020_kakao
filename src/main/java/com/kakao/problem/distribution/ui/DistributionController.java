package com.kakao.problem.distribution.ui;

import com.kakao.problem.assets.entrypoints.RequestHeader;
import com.kakao.problem.assets.entrypoints.RestApiResponse;
import com.kakao.problem.distribution.application.DistributionService;
import com.kakao.problem.distribution.application.request.DistributionAcquireRequest;
import com.kakao.problem.distribution.application.request.DistributionCreateRequest;
import com.kakao.problem.distribution.application.request.DistributionFindRequest;
import com.kakao.problem.distribution.application.response.DistributionAcquireResponse;
import com.kakao.problem.distribution.application.response.DistributionCreateResponse;
import com.kakao.problem.distribution.application.response.DistributionFindResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class DistributionController {

	private final DistributionService distributionService;

	@PostMapping(value = "/distribution")
	public RestApiResponse<DistributionCreateResponse> distributionCreate(@RequestBody @Valid DistributionCreateRequest distributionCreateRequest,
																																			RequestHeader requestHeader){

		DistributionCreateResponse result = distributionService.distributionCreate(distributionCreateRequest, requestHeader);
		return RestApiResponse.ok(result);
	}

	@PutMapping(value = "/distribution/{token}")
	public RestApiResponse<DistributionAcquireResponse> distributionAcquire(@Valid DistributionAcquireRequest distributionAcquireRequest,
																																			RequestHeader requestHeader){

		DistributionAcquireResponse result = distributionService.distributionAcquire(distributionAcquireRequest, requestHeader);
		return RestApiResponse.ok(result);
	}

	@GetMapping(value = "/distribution/{token}")
	public RestApiResponse<DistributionFindResponse> distributionFind(@Valid DistributionFindRequest distributionFindRequest,
																																		RequestHeader requestHeader){

		DistributionFindResponse result = distributionService.distributionFind(distributionFindRequest, requestHeader);
		return RestApiResponse.ok(result);
	}

}