package com.portfolio.datalabel.domain.job.strategy;

import com.portfolio.datalabel.domain.job.AbstractJob;
import com.portfolio.datalabel.domain.job.JobType;
import com.portfolio.datalabel.domain.project.Project;

public interface JobStrategy {

    // 자신이 어떤 타입의 작업을 처리하는 전략인지 반환합니다.
    JobType getJobType();

    // 1. 해당 타입의 Job 엔티티를 생성하는 고유 로직
    AbstractJob createJob(Project project, String specificData);

    // 2. 사용자가 제출한 결과 데이터가 해당 타입의 규칙(예: BBOX는 x, y, w, h 필수)에 맞는지 검증
    void validateSubmitData(String resultData);

    // 3. 검증된 데이터를 실제 엔티티에 업데이트
    void updateJobResult(AbstractJob job, String resultData);
}
