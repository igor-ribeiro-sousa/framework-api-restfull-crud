package com.crud.framework.repository;

import org.springframework.stereotype.Repository;

import com.crud.framework.entidade.Produto;

@Repository
public interface ProdutoRepository extends GenericRepository<Produto, Long> {}
