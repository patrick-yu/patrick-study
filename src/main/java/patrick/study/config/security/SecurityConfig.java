package patrick.study.config.security;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsUtils;

import egovframework.com.cmm.service.EgovUserDetailsService;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.sec.ram.service.impl.EgovUserDetailsSecurityServiceImpl;
import mframework.security.AjaxAwareAuthenticationEntryPoint;

@Configurable
//@EnableWebSecurity(debug = true)
@EnableWebSecurity
public class SecurityConfig {
	private static final String LOGIN_URL = "/svc/login/index.do";

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		http
	        .authorizeRequests()
	        	.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
		        .antMatchers("/svc/login/**").permitAll()
		        .anyRequest().access("@authorizationChecker.check(request, authentication)")
	            //.anyRequest().authenticated()
	        .and()
	            .formLogin()
	            .loginPage(LOGIN_URL)
	            .loginProcessingUrl("/login")
	            //.defaultSuccessUrl("/svc/sample/main.do")
	            .failureUrl(LOGIN_URL + "?error=true")
	            .successHandler(successHandler())
	        .and()
	            .logout()
	            .logoutUrl("/logout")
	            //.deleteCookies("JSESSIONID", "SID")
	            //.logoutSuccessHandler(logoutSuccessHandler())
	        /*
	         * 인증이 안된 Ajax 요청을 처리하는 로직 추가
	         */
	        .and()
	        	.exceptionHandling()
	        	.authenticationEntryPoint(ajaxAwareAuthenticationEntryPoint(LOGIN_URL))
	        	;
		return http.build();
	}

	/**
	 * Security ignore
	 */
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.debug(false)
				.ignoring()
				.antMatchers("/favicon.ico","/static/**","/static/css/**","/static/img/**","/static/frontend/**");
    }

    /**
     * 비밀번호 인코딩 방법 설정
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 로그인이 성공했을 때, 실행할 handler 설정
     * @return
     */
    @Bean
    AuthenticationSuccessHandler successHandler() {
    	return new LoginSuccessHandler();
    }

    @Bean
    EgovUserDetailsHelper egovUserDetailsHelper() {
    	EgovUserDetailsHelper helper = new EgovUserDetailsHelper();
    	helper.setEgovUserDetailsService(egovUserDetailsSecurityService());
    	return helper;
    }

    @Bean
    EgovUserDetailsService egovUserDetailsSecurityService() {
    	return new EgovUserDetailsSecurityServiceImpl();
    }

    /** 인증되지 않은 요청중 AJAX요청일 경우 401로 응답, AJAX요청이 아닐 경우 login으로 리다이렉트
     * @param url
     * @return
     */
    private AjaxAwareAuthenticationEntryPoint ajaxAwareAuthenticationEntryPoint(String url) {
    	return new AjaxAwareAuthenticationEntryPoint(url);
    }
}
