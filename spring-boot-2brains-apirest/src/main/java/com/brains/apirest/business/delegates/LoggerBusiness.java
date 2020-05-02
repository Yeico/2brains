package com.brains.apirest.business.delegates;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.brains.apirest.business.interfaces.ILoggerBusiness;

/**
 * 
 * @author Sergi
 *
 * Clase que expone métodos para guardar objetos serializados en disco.
 * @param <T> Clase genérica.
 */
@Component
public class LoggerBusiness<T> implements ILoggerBusiness<T> {

	//Nombre del documento donde se guardan los datos de la clase serializable
	@Value("${logger.fileName}")
	private String fileName;

	/**
	 * Guarda una clase serializable en archivo 'txt', en disco.
	 */
	@Override
	public void save(T t) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(this.fileName);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
	    objectOutputStream.writeObject(t); //Se escribe el objeto
	    objectOutputStream.flush();
	    objectOutputStream.close();
	    
	    System.out.println(t.toString());
	}
	
}
