package com.dodo.smartsafereturn.answer.service;

import com.dodo.smartsafereturn.admin.dto.AdminResponseDto;
import com.dodo.smartsafereturn.admin.entity.Admin;
import com.dodo.smartsafereturn.admin.service.AdminService;
import com.dodo.smartsafereturn.answer.dto.AnswerCreateDto;
import com.dodo.smartsafereturn.answer.dto.AnswerResponseDto;
import com.dodo.smartsafereturn.answer.dto.AnswerSearchCondition;
import com.dodo.smartsafereturn.answer.dto.AnswerUpdateDto;
import com.dodo.smartsafereturn.answer.entity.Answer;
import com.dodo.smartsafereturn.answer.repository.AnswerRepository;
import com.dodo.smartsafereturn.question.entity.Question;
import com.dodo.smartsafereturn.question.entity.QuestionStatus;
import com.dodo.smartsafereturn.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final AdminService adminService;

    // 문의글에 상태 변경
    @Transactional
    @Override
    public Long create(AnswerCreateDto createDto) {

        // 답변할 문의글
        Question question = questionService.findById(createDto.getQuestionId());
        // 답변할 관리자
        Admin admin = adminService.findById(createDto.getAdminNumber());

        Answer savedAnswer = answerRepository.save(
                Answer.builder()
                        .title(createDto.getTitle())
                        .content(createDto.getContent())
                        .question(question)
                        .admin(admin)
                        .build()
        );
        
        // 답변 글 성공적 저장 시, 완료로 문의 글 변경
        questionService.updateStatus(createDto.getQuestionId(), QuestionStatus.COMPLETED);

        // 양방향 연관관계 메서드
        savedAnswer.addQuestion(question);

        return savedAnswer.getId();
    }

    @Transactional
    @Override
    public void update(AnswerUpdateDto updateDto) {
        Answer answer = answerRepository.findByIdAndIsDeletedIsFalse(updateDto.getAnswerId())
                .orElseThrow(() -> new RuntimeException("[AnswerService] 존재 하지 않는 답변 글"));

        answer.update(updateDto.getTitle(), updateDto.getContent());
    }
    
    @Transactional
    @Override
    public void delete(Long answerId) {
        Answer answer = answerRepository.findByIdAndIsDeletedIsFalse(answerId)
                .orElseThrow(() -> new RuntimeException("[AnswerService] 존재 하지 않는 답변 글"));

        answer.changeIsDeleted();
    }

    @Override
    public AnswerResponseDto getOne(Long answerId) {
        Answer answer = answerRepository.findByIdAndIsDeletedIsFalse(answerId)
                .orElseThrow(() -> new RuntimeException("[AnswerService] 존재 하지 않는 답변 글"));

        return AnswerResponseDto.builder()
                .answerId(answer.getId())
                .adminId(answer.getAdmin().getId())
                .questionId(answer.getQuestion().getId())
                .questionTitle(answer.getQuestion().getTitle())
                .title(answer.getTitle())
                .content(answer.getContent())
                .isDeleted(answer.getIsDeleted())
                .createDate(answer.getCreatedDate())
                .modifiedDate(answer.getModifiedDate())
                .build();
    }

    @Override
    public Page<AnswerResponseDto> getPageByCondition(AnswerSearchCondition condition, Pageable pageable) {
        return answerRepository.getPageByCondition(condition, pageable);
    }

    @Override
    public List<AnswerResponseDto> getListByCondition(AnswerSearchCondition condition) {
        return answerRepository.getListByCondition(condition);
    }
}
