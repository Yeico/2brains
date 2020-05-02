package com.brains.apirest.business.interfaces;

import java.util.List;

import com.brains.apirest.model.User;

public interface IUserBusiness {
	
	/**
	 * Obtiene una lista de usuarios de la API RandomUser. La cantidad de resultados devueltos es pasado por parámetro.
	 * En caso de producirse un error en la obtención de los usuarios se retorna una lista de usuario vacía.
	 * @param totalRquest Número de usuaris a retornar en la lista.
	 * @return Retorna un tipo: List<User>
	 */
	public List<User> get(int totalRequest);
}
