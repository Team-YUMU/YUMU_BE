package com.yumu.yumu_be.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yumu.yumu_be.auth.dto.KakaoInfoDto;
import com.yumu.yumu_be.common.dto.CommonResponse;
import com.yumu.yumu_be.exception.NotFoundException;
import com.yumu.yumu_be.jwt.JwtUtil;
import com.yumu.yumu_be.member.entity.LoginStatus;
import com.yumu.yumu_be.member.entity.Member;
import com.yumu.yumu_be.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTokenService redisTokenService;

    //카카오 로그인 로직
    @Transactional
    public CommonResponse kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException{
        String kakaoToken = getToken(code); //인가 코드로 액세스 토큰 요청
        KakaoInfoDto kakaoInfoDto = getKakaoUserInfo(kakaoToken); //토큰으로 카카오 api 호출 및 사용자 정보 가져옴
        Member kakaoMember = registerKaKaoMemberIfNeeded(kakaoInfoDto); //받은 정보로 회원가입 처리

        String accessToken = jwtUtil.createAccessToken(kakaoMember.getEmail(), kakaoMember.getId());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);

        String refreshToken = jwtUtil.createRefreshToken(kakaoMember.getEmail());
        response.addHeader(JwtUtil.REFRESH_HEADER, refreshToken);

        redisTokenService.addRefreshTokenByRedis(kakaoMember.getEmail(), refreshToken, Duration.ofDays(1));

        kakaoMember.updateLoginStatus(LoginStatus.KAKAO);

        return new CommonResponse("카카오 로그인 성공");
    }

    //인가 코드로 access token 요청 로직
    private String getToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();        //header 생성
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();   //body 생성
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);  //kakao에 요청
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();   //json으로 응답 받고 access token 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    //토큰으로 카카오 api 호출하는 로직
    private KakaoInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();        //헤더 생성
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);     //요청 보내기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();   //받은 응답에서 정보 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        Long id = jsonNode.get("id").asLong();      //dto에 담을 정보 처리
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        return new KakaoInfoDto(id, nickname, email);
    }

    //kakao로 로그인한 회원의 회원가입 처리 로직
    @Transactional
    public Member registerKaKaoMemberIfNeeded(KakaoInfoDto kakaoMemberInfo) {
        String checkEmail = kakaoMemberInfo.getEmail();     //현재 존재하는 계정인지 확인
        String nickname = kakaoMemberInfo.getNickname();
        Long providerId = kakaoMemberInfo.getId();
        String provider = "kakao";

        Member kakaoMember;

        //일반, 카카오 회원가입 기록이 없는 회원 => 회원가입 처리
        if (!memberRepository.existsByEmail(checkEmail)) {
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            kakaoMember = new Member(nickname, checkEmail, encodedPassword, provider, providerId);
            memberRepository.save(kakaoMember);
        }

        //일반 or 카카오 회원가입 기록이 있는 경우 => 회원가입 처리 x, 정보 그대로 가져옴
        kakaoMember = memberRepository.findByEmail(checkEmail).orElseThrow(NotFoundException.NotFoundMemberException::new);

        //일반 회원가입 기록이 있지만 카카오 회원가입 기록이 없는 경우 => provider 수정
        if (kakaoMember.getProvider().isEmpty()) {
            kakaoMember.updateProvider(providerId, provider);
        }

        return kakaoMember;
    }
}
