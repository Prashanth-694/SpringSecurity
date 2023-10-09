package com.eidiko.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eidiko.security.model.Users;
import com.eidiko.security.repo.UserRepo;

@Service
public class UserServiceSecurity {
	
	@Autowired
	private UserRepo repo;

	public List<Users> userList = new ArrayList<Users>();

	public Users insertUser(Users users) {
		return repo.save(users);
	}
	

	public List<Users> getUsers() {
		return repo.findAll();
	}
	
	public Users getUserById(int id) {
		return repo.findById(id).get();
	}

}
