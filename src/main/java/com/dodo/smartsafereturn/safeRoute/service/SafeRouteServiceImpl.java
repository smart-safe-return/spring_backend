package com.dodo.smartsafereturn.safeRoute.service;

import com.dodo.smartsafereturn.member.entity.Member;
import com.dodo.smartsafereturn.member.repository.MemberRepository;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteCreateDto;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteResponseDto;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteUpdateDto;
import com.dodo.smartsafereturn.safeRoute.entity.RouteState;
import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
import com.dodo.smartsafereturn.safeRoute.repository.SafeRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SafeRouteServiceImpl implements SafeRouteService {

    private final SafeRouteRepository safeRouteRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public SafeRouteResponseDto create(SafeRouteCreateDto dto) {

        Member member = memberRepository.findByMemberNumberAndIsDeletedIsFalse(dto.getMemberNumber())
                .orElseThrow(() -> new RuntimeException("[SafeRouteService] create() : 존재하지 않는 회원"));

        SafeRoute safeRoute = safeRouteRepository.save(
                SafeRoute.builder()
                        .startLocation(dto.getStartLocation())
                        .endLocation(dto.getEndLocation())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .member(member)
                        .build()
        );

        return SafeRouteResponseDto.builder()
                .safeRouteId(safeRoute.getId())
                .memberNumber(safeRoute.getMember().getMemberNumber())
                .startLocation(safeRoute.getStartLocation())
                .endLocation(safeRoute.getEndLocation())
                .startTime(safeRoute.getStartTime())
                .endTime(safeRoute.getEndTime())
                .isSuccess(safeRoute.getIsSuccess())
                .build();
    }

    @Transactional
    @Override
    public void update(SafeRouteUpdateDto dto) {

    }

    @Transactional
    @Override
    public void changeStatus(RouteState state, Long safeRouteId) {

    }

    @Transactional
    @Override
    public void delete(Long safeRouteId) {

    }

    @Override
    public SafeRouteResponseDto getSafeRoute(Long safeRouteId) {
        return null;
    }

    @Override
    public List<SafeRouteResponseDto> getMemberSafeRoutes(Long memberNumber) {
        return List.of();
    }
}
