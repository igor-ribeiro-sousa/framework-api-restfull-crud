package com.crud.frame.repository;

import org.springframework.stereotype.Repository;

import com.crud.frame.entidade.Produto;

@Repository
public interface ProdutoRepository extends GenericRepository<Produto, Long> {}
