package com.crud.framework.service.impl;

import org.springframework.stereotype.Service;

import com.crud.framework.config.GenericMapper;
import com.crud.framework.dto.ProdutoDTO;
import com.crud.framework.entidade.Produto;
import com.crud.framework.repository.ProdutoRepository;
import com.crud.framework.service.ProdutoService;

@Service
public class ProdutoServiceImpl extends GenericServiceImpl<Produto, ProdutoDTO, Long> implements ProdutoService {
	
    public ProdutoServiceImpl(ProdutoRepository repository, GenericMapper genericMapper) {
        super(repository, genericMapper, Produto.class, ProdutoDTO.class);
    }
}
