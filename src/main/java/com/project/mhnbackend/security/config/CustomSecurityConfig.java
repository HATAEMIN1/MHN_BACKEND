package com.project.mhnbackend.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableMethodSecurity
public class CustomSecurityConfig {
//   @Value("${cors.allowed-origins}")
//   private List<String> allowedOrigins;
   
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      log.info("=================#1 SecurityFilterChain=====================");
      
      http.cors(httpSecurityCorsConfigurer -> {
         httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
      });
      
      http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
      
      http.csrf(config -> config.disable());
      
      http.formLogin(config -> {
         config.loginProcessingUrl("/api/v1/member/login")
                 .successHandler(new APILoginSuccessHandler())
                 .failureHandler(new APILoginFailHandler())
                 .permitAll();
      });
      
      http.logout(logout -> {
         logout.logoutUrl("/api/vi/member/logout")
                 .logoutSuccessUrl("/api/v1/member/login")
                 .permitAll();
      });
      
      http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);
      
      http.authorizeHttpRequests(auth -> {
         auth.requestMatchers("/**").permitAll() // 모든 엔드포인트를 인증 없이 허용
                 .requestMatchers("/api/v1/doctors/login").permitAll() // Doctor 로그인 엔드포인트 허용
                 .requestMatchers("/api/v1/doctors/register").permitAll() // Doctor 회원가입 엔드포인트 허용
                 .requestMatchers("/api/v1/doctors/sendemail").permitAll() // Doctor 이메일 인증 엔드포인트 허용
                 .requestMatchers("/api/v1/doctors/verify").permitAll() // Doctor 이메일 확인 엔드포인트 허용
                 .requestMatchers("/api/v1/doctors/check-email").permitAll() // Doctor 이메일 중복 체크 엔드포인트 허용
                 .anyRequest().permitAll();
      });
      
      http.exceptionHandling(config -> {
         config.accessDeniedHandler(new CustomAccessDeniedHandler());
      });
      
      return http.build();
   }
   
   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOriginPatterns(List.of("http://3.36.123.236","http://mhnbucket.s3-website.ap-northeast-2.amazonaws.com")); // 프론트엔드 주소 허용
//      configuration.setAllowedOriginPatterns(allowedOrigins);
      configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")); // 모든 HTTP 메소드 허용
      configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type")); // 모든 헤더 허용
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