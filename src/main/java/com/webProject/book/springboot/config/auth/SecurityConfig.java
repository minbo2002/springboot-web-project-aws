package com.webProject.book.springboot.config.auth;

import com.webProject.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  //
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .headers().frameOptions().disable()  // h2-console 화면을 사용하기 위해 해당 옵션들 disabled

                /*
                    authorizeRequests()  :  URL별 권한 관리 설정 옵션
                    antMatchers()        :  권한 관리대상 지정 옵션  (  .permitAll() : 전체권한   /  .hasRole() : 특정권한  )
                    anyRequest()         :  설정된 값 이외의 나머지 URL  (  .authenticated() : 인증된 사용자  )

                    logout()             :  로그아웃기능 설정 옵션  (  .logoutSuccessUrl() : 로그아웃 성공시 이동할 URL  )

                    oauth2Login()        :  OAuth2 로그인 기능 설정 옵션
                    userInfoEndpoint()   :  OAuth2 로그인 성공이후 사용자정보 설정
                    userService()        :  소셜로그인 성공시 후속 조치를 진행할 인터페이스 구현체 등록
                 */

                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "h2-console").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()

                .and()
                    .logout()
                        .logoutSuccessUrl("/")

                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);

    }

}
