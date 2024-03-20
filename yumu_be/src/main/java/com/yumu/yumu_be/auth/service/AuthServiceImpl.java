package com.yumu.yumu_be.auth.service;

import com.yumu.yumu_be.auth.dto.LoginRequest;
import com.yumu.yumu_be.auth.dto.SignupRequest;
import com.yumu.yumu_be.common.dto.CommonResponse;
import com.yumu.yumu_be.exception.BadRequestException;
import com.yumu.yumu_be.exception.NotFoundException;
import com.yumu.yumu_be.jwt.JwtUtil;
import com.yumu.yumu_be.member.entity.Member;
import com.yumu.yumu_be.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    //회원가입
    @Override
    @Transactional
    public CommonResponse signUp(SignupRequest signupRequest) {
        String nickname = signupRequest.getNickname();
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        String checkPassword = signupRequest.getCheckPassword();
        
        isExistKaKaoEmail(email); //카카오 이메일 가입자인지 확인
        isExistEmail(email);    //일반 이메일 가입자인지 확인

        if (!password.equals(checkPassword)) {
            throw new BadRequestException.NotMatchPasswordException();
        }

        String encodedPassword = passwordEncoder.encode(password);  // 패스워드 암호화

        Member member = new Member(nickname, email, encodedPassword);
        memberRepository.save(member);

        return new CommonResponse("회원가입 완료");
    }

    //로그인
    @Override
    @Transactional(readOnly = true)
    public CommonResponse logIn(LoginRequest loginRequest, HttpServletResponse response) {
        String nickname = loginRequest.getNickname();
        String password = loginRequest.getPassword();

        Member member = memberRepository.findByNickname(nickname).orElseThrow(NotFoundException.NotFoundMemberException::new);
        validPassword(password, member.getPassword());  //비밀번호 확인

        String email = member.getEmail();

        Cookie cookie = jwtUtil.createCookie(email);    //refresh token 생성 및 쿠키 담기
        String refreshToken = cookie.getValue();
        String accessToken = jwtUtil.createAccessToken(email, member.getId());  //access token 생성
        response.addCookie(cookie);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);

        tokenService.addRefreshTokenByRedis(email, refreshToken, Duration.ofDays(1));   //redis에 refresh token 저장
        return new CommonResponse("로그인 완료");
    }

    //로그아웃
    @Override
    @Transactional(readOnly = true)
    public CommonResponse logOut(HttpServletRequest request) {
        String accessToken = jwtUtil.resolveToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(accessToken);

        Long expiration = jwtUtil.getExpiration(accessToken); //access token 남은 유효기간
        tokenService.logoutAccessTokenByRedis(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);    //redis에 로그아웃 기록 저장

        Cookie[] cookies = request.getCookies();            //refresh token 삭제
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                tokenService.deleteRefreshTokenByRedis(claims.getSubject());
            }
        }

        return new CommonResponse("로그아웃 완료");
    }

    //닉네임 중복 확인
    private void isExistNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new BadRequestException.DuplicatedNicknameException();
        }
    }

    //카카오로 가입된 사람인지 확인
    private void isExistKaKaoEmail(String email) {
        if (memberRepository.existsByEmailAndProvider(email, "kakao")) {
            throw new BadRequestException.AlreadySignupKakaoException();
        }
    }

    //이메일 중복 확인 확인
    private void isExistEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new BadRequestException.DuplicatedEmailException();
        }
    }

    //인코딩된 비밀번호 확인
    private void validPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BadRequestException.InvalidPasswordException();
        }
    }
}
