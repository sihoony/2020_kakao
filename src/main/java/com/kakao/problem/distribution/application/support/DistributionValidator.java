package com.kakao.problem.distribution.application.support;

import com.kakao.problem.distribution.domain.Distribution;
import com.kakao.problem.distribution.exptions.AcquireDeniedException;
import com.kakao.problem.distribution.exptions.DuplicationAcquireException;
import com.kakao.problem.distribution.exptions.ExpiredRequestException;
import com.kakao.problem.distribution.exptions.NotCreatorException;
import com.kakao.problem.distribution.exptions.NotFoundDistributionException;
import com.kakao.problem.distribution.exptions.ReadRestrictionException;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class DistributionValidator {

  public void createValid( final Distribution distribution, final Long userId ){

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
  }

  public void findValid( final Distribution distribution, final Long userId ){

    if(Objects.isNull(distribution)){
      throw new NotFoundDistributionException();
    }

    if(distribution.isNotCreator(userId)){
      throw new NotCreatorException();
    }

    if(distribution.isReadRestriction()){
      throw new ReadRestrictionException();
    }
  }
}
