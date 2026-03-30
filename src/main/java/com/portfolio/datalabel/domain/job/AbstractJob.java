package com.portfolio.datalabel.domain.job;

import com.portfolio.datalabel.domain.common.BaseTimeEntity;
import com.portfolio.datalabel.domain.member.Member;
import com.portfolio.datalabel.domain.project.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jobs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// point: JOINED 전략을 사용하여, 공통 컬럼은 jobs 테이블에,
// 각 작업 고유의 컬럼은 자식 테이블(job_bbox 등)에 깔끔하게 정규화하여 저장합니다.
@Inheritance(strategy = InheritanceType.JOINED)
// point: DTYPE 컬럼을 통해 이 데이터가 BBOX인지 SEGMENTATION인지 구분합니다.
@DiscriminatorColumn(name = "job_type")
public abstract class AbstractJob extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Member worker; // 할당된 작업자 (초기엔 null)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    // 공통 로직: 작업 할당
    public void assignWorker(Member worker) {
        this.worker = worker;
        this.status = JobStatus.ASSIGNED;
    }

    // 공통 로직: 작업 제출
    public void submit() {
        this.status = JobStatus.SUBMITTED;
    }

    // 자식 클래스들이 생성될 때 공통 필드를 초기화 하기 위한 생성자
    protected AbstractJob(Project project) {
        this.project = project;
        this.status = JobStatus.READY;
    }
}
