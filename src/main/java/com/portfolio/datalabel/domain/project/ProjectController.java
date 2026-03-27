package com.portfolio.datalabel.domain.project;

import com.portfolio.datalabel.domain.project.dto.ProjectCreateRequest;
import com.portfolio.datalabel.domain.project.dto.ProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Project", description = "프로젝트 관리 API")
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "프로젝트 생성", description = "새로운 라벨링 프로젝트를 생성합니다.")
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectCreateRequest request,
            Authentication authentication // JwtAuthenticatoinFilter에서 넣어준 인증 정보
    ) {
        // 로그인한 사용자의 이메일(Subject)을 전달
        ProjectResponse response = projectService.createProject(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "프로젝트 목록 조회", description = "페이징 처리된 프로젝트 목록을 최신순으로 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getProjects(
            // 기본값: 1페이지(0부터 시작), 10개씩
            @ParameterObject @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(projectService.getProjects(pageable));
    }
}
