package com.kakao.problem.distribution.application.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class DistributionFindRequest implements Serializable {

	private static final long serialVersionUID = -1794320694147288699L;

	@NotBlank
	@Length(min = 3, max = 3)
	private String token;

}
