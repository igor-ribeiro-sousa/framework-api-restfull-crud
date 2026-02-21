package com.crud.framework.service;

import java.util.List;

public interface GenericService<T, ID> {
    
	List<T> pesquisar();
	
	T pesquisarPorId(ID id);
	
	T inserir(T entityDTO);
    
	T alterar(ID id, T entityDTO);

	void deletar(ID id);

}