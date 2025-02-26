package com.crud.framework.service;

import org.springframework.stereotype.Service;

import com.crud.framework.entidade.Produto;
import com.crud.framework.repository.ProdutoRepository;

@Service
public class ProdutoService extends GenericService<Produto, Long> {
	
    public ProdutoService(ProdutoRepository repository) {
        super(repository);
    }
}
