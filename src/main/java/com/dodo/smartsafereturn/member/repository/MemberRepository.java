package com.dodo.smartsafereturn.member.repository;

import com.dodo.smartsafereturn.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 활성화 중인 회원 memberNumber 값으로 쿼리
    Optional<Member> findByMemberNumberAndIsDeletedIsFalse(Long memberNumber);
    // 활성화 중인 회원 id 값으로 쿼리
    @Query("select m from Member m where m.id =:id and m.isDeleted = false ")
    Optional<Member> findMemberByIdNotDeleted(@Param("id") String id);
}
