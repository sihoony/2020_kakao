package com.example.problem.distribution.ui;

import com.example.problem.assets.http.RequestHeader;
import com.example.problem.distribution.application.DistributionService;
import com.example.problem.distribution.application.request.DistributionAcquireRequest;
import com.example.problem.distribution.application.request.DistributionCreateRequest;
import com.example.problem.distribution.application.response.DistributionAcquireResponse;
import com.example.problem.distribution.application.response.DistributionCreateResponse;
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
	public ResponseEntity<DistributionCreateResponse> distributionSave(@RequestBody @Valid DistributionCreateRequest distributionCreateRequest,
																																		 RequestHeader requestHeader){

		DistributionCreateResponse result = distributionService.distributionCreate(distributionCreateRequest, requestHeader);
		return ResponseEntity.ok(result);
	}

	@PutMapping(value = "/distribution/{token}")
	public ResponseEntity<DistributionAcquireResponse> distributionSave(@Valid DistributionAcquireRequest distributionAcquireRequest,
	                                                                    RequestHeader requestHeader){

		DistributionAcquireResponse result = distributionService.distributionAcquire(distributionAcquireRequest, requestHeader);
		return ResponseEntity.ok(result);
	}

}