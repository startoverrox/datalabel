package com.portfolio.datalabel.domain.project.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectCreateRequest(
        @NotBlank(message = "프로젝트 이름은 필수입니다.")
        String name,
        String description
) {
}
