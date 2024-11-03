package exam.master.security;

import exam.master.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
        log.debug("SecurityConfig 객체 생성됨!");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors()  // CORS 활성화
            .and()
            .csrf().disable()  // REST API의 경우 CSRF 비활성화
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/v1/login", "/v1/member/join", "/oauth2/authorization/google")
                .permitAll()  // 로그인, 회원가입은 인증 없이 접근 가능
                .anyRequest().authenticated()  // 그 외의 모든 요청은 인증 필요
            )
            .exceptionHandling()
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
            })
            .and()
            .oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    log.debug("소셜 로그인 성공");
                    response.sendRedirect("https://weasel.kkamji.net/home");
                })
                .failureHandler((request, response, authenticationException) -> {
                    log.debug("OAuth2 로그인 실패: {}", authenticationException.getMessage());
                    response.sendRedirect("/login?error");
                })
            )
            .formLogin(form -> form
                .loginProcessingUrl("/v1/login")  // 로그인 처리 API 경로
                .usernameParameter("email") // 로그인 시 사용자 이메일 파라미터 명
                .passwordParameter("password") // 로그인 시 사용자 비밀번호 파라미터 명
                .successForwardUrl("/v1/loginSuccess")
                .permitAll()
            )
            .logout(Customizer.withDefaults());
        return http.build();
    }

    // CORS 설정을 위한 Bean 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://weasel.kkamji.net");  // 허용할 도메인 설정
        configuration.addAllowedMethod("*");  // 모든 HTTP 메서드 허용
        configuration.addAllowedHeader("*");  // 모든 헤더 허용
        configuration.setAllowCredentials(true);  // 자격 증명 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService(MemberService memberService) {
        // 우리가 만든 UserDetailsService 객체를 사용한다.
        // => DB에서 사용자 정보를 가져올 것이다.
        return new MyUserDetailsService(memberService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
