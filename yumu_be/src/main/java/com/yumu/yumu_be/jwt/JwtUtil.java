package com.yumu.yumu_be.jwt;

import com.yumu.yumu_be.auth.service.RedisTokenService;
import com.yumu.yumu_be.member.entity.Member;
import com.yumu.yumu_be.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final UserDetailsServiceImpl userDetailsService;
    public final RedisTokenService redisTokenService;
    public static final String AUTHORIZATION_HEADER = "Authorization"; //헤더에 들어가는 키값
    public static final String REFRESH_HEADER = "Refresh";
    public static final String AUTHORIZATION_KEY = "auth"; //사용자 권한 키값
    private static final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L; //access token 만료 시간 (30분)
    private static final long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 1000L; //refresh token 만료 시간 (24시간)
    public static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //이 알고리즘을 통해 키 암호화


    @Value("${spring.jwt.secret.key}")
    private String secretKey;
    private Key key; //토큰 만들 때 넣어줄 키 값


    //yml의 secret값을 key 변수에 할당
    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    //헤더에서 어세스 토큰 가져옴
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //헤더에서 refresh token 가져옴
    public String resolveRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(refreshToken)) {
            return refreshToken;
        }
        return null;
    }

    //access token 생성
    public String createAccessToken(String email, Long id) {
        Date date = new Date();
        return Jwts.builder()
                        .setSubject(email)
                        .claim(AUTHORIZATION_KEY, id)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    //refresh token 생성 및 redis 저장
    public String createRefreshToken(String email) {
        Date date = new Date();
        return Jwts.builder()
                        .setSubject(email)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    //access token 검증
    public boolean validationToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.info("TokenException");
            //throw new 설정한 exception명
        } catch (ExpiredJwtException e) {
            log.info("Token Expired");
            //throw new 설정한 exception명
        }
        return false;
    }

    //access token 남은 만료 시간
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    //refresh token 값 redis에서 가져오기
    public boolean validationRefreshToken(String email, String refreshToken) {
        return redisTokenService.getRefreshTokenByRedis(email).equals(refreshToken);
    }

    //토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    //토큰으로부터 member 객체 가져오기
    public Member getMemberFromToken(String email) {
        return userDetailsService.loadMemberByEmail(email);
    }


    //user 객체의 username 대신 email 넣음
    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByEmail(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }







}
