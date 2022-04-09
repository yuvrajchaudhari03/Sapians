package com.config;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*This class will handle cases of failure of auth*/
@Component
public class AuthenticationEntryPointConfig implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String message = "Sorry, You're not authorized to access this resource.";
        if(e instanceof BadCredentialsException){
            message = "Login Failed..!!!! Please enter correct credentials.";
        }
        response.sendError(response.SC_UNAUTHORIZED, message);
    }
}