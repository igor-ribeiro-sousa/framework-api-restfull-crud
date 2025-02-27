package com.crud.framework.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crud.framework.response.ResponseAPI;
import com.crud.framework.service.GenericService;

public abstract class GenericController<T, D, ID> {
	
	@Autowired
    protected GenericService<T, D, ID> service;
	
	public GenericController(GenericService<T, D, ID> service) {
        this.service = service;
    }

	@GetMapping
	@SuppressWarnings("unchecked")
    public final ResponseEntity<ResponseAPI<D>> pesquisar() {
    	ResponseAPI<D> response = new ResponseAPI<>();
    	
    	try {
    		List<D> data = service.pesquisar();
    		
    		response.setData((D) data);
    		
    		return ResponseEntity.ok(response);
    		
    	} catch(Exception e) {
    		response.getErrors().add(e.getMessage());
    		return ResponseEntity.badRequest().body(response);
    	}
    }

    @GetMapping("/{id}")
    public final ResponseEntity<ResponseAPI<D>> pesquisarPorId(@PathVariable("id") ID id) {
	    ResponseAPI<D> response = new ResponseAPI<>();
	    
	    try {
	    	 D data = service.pesquisarPorId(id);
	         
	         if (Objects.isNull(data)) {
	             response.getErrors().add("Registro não encontrado.");
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	             
	         }
	         response.setData(data);
	         
	         return ResponseEntity.ok(response);
	         
	    } catch (Exception e) {
	        response.getErrors().add(e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	}

    @PostMapping
    public final ResponseEntity<ResponseAPI<D>> inserir(@RequestBody D entityDTO) {
        ResponseAPI<D> response = new ResponseAPI<>();
        
        try {
             D data = service.inserir(entityDTO);
             
             response.setData(data);
             
             return ResponseEntity.ok(response);
             
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("{id}")
	public final ResponseEntity<ResponseAPI<D>> alterar(@PathVariable ID id, @RequestBody D entityDTO) {
	    ResponseAPI<D> response = new ResponseAPI<>();
	    
	    try {
	        D data = service.alterar(id, entityDTO);
	        
            response.setData(data);
            
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
