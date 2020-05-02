package com.brains.apirest;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.brains.apirest.security.AuthenticationFilter;
import com.brains.apirest.security.AuthorizationFilter;
import com.brains.apirest.security.interfaces.IAuthenticationSecurity;
import com.brains.apirest.security.interfaces.IJWTSecurity;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	/**
	 * Inyección para el uso de los métodos de creación y validación del token.
	 */
	@Autowired
	private IJWTSecurity jwtService;
	
	/**
	 * Inyección para el uso de los métodos relacionados con la autenticación del usuario.
	 */
	@Autowired
	private IAuthenticationSecurity authenticationSecurity;
	
	/**
	 * Configuración de los parámetro de autenticación y autorización para Spring Security. También se configura Cors.
	 * Las sesiones se procesan de forma STATELESS.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().configurationSource(new CorsConfigurationSource() {
			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
	            config.setAllowedHeaders(Collections.singletonList("*"));
	            config.setAllowedMethods(Collections.singletonList("*"));
	            config.addAllowedOrigin("http://localhost:4200");
	            config.setAllowCredentials(true);
	            return config;
			}
		}).and().authorizeRequests()
		.anyRequest().authenticated()
		.and()
		.addFilter(new AuthenticationFilter(authenticationManager(), jwtService, authenticationSecurity))
		.addFilter(new AuthorizationFilter(authenticationManager(), jwtService))
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
