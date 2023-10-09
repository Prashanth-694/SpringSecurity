package com.eidiko.security.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.eidiko.security.model.Employee;
import com.eidiko.security.model.RefreshToken;
import com.eidiko.security.model.Users;
import com.eidiko.security.repo.EmployeeRepo;
import com.eidiko.security.repo.RefreshTokenRepo;
import com.eidiko.security.repo.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RefreshTokenService {
	
	@Autowired
	private RefreshTokenRepo refreshTokenRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	public RefreshToken createRefreshToken(Users users) {
		log.info("inside createRefreshToken "+users.getUsername());
		//Users users=userRepo.findByEmail("MP");
		log.info("inside createRefreshToken users "+users.getRefreshToken());
	//	Users users=userRepo.findById(1).get();
		RefreshToken refreshToken=users.getRefreshToken();
		log.info("Ref Token :"+refreshToken);
		if(refreshToken==null) {
			 refreshToken=RefreshToken.builder().refreshToken(UUID.randomUUID().toString())
					.expiry(Instant.now().plusMillis(2*60*1000))
			.users(users)
			.build();
		}else {
			 refreshToken.setExpiry(Instant.now().plusMillis(2*60*1000));
		}
		
		RefreshToken t = refreshTokenRepo.save(refreshToken);
		
		//users.setRefreshToken(t);
		//userRepo.save(users);
		return refreshToken;
	}
	
public RefreshToken verifyRefreshToken(String refreshToken) {
		
	RefreshToken refreshTokenObj=refreshTokenRepo.findByRefreshToken(refreshToken);
	log.info("RefreshToken "+refreshTokenObj);
	if(refreshTokenObj.getExpiry().compareTo(Instant.now())<0) {
		refreshTokenRepo.delete(refreshTokenObj);
		throw new RuntimeException("refresh token has been expired.");
	}else {
		return refreshTokenObj;
	}
	}

//public Employee getEmployee(int id) {
//	employeeRepo.findById(1).get();
//}

}
