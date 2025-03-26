package com.dodo.smartsafereturn.member.repository;

import com.dodo.smartsafereturn.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 활성화 중인 회원 memberNumber 값으로 쿼리
    Optional<Member> findByMemberNumberAndIsDeletedIsFalse(Long memberNumber);
    // 활성화 중인 회원 id 값으로 쿼리
    Optional<Member> findByIdAndIsDeletedIsFalse(String id);
}
