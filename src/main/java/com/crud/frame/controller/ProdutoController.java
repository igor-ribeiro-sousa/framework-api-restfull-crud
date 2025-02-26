package com.crud.frame.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.frame.entidade.Produto;
import com.crud.frame.service.ProdutoService;

@RestController
@RequestMapping("/api/produtos")
class ProdutoController extends GenericController<Produto, Long> {

	public ProdutoController(ProdutoService service) {
        super(service);
    }
}
