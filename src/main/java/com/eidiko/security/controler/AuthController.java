package com.eidiko.security.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eidiko.security.bo.JwtRequest;
import com.eidiko.security.bo.JwtResponse;
import com.eidiko.security.exception.InvalidCredentialsException;
import com.eidiko.security.model.RefreshToken;
import com.eidiko.security.model.RefreshTokenRequest;
import com.eidiko.security.model.Users;
import com.eidiko.security.repo.RefreshTokenRepo;
import com.eidiko.security.repo.UserRepo;
import com.eidiko.security.service.CustomUserDetailsService;
import com.eidiko.security.service.RefreshTokenService;
import com.eidiko.security.service.UserServiceSecurity;
import com.eidiko.security.util.JwtHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserServiceSecurity userServiceSecurity;

	@Autowired
	AuthenticationManager manager;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private JwtHelper helper;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@GetMapping("/v1/welcome")
	public String welcome() {
		return "Welcome";
	}

	@PostMapping("/v1/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
		log.info("inside login()");
		boolean authFlag = this.doAuthenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
		
		log.info("authFlag " + authFlag);
		
		if(authFlag) {
			
			UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());			
			String token = helper.generateToken(userDetails);
			log.info(token);
			
		
			
			Users findByEmail = customUserDetailsService.UserByEmail(userDetails.getUsername());
			
			RefreshToken refreshToken=refreshTokenService.createRefreshToken(findByEmail);
			log.info(refreshToken.getRefreshToken());
			JwtResponse response = JwtResponse.builder().jwtToken(token).refreshToken(refreshToken.getRefreshToken()).username(userDetails.getUsername()).build();
			return new ResponseEntity<JwtResponse>(response, HttpStatus.OK);
		}else {
			throw new InvalidCredentialsException();
		}
		
	}

	@PostMapping("/v1/register")
	public ResponseEntity<Users> insertUser(@RequestBody Users users) {
		log.info("inside insertUser()");
		users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
		Users user = userServiceSecurity.insertUser(users);
		return new ResponseEntity<Users>(user, HttpStatus.OK);
	}
	
	@PostMapping("/v1/refreshJwtToken")
	public ResponseEntity<JwtResponse> refreshJwtToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		log.info("inside refresgToken()");
		
		RefreshToken refreshToken=refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
		log.info("Re Users :"+refreshToken.getUsers());
		Users users=refreshToken.getUsers();
		
		String token = helper.generateToken(users);
		
		JwtResponse response = JwtResponse.builder().jwtToken(token).refreshToken(refreshToken.getRefreshToken()).username(users.getUsername()).build();
		return new ResponseEntity<JwtResponse>(response, HttpStatus.OK);
	
	}
	
	private boolean doAuthenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);

		try {
			// manager.authenticate(authentication);
			authenticationProvider.authenticate(authentication);
			return true;
		} catch (Exception e) {
			log.info("Credentials Invalid !!");
			
			return false;
		}

	}
}
