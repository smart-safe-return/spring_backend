package com.dodo.smartsafereturn.safeRoute.repository;

import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SafeRouteRepository extends JpaRepository<SafeRoute, Long> {

    // 한 회원의 안전 귀가 루트 사용 내역 list 뽑아오기
    @Query("select sr from SafeRoute sr where sr.member.memberNumber = :memberNumber and sr.member.isDeleted = false ")
    List<SafeRoute> findRoutesByMemberNumber(@Param("memberNumber") Long memberNumber);
}
