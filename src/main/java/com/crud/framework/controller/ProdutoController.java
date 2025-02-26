package com.crud.framework.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.framework.dto.ProdutoDTO;
import com.crud.framework.entidade.Produto;
import com.crud.framework.service.impl.ProdutoServiceImpl;

@RestController
@RequestMapping("/api/produtos")
class ProdutoController extends GenericController<Produto, ProdutoDTO, Long> {

	public ProdutoController() {
        super(Produto.class, ProdutoDTO.class);
    }
}
