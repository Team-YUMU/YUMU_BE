package com.yumu.yumu_be.config;

import com.yumu.yumu_be.jwt.JwtFilter;
import com.yumu.yumu_be.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity //시큐리티 필터를 스프링 필터체인에 등록
@EnableMethodSecurity(securedEnabled = true) //@Secure 어노테이션 사용
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //이후 추가 설정 필요 - cors나 허용 경로 등등...
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)    //로그인 폼 미사용
                .httpBasic(AbstractHttpConfigurer::disable);   //http basic 미사용

        http
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/v1/auction/paging").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/auction/{id}").permitAll()
                                .requestMatchers("/ws-stomp/**").permitAll()
                                .anyRequest().authenticated());

        http
                .addFilterBefore(new JwtFilter(jwtUtil, redisTemplate), UsernamePasswordAuthenticationFilter.class);

        //clear-site-data를 이용한 로그아웃 - 웹사이트의 쿠키, 저장소, 캐시 모두 지움
        HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL));
        http
                .logout((logout) -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(clearSiteData));
        return http.build();

    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader(JwtUtil.AUTHORIZATION_HEADER);
        config.addExposedHeader(JwtUtil.REFRESH_HEADER);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }





}
