package com.example.spring_blog.config;

import com.example.spring_blog.config.auth.PrincipalDetail;
import com.example.spring_blog.config.auth.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration //빈 등록(IoC)-스프링 컨테이너에서 객체 관리 가능
@EnableWebSecurity //security 관련 필터 추가 (security 필터로 등록)
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소 접근시 권한 및 인증 미리 체크
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailService principalDetailService;

    @Bean //BCryptPasswordEncoder를 스프링이 관리(IoC)
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder(); //해시를 이용한 암호화화
   }

//   시큐리티가 대신 로그인시 pw의 해시정보를 알아야지 DB의 해시와 비교 가능
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() //csrf토큰 비활성화 (테스트시에만 걸어두기)
            .authorizeHttpRequests()
                .antMatchers("/", "/auth/**", "/js/**", "/image/**")
                .permitAll() // 위에 적혀진 경로는 permitted
                .anyRequest()
                .authenticated() // 나머지경로는 인증 필요
            .and()
                .formLogin()
                .loginPage("/auth/loginForm") //인증이 필요한경우 여기로 이동
                .loginProcessingUrl("/auth/loginProc") //스프링 시큐리티가 해당 주소로 오는 로그인요청을 가로챈다
                .defaultSuccessUrl("/"); //정상요청시 이동
    }
}
