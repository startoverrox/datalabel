package com.portfolio.datalabel.domain.job;

public enum JobStatus {
    READY,          // 작업 대기 중
    ASSIGNED,       // 작업자 할당됨
    SUBMITTED,      // 작업 제출됨 (검수 대기)
    COMPLETED,      // 검수 완료 (최종 승인)
    REJECTED        // 반려됨
}
