package com.dodo.smartsafereturn.safeRoute.entity;

public enum RouteState {
    // 진행 중, 방금 시작, 완수, 실패, 사용자가 직접 취소
    IN_PROGRESS, STARTED, FINISHED, FAILED, CANCELLED
}
