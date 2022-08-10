package com.example.login_and._signup_backend.Controller;

import com.example.login_and._signup_backend.DAO.UserDAO;
import com.example.login_and._signup_backend.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	@Autowired
	UserDAO userDAO;
	
	@PostMapping("/Users/addUser")
	public int addUser(@RequestBody User user) {
		System.out.println(user.toString());
		return userDAO.addUser(user);
	}
	
	@PostMapping("/Users/isUser")
	public User isUserHasAccount(@RequestBody User user) {
		System.out.println(user.toString());
		return userDAO.isUserhasAccount(user);
	}

}
