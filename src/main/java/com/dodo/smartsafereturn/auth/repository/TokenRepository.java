package com.dodo.smartsafereturn.auth.repository;

import com.dodo.smartsafereturn.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Boolean existsByToken(String token);
    void deleteByToken(String token);
}
