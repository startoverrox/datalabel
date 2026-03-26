package com.portfolio.datalabel.domain.member.dto;

public record TokenResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) {
}
