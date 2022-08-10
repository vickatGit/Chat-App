package com.example.login_and._signup_backend.DAO;

import com.example.login_and._signup_backend.Entities.User;
import com.example.login_and._signup_backend.Repository.UserRepo;

import java.net.PasswordAuthentication;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDAO {
    @Autowired
    UserRepo userRepo;

    public Integer addUser(User user){
        User newUser=userRepo.save(user);
        System.out.println("\n -------------the Id of a user is --"+newUser.getUserId()+"------------\n");
        return newUser.getUserId();
    }

	public User isUserhasAccount(User user) {
//		List<User> isUser = userRepo.findByUsername(user.getUsername());
		user = userRepo.findByUsername(user.getUsername());
//		return isUser.get(0);
		return user;
		
	}
}
