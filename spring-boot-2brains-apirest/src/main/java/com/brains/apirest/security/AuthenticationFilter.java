package com.brains.apirest.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.brains.apirest.security.delegates.JWTSecurity;
import com.brains.apirest.security.interfaces.IAuthenticationSecurity;
import com.brains.apirest.security.interfaces.IJWTSecurity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase que se encarga de la autenticación del usuario. En caso que la autenticación sea exitosa se crea un token. En caso contrario
 * se deniega el acceso al usuario.
 * @author Sergi
 *
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	/**
	 * Variable con la interfaz de validación del usuario
	 */
	private IAuthenticationSecurity authenticationSecurity = null;
	
	/**
	 * Variable con la interfaz del token
	 */
	private IJWTSecurity jwtService = null;

	public AuthenticationFilter(AuthenticationManager authenticationManager, IJWTSecurity jwtService, IAuthenticationSecurity auth) {
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
		this.jwtService = jwtService;
		this.authenticationSecurity = auth;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {	
		String email = request.getParameter("email"); //Se obtiene el correo del usuario desde la petición http
		String password = request.getParameter("password"); //Se obtiene la contraseña del usuario desde la petición http
		
		if(email != null && password !=null) {
			logger.info("Username from request parameter (form-data): " + email);
			logger.info("Password from request parameter (form-data): " + password);
			
		} else {
			User user = null;
			try {
				
				user = new ObjectMapper().readValue(request.getInputStream(), User.class); //Mapeo a la clase User de spring
				
				email = user.getUsername();
				password = user.getPassword();
				
				logger.info("Username from request InputStream (raw): " + email);
				logger.info("Password from request InputStream (raw): " + password);
				
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//Validación del usuario consumiendo la API de Google.
		UsernamePasswordAuthenticationToken authToken = this.authenticationSecurity.getUser(email, password);
		
		return authToken;
	}

	/**
	 * En caso que la validación haya sido exitosa se genera un token y se retorna por la cabecera http
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String token = jwtService.create(authResult); //Creación del token
		
		response.addHeader(JWTSecurity.HEADER, JWTSecurity.PREFIX + token); //Se asigna el token a la cabecera
		
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("token", token);
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");
	}

	/**
	 * En caso que la validación haya fallado asigna un mensaje de retorno en la cabecera http
	 */
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("mensaje", "Error de autenticación: correo o password incorrecto!");
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		response.setContentType("application/json");
	}
}
