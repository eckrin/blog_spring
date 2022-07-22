package com.example.spring_blog.config.auth;

import com.example.spring_blog.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//스프링 시큐리티가 로그인 요청을 가로채서 완료시 UserDetails를 구현한 객체를 저장
public class PrincipalDetail implements UserDetails {

    private User user; //합성(composition)

    public PrincipalDetail(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정이 만료되지 않았는지를 리턴
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠기지 않았는지를 리턴
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되지 않았는지를 리턴
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화되지 않았는지를 리턴
    @Override
    public boolean isEnabled() {
        return true;
    }

    //계정의 권한 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_"+user.getRole(); //ROLE_을 붙여서 리턴해주어야함
            }
        });
        //= collectors.add(()->{return "ROLE_"+user.getRole();});

        return collectors;
    }
}
