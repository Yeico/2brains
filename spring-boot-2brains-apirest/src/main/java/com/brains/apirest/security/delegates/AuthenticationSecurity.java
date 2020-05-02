package com.brains.apirest.security.delegates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.brains.apirest.security.interfaces.IAuthenticationSecurity;

/**
 * Servicio/Componente que valida la existencia de un usuario (login) en Firebase a través de la API de este.
 * @author Sergi
 *
 */
@Service
public class AuthenticationSecurity implements IAuthenticationSecurity {

	private static HttpURLConnection connection;
	
	//Clave de la App en Firebase en la cuenta de correo
	@Value("${authentication.APIKey}")
	private String APIKey;
	
	//URL con la ruta a la API para la validación de usuarios en Firabase
	@Value("${authentication.URLAPI}")
	private String URLAPI;	
	
	/**
	 * Valida las credenciales de un usuario (login) a través de la API de Firebase. En caso que la validación no se realice
	 * con exito se dispara una excepción del tipo: BadCredentialsException.
	 * @param email Correo electrónico
	 * @param password Contraseña
	 * @return Retorna un tipo: UsernamePasswordAuthenticationToken. -> Clase de "confianza" que manipula Spring Security
	 */
	@Override
	public UsernamePasswordAuthenticationToken getUser(String email, String password) {

		boolean hasError = false;
		String jsonString = new JSONObject() //Se crea un JSON con los datos del usuario y el retorno de un token de valdiación
                .put("email", email)
                .put("password", password)
                .put("returnSecureToken", true)
                .toString();
		
		String line = new String("");
		BufferedReader reader = null;
		StringBuffer response = new StringBuffer();
		
		try {
			int status = 0;
			URL url = new URL(this.URLAPI + this.APIKey); //Se concatena la URL a la API y la clave para acceder a esta
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST"); //Type POST
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			OutputStream os = connection.getOutputStream();
            os.write(jsonString.getBytes("UTF-8"));
            os.close();
			
			status = connection.getResponseCode();
			if(status > 299) 
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			else 
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			while((line = reader.readLine()) != null) 
				response.append(line);
			
			reader.close();
			
			hasError = new JSONObject(response.toString()).has("error"); //Se valida si la respuesta contiene la clave "error"
			if (!hasError) { //En caso que no contenga la error se genera un objeto de "confianza".
				
				//Se fuerza una autoridad, al objeto de confianza, para no tener problemas en la autenticación del usuario
				List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				authorities.add(new SimpleGrantedAuthority("USER"));
				
				return new UsernamePasswordAuthenticationToken(email, password, authorities);
			}
			
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		
		throw new BadCredentialsException("Credenciales erroneas");
	}

}
