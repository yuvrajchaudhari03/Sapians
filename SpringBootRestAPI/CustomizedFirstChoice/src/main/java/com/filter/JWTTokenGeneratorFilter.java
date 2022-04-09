package com.filter;

import com.constants.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    /*Generate a JWT token here*/
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String email = authentication.getName();
//            String username = userService.getUserByEmail(email).getUsername();
            String jwt = Jwts.builder().setIssuer("Sapien").setSubject("JWT Token")
                    .claim("u_email", email)
//                    .claim("username", username)
                    .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 30000000))
                    .signWith(key).compact();

            /*We will set this token to the header. We have to allow this header in cors configuration*/
            response.setHeader(SecurityConstants.JWT_HEADER, jwt);
        }

        chain.doFilter(request, response);
    }

    /**/
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(request.getServletPath().equals("/user/login") ||
                StringUtils.containsIgnoreCase(request.getServletPath(), "generateOtp") ||
                StringUtils.containsIgnoreCase(request.getServletPath(), "validateOtp"));
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
