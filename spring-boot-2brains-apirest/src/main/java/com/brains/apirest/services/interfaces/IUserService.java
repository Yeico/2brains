package com.brains.apirest.services.interfaces;

import java.util.List;

import com.brains.apirest.model.User;

public interface IUserService {
	
	/**
	 * Método que retonar una list ade usuario y simula el error peticiones
	 * @return Retorna un tipo: List<User>
	 */
	public List<User> get();
}
