package com.brains.apirest.security.interfaces;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;

public interface IJWTSecurity {
	
	/**
	 * Creación de un token a través del correo electrónico del usuario
	 * @return Retorna un tipo: String
	 */
	public String create(Authentication auth) throws IOException;
	
	/**
	 * Valida un token
	 */
	public boolean validate(String token);
	
	/**
	 * Retorna los claims existentes del token
	 */
	public Claims getClaims(String token);
	
	/**
	 * Retorna los roles que se encuentran al interior del token
	 */
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;
	
	/**
	 * Retorna el correo electrónico al interior del token
	 */
	public String getEmail(String token);
	
	/**
	 * Remplaza el prefijo del token por %A0 (hex)
	 */
	public String resolve(String token);
}
