package com.portfolio.datalabel.domain.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // point: Fetch Join을 사용하여 Project 목록을 가져올 때 연관된 Member까지 쿼리 1번으로 가져와 N+1을 방지합니다.
    @Query("SELECT p FROM Project p JOIN FETCH p.member ORDER BY p.id DESC")
    Page<Project> findAllWithMember(Pageable pageable);
}
