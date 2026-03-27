package com.portfolio.datalabel.domain.project;

import com.portfolio.datalabel.domain.common.BaseTimeEntity;
import com.portfolio.datalabel.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "Text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    // point: 반드시 지연 로딩(LAZY)를 사용하여 불필요한 N+1 쿼리 발생을 원천 차단합니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Project(String name, String description, Member member) {
        this.name = name;
        this.description = description;
        this.status = ProjectStatus.READY; // 초기 상태는 항상 READY
        this.member = member;
    }
}
