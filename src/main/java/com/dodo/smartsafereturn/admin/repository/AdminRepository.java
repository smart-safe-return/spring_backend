package com.dodo.smartsafereturn.admin.repository;

import com.dodo.smartsafereturn.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByIdAndIsDeletedIsFalse(String id);

    Optional<Admin> findByAdminNumberAndIsDeletedIsFalse(Long adminNumber);

    boolean existsByIdAndIsDeletedIsFalse(String id);

    @Query("select ad from Admin ad where ad.isDeleted = false ")
    List<Admin> getList();
}
