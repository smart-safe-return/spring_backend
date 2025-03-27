package com.dodo.smartsafereturn.auth.repository;

import com.dodo.smartsafereturn.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByRefresh(String refresh);
    void deleteByRefresh(String refresh);
}
