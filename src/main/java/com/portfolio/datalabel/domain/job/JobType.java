package com.portfolio.datalabel.domain.job;

public enum JobType {
    BBOX,           // 바운딩 박스
    SEGMENTATION,   // 세그멘테이션
    KEYPOINTS       // 키포인트
    // 향후 VQA, TQA 등 20개 타입이 이곳에 추가됩니다.
}
