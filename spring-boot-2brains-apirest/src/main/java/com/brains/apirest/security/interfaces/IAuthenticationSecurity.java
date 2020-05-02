package com.brains.apirest.security.interfaces;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface IAuthenticationSecurity {
	
	/**
	 * Valida las credenciales de un usuario (login) a través de la API de Firebase. En caso que la validación no se realice
	 * con exito se dispara una excepción del tipo: BadCredentialsException.
	 * @param email Correo electrónico
	 * @param password Contraseña
	 * @return Retorna un tipo: UsernamePasswordAuthenticationToken. -> Clase de "confianza" que manipula Spring Security
	 */
	public UsernamePasswordAuthenticationToken getUser(String email, String password);
}
