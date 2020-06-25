package com.example.problem.distribution.ui;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.problem.assets.http.RequestHeader;
import com.example.problem.distribution.application.request.DistributionSaveRequest;
import com.example.problem.distribution.application.response.DistributionSaveResponse;

@RestController
public class DistributionController {


	@PostMapping(value = "/distribution")
	public ResponseEntity<DistributionSaveResponse> distributionSave(@RequestBody @Valid DistributionSaveRequest distributionSaveRequest,
	                                                                 RequestHeader requestHeader){

		return ResponseEntity.ok(new DistributionSaveResponse(""));
	}

}
