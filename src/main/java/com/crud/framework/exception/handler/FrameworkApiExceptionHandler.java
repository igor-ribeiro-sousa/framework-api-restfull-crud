package com.crud.framework.exception.handler;


import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.crud.framework.exception.FrameworkApiException;
import com.crud.framework.exception.FrameworkValidadorException;

/**
 * @author igor-ribeiro-sousa
 *
 */
@ControllerAdvice
public class FrameworkApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<FrameworkApiException> validationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {

		FrameworkValidadorException errors = new FrameworkValidadorException(
				System.currentTimeMillis(),
				HttpStatus.BAD_REQUEST.value(), 
				"Validation error", 
				"Erro na validação dos campos",
				request.getRequestURI());

		for (FieldError x : ex.getBindingResult().getFieldErrors()) {
			errors.addErrors(x.getField(), x.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

}
