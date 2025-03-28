package com.dodo.smartsafereturn.member.repository;

import com.dodo.smartsafereturn.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 활성화 중인 회원 memberNumber 값으로 쿼리
    Optional<Member> findByMemberNumberAndIsDeletedIsFalse(Long memberNumber);

    // 활성화 중인 회원 id 값으로 쿼리
    @Query("select m from Member m where m.id =:id and m.isDeleted = false ")
    Optional<Member> findMemberByIdNotDeleted(@Param("id") String id);

    // 회원 가입 전, 회원이 존재하는 지 체크 (Id값)
    boolean existsByIdAndIsDeletedIsFalse(String id);

    // 회원 가입 전, 회원이 존재하는 지 체크 (PK값)
    boolean existsByMemberNumberAndIsDeletedIsFalse(Long memberNumber);

    // memberNumber (PK) 로 삭제
    void deleteByMemberNumber(Long memberNumber);

    List<Member> findAllByIsDeletedIsFalse();
}
