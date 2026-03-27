package com.portfolio.datalabel.domain.project.dto;

import com.portfolio.datalabel.domain.project.Project;

import java.time.LocalDateTime;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        String status,
        String creatorName, // 작성자 이름 (Member 객체어서 추출)
        LocalDateTime created_at
) {
    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus().name(),
                project.getMember().getName(),
                project.getCreatedAt()
        );
    }
}
