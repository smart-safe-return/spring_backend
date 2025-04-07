package com.dodo.smartsafereturn.member.repository;

import com.dodo.smartsafereturn.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

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

    List<Member> findAllByIsDeletedIsFalse();

    // 회원 아이디 + 휴대폰 -> 패스워드 검증 전 유효한 회원인지 확인
    @Query("select count(m) > 0 from Member m where m.id = :id and m.phone = :phone")
    boolean isMemberValid(@Param("id") String id, @Param("phone") String phone);

    // 회원 휴대폰 번호로 회원 존재하는지 확인
    boolean existsByPhoneAndIsDeletedIsFalse(String phone);

    // 회원 휴대폰 번호로 회원 정보 쿼리
    Optional<Member> findByPhoneAndIsDeletedIsFalse(String phone);
}
