package com.portfolio.datalabel.domain.member;

import com.portfolio.datalabel.domain.member.dto.MemberLoginRequest;
import com.portfolio.datalabel.domain.member.dto.MemberResponse;
import com.portfolio.datalabel.domain.member.dto.MemberSignUpRequest;
import com.portfolio.datalabel.domain.member.dto.TokenResponse;
import com.portfolio.datalabel.global.exception.CustomException;
import com.portfolio.datalabel.global.exception.ErrorCode;
import com.portfolio.datalabel.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberResponse signup(MemberSignUpRequest request) {
        // 1. 이메일 중복 검사
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATION); // 향후 Custom Exception으로 변경 예정
        }

        // 2. 비밀번호 암호화 및 엔티티 생성
        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .role(Role.USER)
                .build();

        // 3. 저장 및 응답 DTO 반환
        Member savedMember = memberRepository.save(member);
        return MemberResponse.from(savedMember);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(MemberLoginRequest request) {
        // 1. 회원 조회
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 3. JWT 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole().name());

        return new TokenResponse(accessToken, "Bearer", 3600L);
    }
}
