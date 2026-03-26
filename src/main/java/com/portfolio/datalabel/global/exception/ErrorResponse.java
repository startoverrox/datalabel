package com.portfolio.datalabel.global.exception;

import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public record ErrorResponse(
        int status,
        String code,
        String message
) {
    // ErrorCode 기반으로 응답 생성
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getStatus().value())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    // @Valid 등에서 발생한 상세 메시지를 커스텀해서 보내고 싶을 때 사용하는 오버로딩
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String customMessage) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getStatus().value())
                        .code(errorCode.getCode())
                        .message(customMessage)
                        .build());
    }
}
