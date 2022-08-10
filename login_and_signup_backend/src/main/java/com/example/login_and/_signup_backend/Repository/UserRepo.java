package com.example.login_and._signup_backend.Repository;

import com.example.login_and._signup_backend.Entities.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	
	@Query( value = "SELECT * FROM User u WHERE u.username=:username",nativeQuery = true)	
	public User findByUsername(@Param("username") String username);

}
