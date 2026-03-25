package com.portfolio.datalabel.domain.member.dto;

import com.portfolio.datalabel.domain.member.Member;

public record MemberResponse(
        Long id,
        String email,
        String name
) {
    // Entity를 DTO로 변환하는 정적 팩토리 메서드 패턴
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName()
        );
    }
}
