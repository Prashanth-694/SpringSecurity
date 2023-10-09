package com.eidiko.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eidiko.security.exception.InvalidCredentialsException;
import com.eidiko.security.model.Users;
import com.eidiko.security.repo.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepo repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		log.info("inside load "+username);
		Users user=null;
		if(username!=null) {
			
			try {
				user= repo.findByEmail(username);
			} catch (Exception e) {
			e.printStackTrace();
			}
		}
		log.info("User : "+user.getEmail());
		return user;
	}
	
	public Users UserByEmail(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		log.info("inside UserByEmail() "+email);
		Users user=null;
		if(email!=null) {
			
			try {
				user= repo.findByEmail(email);
			} catch (Exception e) {
				throw new InvalidCredentialsException();
			}
		}
		log.info("Users details : "+user.getEmail());
		return user;
	}
	
}
