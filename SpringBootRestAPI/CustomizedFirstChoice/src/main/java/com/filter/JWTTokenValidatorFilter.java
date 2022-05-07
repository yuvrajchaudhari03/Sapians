package com.filter;

import com.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {


	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
		if (null != jwt) {
			try {
				SecretKey key = Keys.hmacShaKeyFor(
						SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
				
				Claims claims = Jwts.parserBuilder()
						.setSigningKey(key)
						.build()
						.parseClaimsJws(jwt)
						.getBody();
				String username = String.valueOf(claims.get("u_email"));
				String authorities = (String) claims.get("authorities");
				Authentication auth = new UsernamePasswordAuthenticationToken(username,null,
						AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			catch (Exception e) {
				response.setHeader("Exception", "Daya pata lagao gadbad kaha h");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "kuch to gadbad h daya...." + e.getMessage());
				return;
			}
			
		}
		chain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
	  Boolean shouldSkip = request.getServletPath().equals("/user/login") ||
			  (request.getServletPath().contains("/project") && request.getMethod().equals(HttpMethod.GET.name()) ||
					  StringUtils.containsIgnoreCase(request.getServletPath(), "generateOtp") ||
					  StringUtils.containsIgnoreCase(request.getServletPath(), "validateOtp"));
	  return shouldSkip;
	}
	 
	
}
