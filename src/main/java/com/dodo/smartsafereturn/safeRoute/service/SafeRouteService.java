package com.dodo.smartsafereturn.safeRoute.service;

import com.dodo.smartsafereturn.safeRoute.dto.LatLngPoint;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteCreateDto;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteResponseDto;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteUpdateDto;
import com.dodo.smartsafereturn.safeRoute.entity.RouteState;
import org.locationtech.jts.geom.LineString;

import java.util.List;

public interface SafeRouteService {

    // 안전 귀가 루트 등록
    SafeRouteResponseDto create(SafeRouteCreateDto dto);
    // 안전 귀가 루트 수정 (도착지, 도착시간)
    void update(SafeRouteUpdateDto dto);
    // 안전 귀가 루트 상태 변경 (사용자 도중 포기, 실패, 완료)
    void changeStatus(RouteState state, Long safeRouteId);
    // 안전 귀가 루트 삭제
    void delete(Long safeRouteId);
    // 안전 귀가 루트 가져오기 (현재 한 건)
    SafeRouteResponseDto getSafeRoute(Long safeRouteId);
    // 안전 귀가 루트 가져오기 (마이페이지 - 회원 귀가 루트 내역 List)
    List<SafeRouteResponseDto> getMemberSafeRoutes(Long memberNumber);
    // json 위치 경도 배열 -> LineString 화
    LineString generateLineString(List<LatLngPoint> points);
}
