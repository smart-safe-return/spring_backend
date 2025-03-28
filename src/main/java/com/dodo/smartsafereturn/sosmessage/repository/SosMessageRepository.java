package com.dodo.smartsafereturn.sosmessage.repository;

import com.dodo.smartsafereturn.sosmessage.entity.SosMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SosMessageRepository extends JpaRepository<SosMessage, Long> {

    Optional<SosMessage> findByIdAndIsDeletedIsFalse(Long id);

    boolean existsByMember_memberNumberAndIsDeletedIsFalse(Long memberNumber);

    @Query("select sm from SosMessage sm where sm.member.memberNumber = :memberNumber and sm.isDeleted = false ")
    Optional<SosMessage> findMemberSosMessage(@Param("memberNumber") Long memberNumber);

    Optional<SosMessage> findByMember_memberNumber(Long memberMemberNumber);
}
