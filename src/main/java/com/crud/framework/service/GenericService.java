package com.crud.framework.service;

import java.util.List;

public interface GenericService<T, ID> {
    
	List<T> pesquisar();
	
	T pesquisarPorId(ID id);
	
	T inserir(T entity);
    
	T alterar(ID id, T entity);

	void deletar(ID id);

}