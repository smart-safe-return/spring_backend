package com.dodo.smartsafereturn.answer.repository;

import com.dodo.smartsafereturn.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
