package com.brains.apirest.business.delegates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.brains.apirest.business.interfaces.IUserBusiness;
import com.brains.apirest.model.User;

/**
 * 
 * @author Sergi
 * Clase que obtiene los usuarios a través del consumo de la API RandomUser 
 */
@Component
public class UserBusiness implements IUserBusiness {
	
	private static HttpURLConnection connection;
	
	//Dirección URL para el consumir la API de RandomUser
	@Value("${users.randomAPIUrl}")
	private String urlAPI;
	
	/**
	 * Obtiene una lista de usuarios de la API RandomUser. La cantidad de resultados devueltos es pasado por parámetro.
	 * En caso de producirse un error en la obtención de los usuarios se retorna una lista de usuario vacía.
	 * @param totalRquest Número de usuaris a retornar en la lista.
	 * @return Retorna un tipo: List<User>
	 */
	@Override
	public List<User> get(int totalRquest) {		
		
		String line = new String("");
		BufferedReader reader = null;
		StringBuffer response = new StringBuffer();
		
		try {
			int status = 0;
			URL url = new URL(this.urlAPI + totalRquest); //Se concatena la URL con el número total de elementos a retornar.
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET"); //El llamado es tipo GET
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			status = connection.getResponseCode();
			if(status > 299) 
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			else 
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			while((line = reader.readLine()) != null) 
				response.append(line);	
			
			reader.close();
			return Parse(response.toString()); //Se parsean y retorna el resultado.
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return new ArrayList<User>();
	}
	
	/**
	 * Clase que parse al tipo: List<User>, un String que en su interior alberga un JSON.
	 * @param reponseBody Cadena con el JSON a parsear.
	 * @return Retorna un tipo: List<User>
	 */
	private static List<User> Parse(String reponseBody) {
		List<User> users = new ArrayList<User>();
		JSONObject jsonObject = new JSONObject(reponseBody);
		
		JSONArray userArrayJsonResult = jsonObject.getJSONArray("results");		
		for(int i = 0; i < userArrayJsonResult.length(); i++) {
			JSONObject userObjectJson = userArrayJsonResult.getJSONObject(i);
			users.add(
				new User(
					i,
					userObjectJson.getJSONObject("dob").getInt("age"),
					userObjectJson.getString("gender"),
					userObjectJson.getJSONObject("name").getString("first"),
					userObjectJson.getJSONObject("name").getString("last"),
					userObjectJson.getString("cell"),
					userObjectJson.getString("email"),
					userObjectJson.getJSONObject("picture").getString("large")
				)
			);
		}
		return users;
	}
	
}
