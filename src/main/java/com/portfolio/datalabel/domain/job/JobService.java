package com.portfolio.datalabel.domain.job;

import com.portfolio.datalabel.domain.job.strategy.JobStrategy;
import com.portfolio.datalabel.domain.member.Member;
import com.portfolio.datalabel.domain.project.Project;
import com.portfolio.datalabel.global.exception.CustomException;
import com.portfolio.datalabel.global.exception.ErrorCode;
import jakarta.persistence.DiscriminatorValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final Map<JobType, JobStrategy> strategyMap;

    // point: 스프링이 JobStrategy 인터페이스를 구현한 모든 클래스(23개)를 List로 자동 주입해 줍니다.
    // 이를 생성자에서 Map<타입, 전략> 형태로 매핑해두면, 0(1)의 속도로 원하는 전략을 찾아 쓸 수 있습니다.
    public JobService(JobRepository jobRepository, List<JobStrategy> strategies) {
        this.jobRepository = jobRepository;
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(JobStrategy::getJobType, Function.identity()));
    }

    // 1. 공통 작업 생성 라우팅
    @Transactional
    public Long createJob(Project project, JobType jobType, String specificData) {
        JobStrategy strategy = getStrategy(jobType);

        // 고유 생성 로직은 각 전략 객체에게 위임 (다형성)
        AbstractJob newJob = strategy.createJob(project, specificData);
        return jobRepository.save(newJob).getId();}

    // 2. 공통 작업 제출 워크플로우
    @Transactional
    public void submitJob(Long jobId, String resultData, Member worker) {
        AbstractJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        // 해당 잡의 타입에 맞는 전략을 꺼내옵니다.
        JobStrategy strategy = getStrategy(JobType.valueOf(job.getClass().getAnnotation(DiscriminatorValue.class).value()));

        // 고유 데이터 검증 -> 고유 데이터 업데이트 -> 공통 상태 업데이트 (제출 완료)
        strategy.validateSubmitData(resultData);
        strategy.updateJobResult(job, resultData);
        job.submit();
    }

    // Map에서 전략을 안전하게 꺼내오는 메서드
    private JobStrategy getStrategy(JobType jobType) {
        JobStrategy strategy = strategyMap.get(jobType);
        if (strategy == null) {
            throw new IllegalArgumentException("지원하지 않는 Job 타입입니다: " + jobType);
        }
        return strategy;
    }
}
