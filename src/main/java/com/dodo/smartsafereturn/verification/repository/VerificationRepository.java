package com.dodo.smartsafereturn.verification.repository;

import com.dodo.smartsafereturn.verification.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Optional<Verification> findByIdAndVerifiedIsFalse(Long id);
}
