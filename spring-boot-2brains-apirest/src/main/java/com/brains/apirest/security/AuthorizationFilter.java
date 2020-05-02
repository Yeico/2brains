package com.brains.apirest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.brains.apirest.security.delegates.JWTSecurity;
import com.brains.apirest.security.interfaces.IJWTSecurity;

/**
 * Clase para la autorización del usuario en la diferentes rutas de la API
 * @author Sergi
 *
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

	/**
	 * Variable con la interfaz del token
	 */
	private IJWTSecurity jwtService = null;
	
	/**
	 * Contructor de la clase
	 * @param authenticationManager Clase que procesa y maneja la autenticación del usuario
	 * @param jwtService Interfaz la creación y validación del token
	 */
	public AuthorizationFilter(AuthenticationManager authenticationManager, IJWTSecurity jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
	}

	/**
	 * Filtro para la obtención y validación del token desde la cabecera http
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(JWTSecurity.HEADER); //Obtiene el token a través de la clave

		if (!requiresAuthentication(header)) { //Valida si existe un token en la cabecera
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = null;

		if(jwtService.validate(header)) { //Validación del token
			authentication = new UsernamePasswordAuthenticationToken(jwtService.getEmail(header), null, jwtService.getRoles(header));
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}
	
	/**
	 * Valida si existe un token con el prefijo Bearer en la petición http
	 * @param header Cadena con el token
	 * @return Retorna un tipo: boolean. Si existe dicho prefijo en la cabecera retorna <code>true</code> en caso 
	 * 			contrario retorna <code>false</code>
	 */
	protected boolean requiresAuthentication(String header) {
		if (header == null || !header.startsWith(JWTSecurity.PREFIX)) {
			return false;
		}
		return true;
	}
	
}
