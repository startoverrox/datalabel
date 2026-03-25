package com.portfolio.datalabel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 활성화 (생성일/수정일 자동 기록)
@SpringBootApplication
public class DatalabelApplication {
	public static void main(String[] args) {
		SpringApplication.run(DatalabelApplication.class, args);
	}

}
