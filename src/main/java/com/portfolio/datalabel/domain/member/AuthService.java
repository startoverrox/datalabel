package com.portfolio.datalabel.domain.member;

import com.portfolio.datalabel.domain.member.dto.MemberLoginRequest;
import com.portfolio.datalabel.domain.member.dto.MemberResponse;
import com.portfolio.datalabel.domain.member.dto.MemberSignUpRequest;
import com.portfolio.datalabel.domain.member.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    // private final JwtTokenTrovider jwtTokenProvider; // 다음 단계에서 구현할 컴포넌트

    @Transactional
    public MemberResponse signup(MemberSignUpRequest request) {
        // 1. 이메일 중복 검사
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일 입니다."); // 향후 Custom Exception으로 변경 예정
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
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 토큰 발급 (임시 구현체 반환 - 토큰 생성 로직은 다음 단계에서 연결)
        // String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole().name());
        String dummyToken = "jwt.dummy.token.here"; // 임시
        return new TokenResponse(dummyToken, "Bearer", 3600L);
    }
}
