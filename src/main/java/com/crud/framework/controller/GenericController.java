package com.crud.framework.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crud.framework.response.ResponseAPI;
import com.crud.framework.service.GenericService;

public abstract class GenericController<T, ID> {
	
	protected final GenericService<T, ID> service;

	public GenericController(GenericService<T, ID> service) {
	    this.service = service;
	}

	@GetMapping
	@SuppressWarnings("unchecked")
    public final ResponseEntity<ResponseAPI<T>> pesquisar() {
    	ResponseAPI<T> response = new ResponseAPI<>();
    	try {
    		List<T> data = service.pesquisar();
    		response.setData((T) data);
    		return ResponseEntity.ok(response);
    	} catch(Exception e) {
    		response.getErrors().add(e.getMessage());
    		return ResponseEntity.badRequest().body(response);
    	}
    }

    @GetMapping("/{id}")
    public final ResponseEntity<ResponseAPI<T>> pesquisarPorId(@PathVariable("id") ID id) {
	    ResponseAPI<T> response = new ResponseAPI<>();
	    try {
	        T data = service.pesquisarPorId(id);
	        response.setData((T) data);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.getErrors().add(e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	}

    @PostMapping
    public final ResponseEntity<ResponseAPI<T>> inserir(@RequestBody T entity) {
        ResponseAPI<T> response = new ResponseAPI<>();
        try {
            T data = service.inserir(entity);
            response.setData((T) data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("{id}")
	public final ResponseEntity<ResponseAPI<T>> alterar(@PathVariable ID id, @RequestBody T entity) {
	    ResponseAPI<T> response = new ResponseAPI<>();
	    try {
	        T data = service.alterar(id, entity);
	        response.setData((T) data);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.getErrors().add(e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	}

    @DeleteMapping("/{id}")
    public final ResponseEntity<ResponseAPI<Void>> deletar(@PathVariable("id") ID id) {
	    ResponseAPI<Void> response = new ResponseAPI<>();
	    try {
	        service.deletar(id);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.getErrors().add(e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	}
}
