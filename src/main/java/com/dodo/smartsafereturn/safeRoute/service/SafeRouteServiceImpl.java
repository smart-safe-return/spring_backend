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

    /**
     * todo 2025-03-28 service 구현 / Postman 테스트 필요
     */
    
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
                        .routePath(dto.getRoutePath()) // todo 추후 받아서 넣기
                        .build()
        );

        return ofDto(safeRoute);
    }

    @Transactional
    @Override
    public void update(SafeRouteUpdateDto dto) {
        SafeRoute safeRoute = safeRouteRepository.findById(dto.getSafeRouteId())
                .orElseThrow(() -> new RuntimeException("[SafeRouteService] update() : 존재하지 않는 안전 귀가 경로"));

        safeRoute.update(dto);
    }

    @Transactional
    @Override
    public void changeStatus(RouteState state, Long safeRouteId) {
        SafeRoute safeRoute = safeRouteRepository.findById(safeRouteId)
                .orElseThrow(() -> new RuntimeException("[SafeRouteService] changeStatus() : 존재하지 않는 안전 귀가 경로"));

        safeRoute.changeIsSuccess(state);
    }

    @Transactional
    @Override
    public void delete(Long safeRouteId) {
        // 존재하는 검증
        SafeRoute safeRoute = safeRouteRepository.findById(safeRouteId)
                .orElseThrow(() -> new RuntimeException("[SafeRouteService] delete() : 존재하지 않는 안전 귀가 경로"));
        // 실제 삭제
        safeRouteRepository.deleteById(safeRouteId);
    }

    @Override
    public SafeRouteResponseDto getSafeRoute(Long safeRouteId) {
        SafeRoute safeRoute = safeRouteRepository.findById(safeRouteId)
                .orElseThrow(() -> new RuntimeException("[SafeRouteService] getSafeRoute() : 존재하지 않는 안전 귀가 경로"));

        return ofDto(safeRoute);
    }

    @Override
    public List<SafeRouteResponseDto> getMemberSafeRoutes(Long memberNumber) {

        return safeRouteRepository.findRoutesByMemberNumber(memberNumber)
                .stream()
                .map(SafeRouteServiceImpl::ofDto)
                .toList();
    }

    private static SafeRouteResponseDto ofDto(SafeRoute safeRoute) {
        return SafeRouteResponseDto.builder()
                .safeRouteId(safeRoute.getId())
                .memberNumber(safeRoute.getMember().getMemberNumber())
                .startLocation(safeRoute.getStartLocation())
                .endLocation(safeRoute.getEndLocation())
                .startTime(safeRoute.getStartTime())
                .endTime(safeRoute.getEndTime())
                .isSuccess(safeRoute.getIsSuccess())
                .routePath(safeRoute.getRoutePath())
                .build();
    }
}
