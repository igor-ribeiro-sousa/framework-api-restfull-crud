package com.crud.frame.service;

import org.springframework.stereotype.Service;

import com.crud.frame.entidade.Produto;
import com.crud.frame.repository.ProdutoRepository;

@Service
public class ProdutoService extends GenericService<Produto, Long> {
	
    public ProdutoService(ProdutoRepository repository) {
        super(repository);
    }
}
