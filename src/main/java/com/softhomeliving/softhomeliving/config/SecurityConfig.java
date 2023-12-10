package com.softhomeliving.softhomeliving.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.softhomeliving.softhomeliving.services.SecurityFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	SecurityFilter securityFilter;
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))				
			.authorizeHttpRequests((authorize) -> 
			 	authorize
			 	.requestMatchers(HttpMethod.POST, "/usuario/login").permitAll()
			 	.requestMatchers(HttpMethod.POST, "/usuario/criar").hasRole("STAFF")  //STAFF == ADMIN
			 	.requestMatchers(HttpMethod.POST, "/app/condominio").hasRole("ADMIN") //ADMIN == MANAGER
			 	.anyRequest()
			 	.authenticated()
			)
			.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
