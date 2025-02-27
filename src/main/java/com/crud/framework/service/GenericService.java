package com.crud.framework.service;

import java.util.List;

public interface GenericService<T, D, ID> {
    
	List<D> pesquisar();
	
	D pesquisarPorId(ID id);
	
	D inserir(D entityDTO);
    
	D alterar(ID id, D entityDTO);

	void deletar(ID id);

}