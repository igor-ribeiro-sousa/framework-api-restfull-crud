package com.crud.framework.service.impl;

import org.springframework.stereotype.Service;

import com.crud.framework.entidade.Produto;
import com.crud.framework.repository.ProdutoRepository;

@Service
public class ProdutoServiceImpl extends GenericServiceImpl<Produto, Long> {
	
    public ProdutoServiceImpl(ProdutoRepository repository) {
        super(repository);
    }
}
