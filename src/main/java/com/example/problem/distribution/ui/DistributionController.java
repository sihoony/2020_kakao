package com.example.problem.distribution.ui;

import com.example.problem.assets.http.RequestHeader;
import com.example.problem.distribution.application.DistributionService;
import com.example.problem.distribution.application.request.DistributionAcquireRequest;
import com.example.problem.distribution.application.request.DistributionCreateRequest;
import com.example.problem.distribution.application.response.DistributionSaveResponse;
import com.example.problem.distribution.domain.Distribution;
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
	public ResponseEntity<DistributionSaveResponse> distributionSave(@RequestBody @Valid DistributionCreateRequest distributionCreateRequest,
	                                                                 RequestHeader requestHeader){

		Distribution result = distributionService.distributionCreate(distributionCreateRequest, requestHeader);
		return ResponseEntity.ok(new DistributionSaveResponse(result.getToken()));
	}

	@PutMapping(value = "/distribution/{token}")
	public ResponseEntity<DistributionSaveResponse> distributionSave(@RequestBody @Valid DistributionAcquireRequest distributionAcquireRequest,
																																	 RequestHeader requestHeader){

		Distribution result = distributionService.distributionCreate(distributionAcquireRequest, requestHeader);
		return ResponseEntity.ok(new DistributionSaveResponse(result.getToken()));
	}

}