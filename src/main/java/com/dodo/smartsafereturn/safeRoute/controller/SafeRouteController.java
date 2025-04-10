package com.dodo.smartsafereturn.safeRoute.controller;

import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteChangeStateDto;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteCreateDto;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteResponseDto;
import com.dodo.smartsafereturn.safeRoute.dto.SafeRouteUpdateDto;
import com.dodo.smartsafereturn.safeRoute.service.SafeRouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/safe-route")
@RequiredArgsConstructor
@Tag(name = "안전 귀가 경로 관리 API", description = "안전 귀가 경로 CRUD 및 상태 관리 API")
public class SafeRouteController {

    private final SafeRouteService safeRouteService;

    // 안전 귀가 루트 등록
    @Operation(
            summary = "안전 귀가 경로 등록",
            description = "새로운 안전 귀가 경로를 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "등록할 안전 귀가 경로 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SafeRouteCreateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "안전 귀가 경로 등록 요청 예시",
                                            summary = "안전 귀가 경로 등록에 필요한 데이터",
                                            value = """
                                                    {
                                                              "member_number": 1,
                                                              "start_location": "서울시 강남구 역삼동 123-45",
                                                              "end_location": "서울시 서초구 방배동 789-10",
                                                              "start_time": "2025-04-10T10:23:15.789Z",
                                                              "end_time": "2025-04-10T10:43:15.789Z",
                                                              "route_path": [
                                                                  { "lat": 37.48041727265124, "lng": 126.87951498295432 },
                                                                  { "lat": 37.48050994547239, "lng": 126.87886819594975 },
                                                                  { "lat": 37.48050994547239, "lng": 126.87886819594975 },
                                                                  { "lat": 37.48053133149171, "lng": 126.87885921279691 },
                                                                  { "lat": 37.480787963246215, "lng": 126.87873344865714 },
                                                                  { "lat": 37.481265581109305, "lng": 126.8785268361418 },
                                                                  { "lat": 37.48281960019318, "lng": 126.87785309967872 },
                                                                  { "lat": 37.482883756249635, "lng": 126.87781716706736 },
                                                                  { "lat": 37.48291939847941, "lng": 126.87786208283156 },
                                                                  { "lat": 37.482926526923315, "lng": 126.87788004913725 },
                                                                  { "lat": 37.482926526923315, "lng": 126.87788004913725 },
                                                                  { "lat": 37.48307622408842, "lng": 126.87780818391451 },
                                                                  { "lat": 37.48307622408842, "lng": 126.87780818391451 },
                                                                  { "lat": 37.48308335251737, "lng": 126.87778123445598 },
                                                                  { "lat": 37.48308335251737, "lng": 126.87777225130314 },
                                                                  { "lat": 37.48311186622637, "lng": 126.87771835238608 },
                                                                  { "lat": 37.483118994651925, "lng": 126.87771835238608 },
                                                                  { "lat": 37.483610854372515, "lng": 126.87752970617642 },
                                                                  { "lat": 37.483682138121345, "lng": 126.87749377356506 },
                                                                  { "lat": 37.48406706918979, "lng": 126.87733207681391 },
                                                                  { "lat": 37.48449476805095, "lng": 126.87715241375709 },
                                                                  { "lat": 37.484544666091935, "lng": 126.87713444745141 },
                                                                  { "lat": 37.48469436001493, "lng": 126.87706258222869 },
                                                                  { "lat": 37.48473712965212, "lng": 126.87703563277016 },
                                                                  { "lat": 37.48509354234322, "lng": 126.87688291917186 },
                                                                  { "lat": 37.48548559433956, "lng": 126.87672122242071 },
                                                                  { "lat": 37.48552836352379, "lng": 126.87670325611505 },
                                                                  { "lat": 37.48552836352379, "lng": 126.87670325611505 },
                                                                  { "lat": 37.4855426199131, "lng": 126.87669427296221 },
                                                                  { "lat": 37.48559251725423, "lng": 126.87673020557357 },
                                                                  { "lat": 37.48560677363129, "lng": 126.87678410449062 },
                                                                  { "lat": 37.48560677363129, "lng": 126.87678410449062 },
                                                                  { "lat": 37.48587051611619, "lng": 126.87666732350368 },
                                                                  { "lat": 37.48587051611619, "lng": 126.87666732350368 },
                                                                  { "lat": 37.48587764427854, "lng": 126.87669427296221 },
                                                                  { "lat": 37.48587764427854, "lng": 126.87672122242071 },
                                                                  { "lat": 37.485998822934334, "lng": 126.87705359907585 },
                                                                  { "lat": 37.485998822934334, "lng": 126.87705359907585 },
                                                                  { "lat": 37.486005951084444, "lng": 126.87707156538153 }
                                                                ]
                                                            }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "안전 귀가 경로 등록 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SafeRouteResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "등록 성공 응답 예시",
                                                    summary = "등록된 안전 귀가 경로 정보",
                                                    value = """
                                                            {
                                                              "safe_route_id": 1,
                                                              "member_number": 1,
                                                              "start_location": "서울시 강남구 역삼동 123-45",
                                                              "end_location": "서울시 서초구 방배동 789-10",
                                                              "start_time": "2025-04-03T16:20:15.789Z",
                                                              "end_time": "2025-04-03T17:20:15.789Z",
                                                              "is_success": "STARTED",
                                                              "route_path": ["geometry(lineString, 4326)"]
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("")
    public ResponseEntity<SafeRouteResponseDto> create(@Validated @RequestBody SafeRouteCreateDto dto) {

        log.info("dto = {}", dto.toString());
        log.info("dto = {}", dto.getRoutePath());

        return ResponseEntity.ok(safeRouteService.create(dto));
    }

    // 안전 귀가 루트 수정 (도착지, 도착시간)
    @Operation(
            summary = "안전 귀가 경로 수정",
            description = "기존 안전 귀가 경로의 도착지 및 도착 시간을 수정합니다.",
            parameters = {
                    @Parameter(name = "safeRouteId", description = "수정할 안전 귀가 경로 ID", required = true, example = "1")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "수정할 안전 귀가 경로 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SafeRouteUpdateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "안전 귀가 경로 수정 요청 예시",
                                            summary = "안전 귀가 경로 수정에 필요한 데이터",
                                            value = """
                                                    {
                                                      "safe_route_id": 1,
                                                      "end_location": "수정테스트",
                                                      "end_time": "2025-04-13T17:20:15.789Z",
                                                      "route_path": [
                                                          {
                                                            "lat": 37.48041727265124,
                                                            "lng": 126.87951498295432
                                                          },
                                                          {
                                                            "lat": 37.48050994547239,
                                                            "lng": 126.87886819594975
                                                          }
                                                        ]
                                                    
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "안전 귀가 경로 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "안전 귀가 경로를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{safeRouteId}")
    public ResponseEntity<?> update(@Validated @RequestBody SafeRouteUpdateDto dto) {
        safeRouteService.update(dto);
        return ResponseEntity.ok().build();
    }

    // 안전 귀가 루트 상태 변경 (사용자 도중 포기, 실패, 완료)
    @Operation(
            summary = "안전 귀가 경로 상태 변경",
            description = "안전 귀가 경로의 상태를 변경합니다. (진행중, 완료, 실패, 취소 등)",
            parameters = {
                    @Parameter(name = "safeRouteId", description = "상태를 변경할 안전 귀가 경로 ID", required = true, example = "1")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "변경할 상태 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SafeRouteChangeStateDto.class),
                            examples = {
                                    @ExampleObject(
                                            name = "상태 변경 요청 예시",
                                            summary = "안전 귀가 경로 상태 변경 데이터",
                                            value = """
                                                    {
                                                      "state": "FINISHED"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "안전 귀가 경로 상태 변경 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 또는 유효하지 않은 상태값",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "안전 귀가 경로를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{safeRouteId}/state")
    public ResponseEntity<?> update(
            @Validated @RequestBody SafeRouteChangeStateDto dto, @PathVariable Long safeRouteId) {
        safeRouteService.changeStatus(dto.getState(), safeRouteId);
        return ResponseEntity.ok().build();
    }

    // 안전 귀가 루트 삭제
    @Operation(
            summary = "안전 귀가 경로 삭제",
            description = "안전 귀가 경로를 삭제합니다.",
            parameters = {
                    @Parameter(name = "safeRouteId", description = "삭제할 안전 귀가 경로 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "안전 귀가 경로 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "안전 귀가 경로를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{safeRouteId}")
    public ResponseEntity<?> delete(@PathVariable Long safeRouteId) {
        safeRouteService.delete(safeRouteId);
        return ResponseEntity.ok().build();
    }

    // 안전 귀가 루트 가져오기 (현재 한 건)
    @Operation(
            summary = "안전 귀가 경로 상세 조회",
            description = "특정 안전 귀가 경로의 상세 정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "safeRouteId", description = "조회할 안전 귀가 경로 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "안전 귀가 경로 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SafeRouteResponseDto.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "안전 귀가 경로 조회 응답 예시",
                                                    summary = "안전 귀가 경로 상세 정보",
                                                    value = """
                                                            {
                                                              "safe_route_id": 1,
                                                              "end_location": "서울시 서초구 방배동 789-10",
                                                              "end_time": "2025-04-03T17:20:15.789Z",
                                                              "route_path": ["geometry(lineString, 4326)"]
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "안전 귀가 경로를 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{safeRouteId}")
    public ResponseEntity<SafeRouteResponseDto> getOne(@PathVariable Long safeRouteId) {
        return ResponseEntity.ok(safeRouteService.getSafeRoute(safeRouteId));
    }

    // 안전 귀가 루트 가져오기 (마이페이지 - 회원 귀가 루트 내역 List)
    @Operation(
            summary = "회원별 안전 귀가 경로 목록 조회",
            description = "특정 회원의 모든 안전 귀가 경로 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "memberNumber", description = "회원 번호", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "안전 귀가 경로 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SafeRouteResponseDto.class, type = "array"),
                                    examples = {
                                            @ExampleObject(
                                                    name = "회원별 안전 귀가 경로 목록 응답 예시",
                                                    summary = "회원의 안전 귀가 경로 목록",
                                                    value = """
                                                            [
                                                              {
                                                                "safe_route_id": 1,
                                                                "end_location": "서울시 서초구 방배동 789-10",
                                                                "end_time": "2025-04-03T17:20:15.789Z",
                                                                "route_path": ["geometry(lineString, 4326)"]
                                                              },
                                                              {
                                                                "safe_route_id": 2,
                                                                "end_location": "서울시 서초구 방배동 789-15",
                                                                "end_time": "2025-04-03T18:20:15.789Z",
                                                                "route_path": ["geometry(lineString, 4326)"]
                                                              }
                                                            ]
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "회원을 찾을 수 없음",
                            content = @Content(schema = @Schema(implementation = RuntimeException.class))
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/member/{memberNumber}")
    public ResponseEntity<List<SafeRouteResponseDto>> getMemberRouteList(@PathVariable Long memberNumber) {
        return ResponseEntity.ok(safeRouteService.getMemberSafeRoutes(memberNumber));
    }
}