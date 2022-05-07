package com.service;

import com.constants.SecurityConstants;
import com.entities.Role;
import com.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class LoginService {
    public String login(UserDetails user){
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return generateJwt(auth);
    }

    public String login(User user){
        Collection<? extends GrantedAuthority> grantedAuthorities = rolesToGrantedAuthorities(user.getRoles());
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getU_email(),user.getU_password(),grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return generateJwt(auth);
    }

    public String loginWithoutPassword(String username,Collection<? extends GrantedAuthority> authorities){
        Authentication auth = new UsernamePasswordAuthenticationToken(username,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return generateJwt(auth);
    }

    public String loginWithoutPassword(String username,List<Role> authorities){
        Authentication auth = new UsernamePasswordAuthenticationToken(username,null,rolesToGrantedAuthorities(authorities));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return generateJwt(auth);
    }

    public String generateJwt(Authentication authentication) {
        if (null != authentication) {
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String email = authentication.getName();

            Integer user_id = null;
            if(authentication.getPrincipal() instanceof User){
                user_id = ((User) authentication.getPrincipal()).getU_id();
            }
            String jwt = Jwts.builder().setIssuer("Sapien").setSubject("JWT Token")
                    .claim("u_email", email)
                    .claim("u_id", user_id)
                    .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 30000000))
                    .signWith(key).compact();
            return jwt;
        }
        return null;
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

    private Collection<? extends GrantedAuthority> rolesToGrantedAuthorities(List<Role> roles){
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        for (Role role : roles){
            GrantedAuthority ga = new SimpleGrantedAuthority(role.getName());
            grantedAuths.add(ga);
        }
        return grantedAuths;
    }
}
