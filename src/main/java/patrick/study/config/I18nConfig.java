package patrick.study.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import mframework.i18n.db.DBMessageSource;

@Configuration
public class I18nConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(localeChangeInterceptor())
//				.addPathPatterns("/**")
//				.excludePathPatterns("/static/**");

		registry.addInterceptor(localeChangeInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/static/**");
	}

	@Bean("messageSource")
	MessageSource messageSource() {
		DBMessageSource source = new DBMessageSource();
		return source;
	}

	@Bean
	LocaleResolver localeResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.KOREAN);
        resolver.setCookieName("lang");
        resolver.setCookieHttpOnly(true);
        return resolver;
	}

	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
	}

	/**
	 * URI Path를 이용한 locale change
	 * @return
	 */
//	@Bean
//	BnLocaleChangeInterceptor localeChangeInterceptor() {
//		BnLocaleChangeInterceptor interceptor = new BnLocaleChangeInterceptor();
//        return interceptor;
//    }

	/**
	 * Hostname을 이용한 locale change
	 * @return
	 */
//	@Bean
//	BnLocaleChangeHostInterceptor localeChangeInterceptor() {
//		BnLocaleChangeHostInterceptor interceptor = new BnLocaleChangeHostInterceptor();
//        return interceptor;
//    }

}
