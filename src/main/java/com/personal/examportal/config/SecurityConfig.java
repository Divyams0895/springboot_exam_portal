package com.personal.examportal.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.personal.examportal.service.CustomUserDetailsService;
import com.personal.examportal.service.SettingsService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
 class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Autowired
    private SettingsService settingsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/dashboard").hasRole("ADMIN")
                .requestMatchers("/home").hasRole("STUDENT")
                .requestMatchers("/exams", "/profile","/exam/*").authenticated() // Require login
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .successHandler(successHandler()) // redirect based on role
            )
            .logout(logout -> logout
            		.logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException {
            	
            	// Set session attribute
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(10 * 60); // 10 minutes
                session.setAttribute("username", authentication.getName());

                // Set cookie
                Cookie cookie = new Cookie("user", authentication.getName());
                cookie.setMaxAge(10 * 60); // 10 minutes
                cookie.setHttpOnly(true); // optional but safer
                cookie.setPath("/");
                response.addCookie(cookie);

                // Redirect based on role
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                for (GrantedAuthority authority : authorities) {
//                	System.out.println(authority.getAuthority());
                    if (authority.getAuthority().equals("ROLE_ADMIN")) {
                        response.sendRedirect("/dashboard");
                        return;
                    } else if (authority.getAuthority().equals("ROLE_STUDENT")) {
                    	//request.setAttribute("settings", settingsService.getSettings());
                        response.sendRedirect("/");
                        return;
                    }
                }
                response.sendRedirect("/");
            }
        };
    }
}
