package com.dodo.smartsafereturn.emergencycontact.repository;

import com.dodo.smartsafereturn.emergencycontact.entity.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {

    boolean existsByMember_memberNumberAndPhone(Long memberNumber, String phone);

    @Query("select ec from EmergencyContact ec where ec.member.memberNumber = :memberNumber and ec.member.isDeleted = false ")
    List<EmergencyContact> findMemberContacts(@Param("memberNumber") Long memberNumber);
}
