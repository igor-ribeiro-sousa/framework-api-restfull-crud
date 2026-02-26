package com.crud.framework.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crud.framework.config.GenericMapper;
import com.crud.framework.response.ResponseAPI;
import com.crud.framework.service.GenericService;

public abstract class GenericController<T, D, ID> {

	protected final GenericService<T, ID> service;
	protected final GenericMapper genericMapper;
	
	private final Class<T> entityClass;
    private final Class<D> dtoClass;

	public GenericController(GenericService<T, ID> service, GenericMapper genericMapper, Class<T> entityClass, Class<D> dtoClass) {
        this.service = service;
        this.genericMapper = genericMapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }
	
	@GetMapping
	public final ResponseEntity<ResponseAPI<List<D>>> pesquisar() {

		List<T> entities = service.pesquisar();
		
		List<D> resultados = genericMapper.convertListEntityToListDTO(entities, dtoClass);

		ResponseAPI<List<D>> response = new ResponseAPI<>();
		response.setData(resultados);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public final ResponseEntity<ResponseAPI<D>> pesquisarPorId(@PathVariable("id") ID id) {

		T entidade = service.pesquisarPorId(id);

		D resultado = genericMapper.convertToDTO(entidade, dtoClass);
		
		ResponseAPI<D> response = new ResponseAPI<>();
		response.setData(resultado);

		return ResponseEntity.ok(response);
		
	}

	@PostMapping
	public final ResponseEntity<ResponseAPI<D>> inserir(@RequestBody D entityDTO) {
		
		T entidade = genericMapper.convertToEntity(entityDTO, entityClass);
		
		T resultado = service.inserir(entidade);

		D resultadoDTO = genericMapper.convertToDTO(resultado, dtoClass);

		ResponseAPI<D> response = new ResponseAPI<>();
		response.setData(resultadoDTO);

		return ResponseEntity.ok(response);
		
	}

	@PutMapping("{id}")
	public final ResponseEntity<ResponseAPI<D>> alterar(@PathVariable ID id, @RequestBody D entityDTO) {
		
		T entidade = genericMapper.convertToEntity(entityDTO, entityClass);

		T resultado = service.alterar(id, entidade);

		D resultadoDTO = genericMapper.convertToDTO(resultado, dtoClass);
		
		ResponseAPI<D> response = new ResponseAPI<>();
		response.setData(resultadoDTO);

		return ResponseEntity.ok(response);
		
	}

	@DeleteMapping("/{id}")
	public final ResponseEntity<ResponseAPI<Void>> deletar(@PathVariable("id") ID id) {
		
		service.deletar(id);

		ResponseAPI<Void> response = new ResponseAPI<>();
		return ResponseEntity.ok(response);
		
	}
}
