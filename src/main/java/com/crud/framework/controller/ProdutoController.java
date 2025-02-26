package com.crud.framework.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.framework.entidade.Produto;
import com.crud.framework.service.ProdutoService;

@RestController
@RequestMapping("/api/produtos")
class ProdutoController extends GenericController<Produto, Long> {

	public ProdutoController(ProdutoService service) {
        super(service);
    }
}
