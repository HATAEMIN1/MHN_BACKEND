package com.project.mhnbackend.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.mhnbackend.security.handler.APILoginFailHandler;
import com.project.mhnbackend.security.handler.APILoginSuccessHandler;
import com.project.mhnbackend.security.handler.CustomAccessDeniedHandler;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@ReadingConverter
@EnableMethodSecurity
public class CustomSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("=================#1 SecurityFilterChain=====================");

		http.cors(httpSecurityCorsConfigurer -> {
			httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
		});

		http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.csrf(config -> config.disable());

		http.formLogin(config -> {
			config.loginPage("/api/member/login"); // security
			config.successHandler(new APILoginSuccessHandler());
			config.failureHandler(new APILoginFailHandler());
		}

		);

		http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

		http.exceptionHandling(config -> {
			config.accessDeniedHandler(new CustomAccessDeniedHandler());
		});

		return http.build();
	};

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("*"));
//		configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"));
//		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
//		configuration.setAllowCredentials(true);
		configuration.setAllowedOriginPatterns(List.of("")); // 로컬호스트 허용
        configuration.addAllowedMethod(""); // 모든 HTTP 메소드 허용 (GET, POST, 등)
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

