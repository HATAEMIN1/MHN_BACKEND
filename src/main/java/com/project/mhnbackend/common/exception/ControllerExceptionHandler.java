package com.project.mhnbackend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.project.mhnbackend.common.exception.ex.CustomApiException;
import com.project.mhnbackend.common.exception.ex.CustomException;
import com.project.mhnbackend.common.util.Script;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public String exception(CustomException e) {
			return Script.back(e.getMessage());
		}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e) {
		return new ResponseEntity<>(new CMRespDTO<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
}
