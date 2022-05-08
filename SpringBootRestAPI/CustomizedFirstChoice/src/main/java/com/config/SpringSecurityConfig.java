package com.config;

import com.entities.Role;
import com.filter.JWTTokenGeneratorFilter;
import com.filter.JWTTokenValidatorFilter;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

/*This class will have all all the configuartions*/
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPointConfig entryPoint;
    @Autowired
    private UserService userDetailsService;

    /*This method will get UserDetails (Objects of User) from UserDetailsServiceImpl class. and also, add password encoder. */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }


    /*This method will have logic for securing endpoint*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
                cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }).and()
                .csrf().disable()
				.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .anonymous().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/user").permitAll()
                .antMatchers(HttpMethod.GET, "/user").hasAuthority(Role.ADMIN)
                .antMatchers(HttpMethod.PUT, "/user").authenticated()
                .antMatchers(HttpMethod.GET, "/user/my_profile").authenticated()
                .antMatchers(HttpMethod.GET, "/user/*").hasAuthority(Role.ADMIN)
                .antMatchers(HttpMethod.POST, "/user/giveAuthority").hasAuthority(Role.ADMIN)
                .antMatchers(HttpMethod.POST, "/user/vendor").authenticated()
                .antMatchers(HttpMethod.POST, "/user/addMoney").authenticated()
                .antMatchers(HttpMethod.DELETE, "/user/*").hasAuthority(Role.ADMIN)
                .antMatchers(HttpMethod.GET, "/product/byVendor/*").hasAuthority(Role.VENDOR)
                .antMatchers(HttpMethod.POST, "/upload_product_image").hasAnyAuthority(Role.VENDOR, Role.ADMIN)
                .antMatchers("/contactUs").permitAll()
                .antMatchers(HttpMethod.GET,"/product/*").permitAll()
                .antMatchers("/product/addproduct").hasAuthority(Role.VENDOR)
                /*Requests that are not register here will be validated by JWT*/
                .and().httpBasic()
                /*This will add entrypoint(AuthenticationEntryPointConfig bean) and commence method will handle exception while authentication process.*/
                .authenticationEntryPoint(entryPoint);
    }
}
