package com.gamepan.gameboard.global.config;

import com.gamepan.gameboard.global.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration          // 스프링 설정 클래스로 등록
@EnableWebSecurity      // 스프링 시큐리티 활성화
@EnableMethodSecurity   // 메소드 단위 권한 체크를 허용
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomUserDetailsService customUserDetailsService;
    // password 인코딩
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DB에서 password를 가져와 비교한 후 인코딩
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    } //1


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())               // CSRF - 사이트 간 요청 위조
                                                                                    // CSRF 보호 기능을 임시로 끈다. 세션 인증 완성 시 다시 켠다.


                .authorizeHttpRequests(auth -> auth     // 각 경로 접근 권한 지정
                        .requestMatchers("/","/login", "/signup","/css/**", "/js/**").permitAll() // 누구나 접근 가능
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")                    // /admin은 ADMIN 권한 만 접근 가능
                        .requestMatchers("/api/**").authenticated()                           // 나머지는 로그인 시 접근 가능
                        .anyRequest().authenticated() //.permitAll()
                )

                .formLogin(form -> form                 // 로그인 설정
                        .loginPage("/login")                                       // 사용자 정의 로그인 페이지 경로
                        .loginProcessingUrl("/login")                                // 로그인 요청을 처리할 URL
                        .defaultSuccessUrl("/", true)         // 로그인 성공시 리다이렉트 될 페이지
                        .failureUrl("/login?error=true")          // 로그인 실패시 이동할 페이지
                        .permitAll()                                                 // 로그인 페이지는 비로그인 사용자도 접근 가능
                )

                .logout(logout -> logout                    // 로그아웃 설정
                        .logoutUrl("/logout")                                       // 사용자 정의 로그아웃 페이지 경로
                        .logoutSuccessUrl("/login?logout=true")                     // 로그아웃 성공시 리다이렉트
                        .invalidateHttpSession(true)                                  // 세션 무효화
                        .deleteCookies("JSESSIONID")                // JSESSIONID 쿠키 삭제
                );

        return http.build();
    }

    /*@Bean // 개발용 securityFilter 어디든 로그인없이 접근 가능
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()   //  전체 허용
                )
                .formLogin(form -> form.disable())   //  로그인 폼 비활성화
                .httpBasic(basic -> basic.disable()) // Basic 인증 비활성화
                .logout(logout -> logout.disable()); //  로그아웃 비활성화(선택)

        return http.build();
    }*/

}
