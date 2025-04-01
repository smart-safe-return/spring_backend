package com.dodo.smartsafereturn.answer.repository;

import com.dodo.smartsafereturn.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long>, CustomAnswerRepository {
    Optional<Answer> findByIdAndIsDeletedIsFalse(Long id);
}
