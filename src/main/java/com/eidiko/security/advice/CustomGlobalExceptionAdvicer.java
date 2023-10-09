package com.eidiko.security.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.eidiko.security.exception.InvalidCredentialsException;

@ControllerAdvice
public class CustomGlobalExceptionAdvicer extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<String> invalidCredentials(){
		
		return new ResponseEntity<String>("Invalid Credentials.", HttpStatus.NOT_FOUND);
	}
	
	
	

}
