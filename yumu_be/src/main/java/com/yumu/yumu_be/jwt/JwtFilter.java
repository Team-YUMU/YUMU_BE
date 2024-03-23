package com.yumu.yumu_be.jwt;

import com.yumu.yumu_be.member.entity.Member;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtUtil.resolveAccessToken(request);
        //1. access token 유효할 경우
        //2. access token 유효하지 않고 refresh token 유효할 경우
        //3. access token, refresh token 유효하지 않을 경우
        //4. token 없을 경우
        if (accessToken != null) {                        //토큰이 존재하고

            try {
                validBlackToken(accessToken);   //로그아웃된 토큰인지 검사
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if(!jwtUtil.validationToken(accessToken)) {   //access token이 유효하지 않음
                String refreshToken = jwtUtil.resolveRefreshToken(request);

                String email = jwtUtil.getUserInfoFromToken(refreshToken).getSubject(); //refresh token으로 user 정보 가져옴

                if (jwtUtil.validationRefreshToken(email, refreshToken)) {  //refresh token 유효성 확인
                    Member member = jwtUtil.getMemberFromToken(email);
                    accessToken = jwtUtil.createAccessToken(email, member.getId());
                    response.setHeader("Authorization", accessToken); //새 access token 발급 및 교체
                } else {
                    return; // exception handler 이후에 추가해야함 => 토큰 둘 다 유효하지 않음
                }
            }
            setAuthentication(accessToken);     //access token이 유효할 경우와 refresh token으로 새 access token을 발급한 경우
        }
        filterChain.doFilter(request, response);
    }

    //로그아웃된 토큰인지 검사하는 로직
    public void validBlackToken(String accessToken) throws Exception {
        String blackToken = redisTemplate.opsForValue().get(accessToken);
        if (StringUtils.hasText(blackToken)) {      //null이 나오면 블랙리스트에 토큰이 없다 == 로그아웃된 상태가 아님
            throw new Exception(); //이후에 수정
        }
    }

    //SecurityContext에 Authentication 객체 저장
    public void setAuthentication(String token) {
        Claims info = jwtUtil.getUserInfoFromToken(token);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(info.getSubject());
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

}
