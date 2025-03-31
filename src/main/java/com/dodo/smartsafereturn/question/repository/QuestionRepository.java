package com.dodo.smartsafereturn.question.repository;

import com.dodo.smartsafereturn.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long>, CustomQuestionRepository {

    Optional<Question> findByIdAndIsDeletedIsFalse(Long questionId);
}
