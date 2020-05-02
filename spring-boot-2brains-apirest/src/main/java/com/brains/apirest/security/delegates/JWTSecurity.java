package com.brains.apirest.security.delegates;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.brains.apirest.security.SimpleGrantedAuthorityMixin;
import com.brains.apirest.security.interfaces.IJWTSecurity;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Servicio/Componente par la creación y validación de un token JWT
 * @author Sergi
 *
 */
@Service
public class JWTSecurity implements IJWTSecurity {
	
	//Tiempo máximo de expiración de un token
	@Value("${authentication.maxEpirationMilisecond}")
	private long maxEpirationMilisecond;
	
	public static final String SECRETKEY = "2brains"; //Clave secreta del token
	public static final String HEADER = "Authorization"; //Clave para asignar el token en la respuesta http
	public static final String PREFIX = "Bearer "; //Prefijo del token
	
	/**
	 * Creación de un token a través del correo electrónico del usuario
	 * @return Retorna un tipo: String
	 */
	@Override
	public String create(Authentication auth) throws IOException {
		String userEmail = auth.getName();
		Collection<? extends GrantedAuthority> rol = auth.getAuthorities();
		
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(rol));
		
		//Generación del token
		String token = Jwts.builder()
				.setClaims(claims)
				.setSubject(userEmail)
				.signWith(SignatureAlgorithm.HS512, SECRETKEY.getBytes())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + this.maxEpirationMilisecond))
				.compact();

		return token;
	}

	/**
	 * Valida un token
	 */
	@Override
	public boolean validate(String token) {
		try {
			getClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * Retorna los claims existentes del token
	 */
	@Override
	public Claims getClaims(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRETKEY.getBytes())
				.parseClaimsJws(resolve(token)).getBody();
		return claims;
	}

	/**
	 * Retorna los roles que se encuentran al interior del token
	 */
	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		Object roles = getClaims(token).get("authorities");

		Collection<? extends GrantedAuthority> authorities = Arrays
				.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
						.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

		return authorities;
	}
	
	/**
	 * Retorna el correo electrónico al interior del token
	 */
	@Override
	public String getEmail(String token) {
		return getClaims(token).getSubject();
	}

	/**
	 * Remplaza el prefijo del token por %A0 (hex)
	 */
	@Override
	public String resolve(String token) {
		if (token != null && token.startsWith(PREFIX)) {
			return token.replace(PREFIX, "");
		}
		return null;
	}
	
}
