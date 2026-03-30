package com.portfolio.datalabel.domain.job;

import org.springframework.data.jpa.repository.JpaRepository;

// 부모 타입인 AbstractJob으로 Repository를 하나만 만들면,
// 자식 타입(BboxJob 등)도 모두 여기서 조회/저장 가능합니다
public interface JobRepository extends JpaRepository<AbstractJob, Long> {
}
