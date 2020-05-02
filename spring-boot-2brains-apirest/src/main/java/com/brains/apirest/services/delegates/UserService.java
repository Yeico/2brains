package com.brains.apirest.services.delegates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.brains.apirest.business.interfaces.ILoggerBusiness;
import com.brains.apirest.business.interfaces.IUserBusiness;
import com.brains.apirest.model.User;
import com.brains.apirest.services.interfaces.IUserService;

/**
 * Servicio/Componente para la obtención de usuario a través de la API RandomUser
 * @author Sergi
 *
 */
@Service
public class UserService implements IUserService {
	
	//Clave con el total de usuario a recuperar
	@Value("${users.totalRequestApi}")
	private int totalRequest;
	
	/**
	 * Inyección para el uso de los métodos de guardado de loggins.
	 */
	@Autowired
	private ILoggerBusiness<User> _loggerBusiness;
	
	
	/**
	 * Inyección para el uso de los métodos de obtención de usuarios
	 */
	@Autowired
	private IUserBusiness _userBusiness;

	/**
	 * Método que retonar una list ade usuario y simula el error peticiones
	 * @return Retorna un tipo: List<User>
	 */
	@Override
	public List<User> get() {
		this.totalRequest = (this.totalRequest <= 0) ? 1 : this.totalRequest;
		boolean exit = false;
		int totalFail = (this.totalRequest * 10) / 100; //10% de usuarios a descartar
		Random random = new Random();
		List<User> users = new ArrayList<User>();
		
		do {
			boolean mustFail = random.nextBoolean(); //Debe fallar o no la consulta
			
			//Diferencia de usuarios faltantes entre el total y los existentes en la lista
			int difference = Math.abs(this.totalRequest - users.size());
			users.addAll(this._userBusiness.get(difference)); //Recupero la diferencia usuarios
			
			if (mustFail || users.size() < this.totalRequest) { //Valido si debo fallar o si "size" de la lista contiene el total de usuarios
				
				//Valido el número de usuario a "fallar", a sacar del listado, es coherente con el tamaño de la lsita
				if (users.size() >= totalFail) {
					for (int i = 0; i <= totalFail; i++) { //Se repite el número de veces a fallar
						
						try {
							int how = random.nextInt((users.size())); //¿A quién saco? De manera aleatoria
							this._loggerBusiness.save(users.get(how)); //Guardo el usuario que "falló"
							users.remove(how); //Elimino el usuario que "falló"
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
				
			}
			else
				exit = true;
			
		} while (users.size() < totalRequest && exit == false);
		
		return users;
	}
}
