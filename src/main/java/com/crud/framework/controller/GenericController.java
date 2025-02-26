package com.crud.framework.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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
import com.crud.framework.service.impl.GenericServiceImpl;

public abstract class GenericController<T, D, ID> {
	
	@Autowired
    protected GenericServiceImpl<T, ID> service;

    @Autowired
    protected ModelMapper modelMapper;

    private final Class<T> entityClass;
    private final Class<D> dtoClass;

    protected GenericController(Class<T> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

	@GetMapping
	@SuppressWarnings("unchecked")
    public final ResponseEntity<ResponseAPI<D>> pesquisar() {
    	ResponseAPI<D> response = new ResponseAPI<>();
    	try {
    		List<T> data = service.pesquisar();
    		List<D> dataResponse = data.stream().map(entity -> modelMapper.map(entity, dtoClass)).collect(Collectors.toList());
    		response.setData((D) dataResponse);
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
	    	 T entity = service.pesquisarPorId(id);
	         
	         if (entity == null) {
	             response.getErrors().add("Registro não encontrado.");
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	         }

	         D dataResponse = modelMapper.map(entity, dtoClass);
	         response.setData(dataResponse);
	         return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        response.getErrors().add(e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	}

    @PostMapping
    public final ResponseEntity<ResponseAPI<D>> inserir(@RequestBody T dto) {
        ResponseAPI<D> response = new ResponseAPI<>();
        try {
        	 T entity = modelMapper.map(dto, entityClass);
             T savedEntity = service.inserir(entity);
             D dataResponse = modelMapper.map(savedEntity, dtoClass);
             response.setData(dataResponse);
             return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("{id}")
	public final ResponseEntity<ResponseAPI<D>> alterar(@PathVariable ID id, @RequestBody T dto) {
	    ResponseAPI<D> response = new ResponseAPI<>();
	    try {
	    	T entity = modelMapper.map(dto, entityClass);
	        T savedEntity = service.alterar(id, entity);
            D dataResponse = modelMapper.map(savedEntity, dtoClass);
            response.setData(dataResponse);
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
