package com.dodo.smartsafereturn.safeRoute.controller;

import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteCreateDto;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteResponseDto;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteUpdateDto;
import com.dodo.smartsafereturn.safeRoute.entity.RouteState;
import com.dodo.smartsafereturn.safeRoute.entity.SafeRoute;
import com.dodo.smartsafereturn.safeRoute.service.SafeRouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/safe-route")
@RequiredArgsConstructor
public class SafeRouteController {

    private final SafeRouteService safeRouteService;

    // 안전 귀가 루트 등록
    @PostMapping("")
    public ResponseEntity<SafeRouteResponseDto> create(@RequestBody SafeRouteCreateDto dto) {
         return ResponseEntity.ok(safeRouteService.create(dto));
    }

    // 안전 귀가 루트 수정 (도착지, 도착시간)
    @PutMapping("/{safeRouteId}")
    public ResponseEntity<?> update(@RequestBody SafeRouteUpdateDto dto) {
        safeRouteService.update(dto);
        return ResponseEntity.ok().build();
    }

    // 안전 귀가 루트 상태 변경 (사용자 도중 포기, 실패, 완료)
    @PutMapping("/{safeRouteId}/state")
    public ResponseEntity<?> update(@RequestBody RouteState state, @PathVariable Long safeRouteId) {
        safeRouteService.changeStatus(state, safeRouteId);
        return ResponseEntity.ok().build();
    }

    // 안전 귀가 루트 삭제
    @DeleteMapping("/{safeRouteId}")
    public ResponseEntity<?> delete(@PathVariable Long safeRouteId) {
        safeRouteService.delete(safeRouteId);
        return ResponseEntity.ok().build();
    }

    // 안전 귀가 루트 가져오기 (현재 한 건)
    @GetMapping("/{safeRouteId}")
    public ResponseEntity<SafeRouteResponseDto> getOne(@PathVariable Long safeRouteId) {
        return ResponseEntity.ok(safeRouteService.getSafeRoute(safeRouteId));
    }

    // 안전 귀가 루트 가져오기 (마이페이지 - 회원 귀가 루트 내역 List)
    @GetMapping("/member/{memberNumber}")
    public ResponseEntity<List<SafeRouteResponseDto>> getMemberRouteList(@PathVariable Long memberNumber) {
        return ResponseEntity.ok(safeRouteService.getMemberSafeRoutes(memberNumber));
    }
}
