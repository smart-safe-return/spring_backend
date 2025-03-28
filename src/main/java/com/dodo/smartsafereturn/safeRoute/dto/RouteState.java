package com.dodo.smartsafereturn.safeRoute.dto;

public enum RouteState {
    // todo 귀가루트에 완주여부 이거 뭐지
    // 진행 중, 방금 시작, 완수, 실패, 사용자가 직접 취소
    IN_PROGRESS, STARTED, FINISHED, FAILED, CANCELLED
}
