package com.yumu.yumu_be.security;

import com.yumu.yumu_be.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final Member member;
    private final String email;

    public UserDetailsImpl(Member member, String email) {
        this.member = member;
        this.email = email;
    }

    //인증 완료된 user를 가져오는 getter
    public Member getMember(){
        return member;
    }

    //권한 구분이 없어 null 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getNickname();
    }

    //계정 만료 여부 - 만료 안 됨
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠김 여부 - 잠김 안 됨
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부 - 만료 안 됨
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //사용자 활성화 여부 - 활성화
    @Override
    public boolean isEnabled() {
        return true;
    }


}
