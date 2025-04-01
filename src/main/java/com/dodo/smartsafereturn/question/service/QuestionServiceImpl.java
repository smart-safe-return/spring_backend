package com.dodo.smartsafereturn.question.service;

import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.member.repository.MemberRepository;
import com.dodo.smartsafereturn.question.dto.*;
import com.dodo.smartsafereturn.question.entity.Question;
import com.dodo.smartsafereturn.question.entity.QuestionStatus;
import com.dodo.smartsafereturn.question.repository.QuestionRepository;
import com.dodo.smartsafereturn.questioncategory.entity.QuestionCategory;
import com.dodo.smartsafereturn.questioncategory.repository.QuestionCategoryRepository;
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
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final QuestionCategoryRepository questionCategoryRepository;

    @Transactional
    @Override
    public Long create(QuestionCreateDto createDto) {

        // member
        Member member = memberRepository.findByMemberNumberAndIsDeletedIsFalse(createDto.getMemberNumber())
                .orElseThrow(() -> new RuntimeException("[QuestionService] create: 존재 하지 않는 회원"));
        // category
        QuestionCategory category = questionCategoryRepository.findByCategoryAndIsDeletedIsFalse(createDto.getCategory())
                .orElseThrow(() -> new RuntimeException("[QuestionService] create: 존재 하지 않는 카테고리 "));

        // 등록
        Question saved = questionRepository.save(
                Question.builder()
                        .title(createDto.getTitle())
                        .content(createDto.getContent())
                        .member(member)
                        .questionCategory(category)
                        .build()
        );

        return saved.getId();
    }

    @Transactional
    @Override
    public void update(QuestionUpdateDto updateDto) {
        // 실존하는 지 체크
        Question question = questionRepository.findByIdAndIsDeletedIsFalse(updateDto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("[QuestionService] update: 존재하지 않는 문의 글"));

        // 바꿀 카테고리의 아이디 가져오기
        QuestionCategory category = questionCategoryRepository.findByCategoryAndIsDeletedIsFalse(updateDto.getCategory())
                .orElseThrow(() -> new RuntimeException("[QuestionService] update: 존재 하지 않는 카테고리 "));


        question.update(updateDto.getTitle(), updateDto.getContent(), category);

    }

    @Transactional
    @Override
    public void delete(Long questionId) {
        // 실존하는 지 체크
        Question question = questionRepository.findByIdAndIsDeletedIsFalse(questionId)
                .orElseThrow(() -> new RuntimeException("[QuestionService] delete: 존재하지 않는 문의 글"));

        // 삭제여부가 false 라면 삭제
        if (!question.getIsDeleted()) {
            question.changeDeleteFlag();
        }
    }

    @Override
    public QuestionResponseDto getOne(Long questionId) {
        return questionRepository.getQuestionById(questionId);
    }

    @Override
    public List<QuestionResponseListDto> getList() {
        List<Question> list = questionRepository.findAll();
        return list
                .stream()
                .map(q -> QuestionResponseListDto
                        .builder()
                        .questionId(q.getId())
                        .memberId(q.getMember().getId())
                        .category(q.getQuestionCategory().getCategory())
                        .title(q.getTitle())
                        .createdDate(q.getCreatedDate())
                        .modifiedDate(q.getModifiedDate())
                        .build()
                )
                .toList();
    }

    @Override
    public Page<QuestionResponseListDto> getListByCondition(QuestionSearchCondition condition, Pageable pageable) {
        return questionRepository.searchQuestionByCondition(condition, pageable);
    }

    @Transactional
    @Override
    public void updateStatus(Long questionId, QuestionStatus status) {
        // 실존하는 지 체크
        Question question = questionRepository.findByIdAndIsDeletedIsFalse(questionId)
                .orElseThrow(() -> new RuntimeException("[QuestionService] updateStatus: 존재하지 않는 문의 글"));

        question.updateStatus(status);
    }

    @Override
    public Question findById(Long questionId) {
        return questionRepository.findByIdAndIsDeletedIsFalse(questionId)
                .orElseThrow(() -> new RuntimeException("[QuestionService] updateStatus: 존재하지 않는 문의 글"));
    }
}
