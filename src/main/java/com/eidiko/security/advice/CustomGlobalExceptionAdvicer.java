package com.eidiko.security.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.eidiko.security.exception.InvalidCredentialsException;

@ControllerAdvice
public class CustomGlobalExceptionAdvicer //extends ResponseEntityExceptionHandler
{
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<String> invalidCredentials(){
		
		return new ResponseEntity<String>("Invalid Credentials.", HttpStatus.NOT_FOUND);
	}
	
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> inputArgumentNotValidException(MethodArgumentNotValidException ex){
		Map<String, String> inputErrorMap=new HashMap<String, String>();
		ex.getBindingResult().getAllErrors().forEach((errors)->{
			String fields=((FieldError)errors).getField();
			String errMsg=errors.getDefaultMessage();
			inputErrorMap.put(fields, errMsg);
		});
		return new ResponseEntity<Map<String, String>>(inputErrorMap, HttpStatus.BAD_REQUEST);
	}
}
