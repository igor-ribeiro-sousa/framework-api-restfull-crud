package com.crud.framework.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author igor-ribeiro-sousa
 *
 */
public class FrameworkValidadorException extends FrameworkApiException {

	private static final long serialVersionUID = 1L;

	private List<ErroCampo> errors = new ArrayList<>();

	public FrameworkValidadorException() {
		super();
	}

	public FrameworkValidadorException(Long timestamp, String dateTime, Integer status, String error, String message, String path) {
		super(timestamp, dateTime, status, error, message, path);
	}

	public List<ErroCampo> getErrors() {
		return errors;
	}

	public void addErrors(String nomeCampo, String mensagem) {
		this.errors.add(new ErroCampo(nomeCampo, mensagem));
	}
}
