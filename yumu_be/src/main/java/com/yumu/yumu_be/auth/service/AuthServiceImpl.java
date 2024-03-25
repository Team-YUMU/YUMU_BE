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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTokenService redisTokenService;

    //회원가입
    @Override
    @Transactional
    public CommonResponse signUp(SignupRequest signupRequest) {
        String nickname = signupRequest.getNickname();
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        String checkPassword = signupRequest.getCheckPassword();

        isExistNickname(nickname); //닉네임 중복 확인
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

    //닉네임 중복 확인
    @Override
    @Transactional(readOnly = true)
    public CommonResponse checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new BadRequestException.DuplicatedNicknameException();
        }
        return new CommonResponse("사용 가능한 닉네임입니다.");
    }
    
    //이메일 중복 확인
    @Override
    @Transactional(readOnly = true)
    public CommonResponse checkEmail(String email) {
        if (memberRepository.existsByEmailAndProvider(email, "kakao")) {
            throw new BadRequestException.AlreadySignupKakaoException();
        } else if (memberRepository.existsByEmail(email)) {
            throw new BadRequestException.DuplicatedEmailException();
        }
        return new CommonResponse("사용 가능한 이메일입니다.");
    }

    //로그인
    @Override
    @Transactional(readOnly = true)
    public CommonResponse logIn(LoginRequest loginRequest, HttpServletResponse response) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundException.NotFoundMemberException::new);
        validPassword(password, member.getPassword());  //비밀번호 확인

        String accessToken = jwtUtil.createAccessToken(email, member.getId());  //access token 생성
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);

        String refreshToken = jwtUtil.createRefreshToken(email);
        response.addHeader(JwtUtil.REFRESH_HEADER, refreshToken);

        redisTokenService.addRefreshTokenByRedis(email, refreshToken, Duration.ofDays(1));   //redis에 refresh token 저장
        return new CommonResponse("로그인 완료");
    }

    //임시 비밀번호 발급
    @Override
    @Transactional(readOnly = true)
    public String findPassword(String email) {
        isExistEmail(email);
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundException.NotFoundMemberException::new);
        String password = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);
        member.updatePassword(encodedPassword);
        return password;
    }

    //로그아웃
    @Override
    @Transactional(readOnly = true)
    public CommonResponse logOut(HttpServletRequest request) {
        String accessToken = jwtUtil.resolveAccessToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(accessToken);

        Long expiration = jwtUtil.getExpiration(accessToken); //access token 남은 유효기간
        redisTokenService.logoutAndWitdrawAccessTokenByRedis(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);    //redis에 로그아웃 기록 저장

        redisTokenService.deleteRefreshTokenByRedis(claims.getSubject());    //refresh token 삭제

        return new CommonResponse("로그아웃 완료");
    }

    //탈퇴
    @Override
    @Transactional
    public CommonResponse withdraw(String password, HttpServletRequest request) {
        String accessToken = jwtUtil.resolveAccessToken(request);
        Claims claims = jwtUtil.getUserInfoFromToken(accessToken);

        //존재하는 멤버인지, 입력한 비밀번호와 일치하는지 확인
        Member member = memberRepository.findByEmail(claims.getSubject()).orElseThrow(NotFoundException.NotFoundMemberException::new);
        String encodedPassword = passwordEncoder.encode(password);
        if (!member.getPassword().equals(encodedPassword)) {
            throw new BadRequestException.NotMatchPasswordException();
        }

        Long expiration = jwtUtil.getExpiration(accessToken); //access token 남은 유효기간
        redisTokenService.logoutAndWitdrawAccessTokenByRedis(accessToken, "withdraw", expiration, TimeUnit.MILLISECONDS);    //redis에 로그아웃 기록 저장

        redisTokenService.deleteRefreshTokenByRedis(claims.getSubject());    //refreshToken 삭제

        //member 삭제
        memberRepository.delete(member);

        return new CommonResponse("탈퇴 완료");
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
