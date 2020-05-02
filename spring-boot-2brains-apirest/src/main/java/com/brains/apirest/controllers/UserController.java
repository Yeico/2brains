package com.brains.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brains.apirest.model.User;
import com.brains.apirest.services.interfaces.IUserService;

/**
 * 
 * @author Sergi
 * API para la obtención de usuarios
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:4200"})
public class UserController {

	/**
	 * Inyección para el uso de los métodos relacionados con la obtención de usuarios.
	 */
	@Autowired
	private IUserService _userService;
	
	/**
	 * Obtiene un listao de usuarios.
	 * @return Retorna un tipo: List<User>
	 */
	@GetMapping("users")
	public List<User> index(){
		return this._userService.get();
	}
	
}
