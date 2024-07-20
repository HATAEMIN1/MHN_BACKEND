package com.project.mhnbackend.security.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.project.mhnbackend.common.util.JWTUtil;
import com.project.mhnbackend.member.dto.request.MemberDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("---------------doFilterInternal-----------------");
        log.info("---------------doFilterInternal-----------------");
        log.info("---------------doFilterInternal-----------------");

        String authHeaderStr = request.getHeader("Authorization");

        if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
            log.error("Authorization header is missing or does not start with Bearer");
            sendErrorResponse(response, "ERROR_ACCESS_TOKEN");
            return;
        }

        try {
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);

            String email = (String) claims.get("email");
            String password = (String) claims.get("password");
            String name = (String) claims.get("name");
            String nickname = (String) claims.get("nickname");
            String tel = (String) claims.get("tel");
            List<String> memberTypeList = (List<String>) claims.get("memberTypeList");

            MemberDTO memberDTO = new MemberDTO(email, password, name, nickname, tel, memberTypeList);
            log.info("-----------------------------------");
            log.info(memberDTO);
            log.info(memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDTO, password, memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT Check Error..............");
            log.error(e.getMessage(), e); // 예외 전체를 로그에 기록
            sendErrorResponse(response, "ERROR_ACCESS_TOKEN");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        Gson gson = new Gson();
        String msg = gson.toJson(Map.of("error", errorMessage));

        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(msg);
        printWriter.close();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        log.info("check uri " + path);

        if (path.startsWith("/api/member/")) {
            return true;
        }

        return false;
    }
}
