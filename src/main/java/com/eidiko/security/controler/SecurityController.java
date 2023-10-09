package com.eidiko.security.controler;

import java.security.Principal;
import java.util.List;

import org.aspectj.weaver.ast.HasAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eidiko.security.bo.JwtRequest;
import com.eidiko.security.bo.JwtResponse;
import com.eidiko.security.model.Users;
import com.eidiko.security.service.UserServiceSecurity;
import com.eidiko.security.util.JwtHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class SecurityController {

	@Autowired
	private UserServiceSecurity userServiceSecurity;

	@GetMapping("/getAllUsers")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<List<Users>> getAllUser(Principal principal) {
		log.info("inside getAllUsers()" + principal.getName());

		return new ResponseEntity<List<Users>>(userServiceSecurity.getUsers(), HttpStatus.OK);
	}

	@GetMapping("/getUser")

	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<String> getUser(Principal principal) {
		log.info("inside getUser() " + principal.getName());

		return new ResponseEntity<String>("ROLE_USER", HttpStatus.OK);
	}

	@GetMapping("/getUserById/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Users> getUserById(@PathVariable int id, Principal principal) {
		log.info("inside getUserById() " + principal.getName());

		return new ResponseEntity<Users>(userServiceSecurity.getUserById(id), HttpStatus.OK);
	}
	
	@GetMapping("/getUserRparam")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Users> getUserReqParam(@RequestParam int id, Principal principal) {
		log.info("inside getUserById() " + principal.getName());

		return new ResponseEntity<Users>(userServiceSecurity.getUserById(id), HttpStatus.OK);
	}
	@GetMapping("/getUserForm")
	@PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Users> getUserFormData(@PathVariable int id, Principal principal) {
		log.info("inside getUserById() " + principal.getName());

		return new ResponseEntity<Users>(userServiceSecurity.getUserById(id), HttpStatus.OK);
	}

	
}
