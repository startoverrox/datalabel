package com.portfolio.datalabel.domain.project;

import com.portfolio.datalabel.domain.member.Member;
import com.portfolio.datalabel.domain.member.MemberRepository;
import com.portfolio.datalabel.domain.project.dto.ProjectCreateRequest;
import com.portfolio.datalabel.domain.project.dto.ProjectResponse;
import com.portfolio.datalabel.global.exception.CustomException;
import com.portfolio.datalabel.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest request, String email) {
        // JWT 토큰에서 추출한 email로 현재 로그인한 사용자 정보 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Project project = Project.builder()
                .name(request.name())
                .description(request.description())
                .member(member)
                .build();

        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional (readOnly = true)
    public Page<ProjectResponse> getProjects(Pageable pageable) {
        return projectRepository.findAllWithMember(pageable)
                .map(ProjectResponse::from); // Page 객체 내부의 Entity를 DTO로 우아하게 변환
    }
}
