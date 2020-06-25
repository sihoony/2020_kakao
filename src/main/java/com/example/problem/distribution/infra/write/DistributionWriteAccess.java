package com.example.problem.distribution.infra.write;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.problem.distribution.domain.Distribution;

public interface DistributionWriteAccess extends JpaRepository<Distribution, Long> {
}
