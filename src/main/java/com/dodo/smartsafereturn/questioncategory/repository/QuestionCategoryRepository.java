package com.dodo.smartsafereturn.questioncategory.repository;

import com.dodo.smartsafereturn.questioncategory.entity.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {

    Optional<QuestionCategory> findByCategoryAndIsDeletedIsFalse(String category);

    Optional<QuestionCategory> findByIdAndIsDeletedIsFalse(Long id);

    boolean existsByCategoryAndIsDeletedIsFalse(String category);

    List<QuestionCategory> findAllByIsDeletedIsFalse();
}
