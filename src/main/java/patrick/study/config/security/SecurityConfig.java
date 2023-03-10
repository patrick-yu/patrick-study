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
	         * ????????? ?????? Ajax ????????? ???????????? ?????? ??????
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
     * ???????????? ????????? ?????? ??????
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ???????????? ???????????? ???, ????????? handler ??????
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

    /** ???????????? ?????? ????????? AJAX????????? ?????? 401??? ??????, AJAX????????? ?????? ?????? login?????? ???????????????
     * @param url
     * @return
     */
    private AjaxAwareAuthenticationEntryPoint ajaxAwareAuthenticationEntryPoint(String url) {
    	return new AjaxAwareAuthenticationEntryPoint(url);
    }
}
