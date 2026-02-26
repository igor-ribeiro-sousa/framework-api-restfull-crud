package com.crud.framework.exception;

import java.io.Serializable;

/**
 * 
 * @author igor-ribeiro-sousa
 *
 */
public class ErroCampo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nomeCampo;
	private String mensagemErro;

	public ErroCampo() { }

	public ErroCampo(String nomeCampo, String mensagemErro) {
		this.nomeCampo = nomeCampo;
		this.mensagemErro = mensagemErro;
	}

	public String getNomeCampo() {
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}
}
