package com.crud.framework.exception.handler;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.crud.framework.exception.FrameworkApiException;
import com.crud.framework.exception.FrameworkValidadorException;

@RestControllerAdvice
public class FrameworkApiExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FrameworkApiExceptionHandler.class);

    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @ExceptionHandler({ 
    	BindException.class, 
    	MethodArgumentNotValidException.class 
    })
    public ResponseEntity<FrameworkApiException> exceptionValidacao(Exception ex, HttpServletRequest request) {

        BindingResult br;
        
        if (ex instanceof MethodArgumentNotValidException) {
            br = ((MethodArgumentNotValidException) ex).getBindingResult();
        } else {
            br = ((BindException) ex).getBindingResult();
        }
        
        String codigoErro = logarException(HttpStatus.BAD_REQUEST, "Validation error", ex, request);

        FrameworkValidadorException errors = montarFrameworkValidadorExceptionBody(HttpStatus.BAD_REQUEST, "Validation error", "Erro na validação dos campos. Informe o código: " + codigoErro, request);

        for (FieldError x : br.getFieldErrors()) {
            errors.addErrors(x.getField(), x.getDefaultMessage());
        }

        return montarRespostaException(HttpStatus.BAD_REQUEST, errors);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<FrameworkApiException> exceotionIntegridade(DataIntegrityViolationException ex, HttpServletRequest request) {
    	
    	String codigoErro = logarException(HttpStatus.CONFLICT, "Data integrity violation", ex, request);

    	return montarRespostaException(HttpStatus.CONFLICT, "Data integrity violation", "Operação não pôde ser concluída por violação de integridade. Informe o código: " + codigoErro, request);
    }
    
    @ExceptionHandler({
    	HttpMessageNotReadableException.class,
    	MissingRequestHeaderException.class,
    	MissingServletRequestParameterException.class,
    	MethodArgumentTypeMismatchException.class,
    	HttpRequestMethodNotSupportedException.class,
    	HttpMediaTypeNotSupportedException.class,
    	HttpMediaTypeNotAcceptableException.class
    })
    public ResponseEntity<FrameworkApiException> exception4xx(Exception ex, HttpServletRequest request) {
    	
    	String codigoErro = logarException(HttpStatus.BAD_REQUEST, "Requisição inválida", ex, request);
    	
    	return montarRespostaException(HttpStatus.BAD_REQUEST, "Requisição inválida.", "Informe o código: " + codigoErro, request);
    }
    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FrameworkApiException> exception5xx(Exception ex, HttpServletRequest request) {
		
    	String codigoErro = logarException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno", ex, request);

		return montarRespostaException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "Erro inesperado. Informe o código: " + codigoErro, request);
    }
    
    
    private String logarException(HttpStatus status, String titulo, Exception ex, HttpServletRequest request) {

    	String codigoErro = UUID.randomUUID().toString();

        String uri        = request.getRequestURI();
        String query      = request.getQueryString();
        String fullPath   = (query == null) ? uri : (uri + "?" + query);
        String usuario    = (request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : "USUARIO_PADRAO";
        String metodo 	  = request.getMethod();
        String enderecoIP = request.getRemoteAddr();

        StringBuilder sb = new StringBuilder();

        sb.append("=======================\n")
          .append("ERRO: ").append(titulo).append("\n")
          .append("errorId: ").append(codigoErro).append("\n")
          .append("método: ").append(metodo).append("\n")
          .append("path: ").append(fullPath).append("\n")
          .append("status: ")
              .append(status.value())
              .append(" ")
              .append(status.name())
              .append("\n")
          .append("endereço IP: ").append(enderecoIP).append("\n")
          .append("usuário: ").append(usuario).append("\n")
          .append("exception: ").append(ex.getClass().getName()).append("\n")
          .append("message: ")
              .append(ex.getMessage() != null ? ex.getMessage() : "-")
              .append("\n")
          .append("=======================");

        String body = sb.toString();

        LOG.error("\n{}", body);
        LOG.error("TRACE | errorId={}", codigoErro, ex);

        return codigoErro;
    }
    
    
    private OffsetDateTime now() {
        return OffsetDateTime.now(ZONE);
    }
    
    private FrameworkValidadorException montarFrameworkValidadorExceptionBody(HttpStatus status, String error, String message, HttpServletRequest request) {
		
    	OffsetDateTime n = now();
    	
    	return new FrameworkValidadorException(
    			n.toInstant().toEpochMilli(),
        		n.format(FMT),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );
	}

    private FrameworkApiException montarFrameworkApiExceptionBody(HttpStatus status, String error, String message, HttpServletRequest request) {
        
    	OffsetDateTime n = now();
    	
        return new FrameworkApiException(
        		n.toInstant().toEpochMilli(),
        		n.format(FMT),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );
    }

    private ResponseEntity<FrameworkApiException> montarRespostaException(HttpStatus status, FrameworkApiException body) {
        return ResponseEntity.status(status).body(body);
    }

    private ResponseEntity<FrameworkApiException> montarRespostaException(HttpStatus status, String error, String message, HttpServletRequest request) {
        return montarRespostaException(status, montarFrameworkApiExceptionBody(status, error, message, request));
    }
}