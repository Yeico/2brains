package com.brains.apirest.business.interfaces;

import java.io.IOException;

/**
 * 
 * @author Sergi
 *
 * Clase que expone métodos para guardar objetos serializados en disco.
 * @param <T> Clase genérica.
 */
public interface ILoggerBusiness<T> {

	/**
	 * Guarda una clase serializable en archivo 'txt', en disco.
	 */
	public void save(T t) throws IOException;
	
}
