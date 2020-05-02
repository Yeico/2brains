package com.brains.apirest.model;

import java.io.Serializable;

/**
 * DTO del usuario.
 * Clase serializable
 * @author Sergi
 *
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private int age = 0;
	private String gender = new String("");
	private String name = new String("");
	private String surname = new String("");
	private String cell = new String("");
	private String email = new String("");
	private String image = new String("");
	
	/**
	 * Constructor de la clase usuario
	 * @param id -> Identificador
	 * @param age -> Edad
	 * @param gender -> Sexo
	 * @param name -> Nombre
	 * @param surname -> Apellido
	 * @param cell -> Número de móvil
	 * @param email -> Correo electrónico
	 * @param image -> Ruta web de la imagen
	 */
	public User(int id, int age, String gender, String name, String surname, String cell, String email, String image) {
		this.id = id;
		this.age = age;
		this.gender = gender;
		this.name = name;
		this.surname = surname;
		this.cell = cell;
		this.email = email;
		this.image = image;
	}
	
	/**
	 * Obtiene el identificador del usuario
	 * @return Retorna un tipo: int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Obtiene la edad del usuario
	 * @return Retorna un tipo: int
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Obtiene el sexo del usuario
	 * @return Retorna un tipo: String
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Obtiene el nombre del usuario
	 * @return Retorna un tipo: String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtiene el apellido del usuario
	 * @return Retorna un tipo: String
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Obtiene el número de móvil del usuario
	 * @return Retorna un tipo: String
	 */
	public String getCell() {
		return cell;
	}

	/**
	 * Obtiene el correo electrónico del usuario
	 * @return Retorna un tipo: String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Obtiene imagen del usuario
	 * @return Retorna un tipo: String
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Sobreescritura del método String de la clase Object
	 * @return Retorna un tipo: String
	 */
	@Override
	public String toString() {
		return "El usuario de nombre " + this.name + " y apellido " + this.surname + " ha generado un error en la consulta";
	}
	
}
