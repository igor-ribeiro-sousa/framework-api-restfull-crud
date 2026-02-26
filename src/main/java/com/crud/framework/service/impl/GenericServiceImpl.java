package com.crud.framework.service.impl;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Id;

import org.springframework.beans.BeanUtils;

import com.crud.framework.repository.GenericRepository;
import com.crud.framework.service.GenericService;


public abstract class GenericServiceImpl<T, ID> implements GenericService<T, ID> {
	
    protected final GenericRepository<T, ID> repository;

    public GenericServiceImpl(GenericRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> pesquisar() {
    	try {
    		List<T> resultados = repository.findAll();
    		
    		completarPosPesquisar(resultados);
    		
    		return resultados;
    		
    	} catch(Exception e) {
			throw new RuntimeException("Erro ao pesquisar: " + e.getMessage(), e);
			
    	}
    }
    
    @Override
	public T pesquisarPorId(ID id) {
		T resultado = repository.findById(id).orElseThrow(() -> new RuntimeException("Entidade não encontrada"));
		
		completarPosPesquisarPorId(resultado);
		
		return resultado;
	}

    @Override
	public T inserir(T entity) {
		if (!validarInserir(entity)) {
			throw new RuntimeException("Falha na validação da inserção.");
			
		}
		try {
			completarInserir(entity);
			
			T resultado = repository.save(entity);
			
			completarPosInserir(resultado);
			
			return resultado;
			
		} catch (Exception e) {
			throw new RuntimeException("Erro ao inserir: " + e.getMessage(), e);
			
		}
	}
    
    @Override
	public T alterar(ID id, T entity) {
		if (!validarAlterar(entity)) {
			throw new RuntimeException("Falha na validação da alteração.");
			
		}
		try {
			completarAlterar(entity);
			
			T entityBD = repository.findById(id).orElseThrow(() -> new RuntimeException("Entidade não encontrada"));
			
			BeanUtils.copyProperties(entity, entityBD, obterNomeDoIdentificador(entity.getClass()));
			
			T resultado = repository.save(entityBD);
			
			completarPosAlterar(resultado);
			
			return resultado;
			
		} catch (Exception e) {
			throw new RuntimeException("Erro ao alterar a entidade: " + e.getMessage(), e);
			
		}
	}
    
    @Override
   	public void deletar(ID id) {
   		try {
   			T entidade = repository.findById(id).orElseThrow(() -> new RuntimeException("Entidade não encontrada para exclusão"));
   			
   			repository.delete(entidade);
   			
   		} catch (Exception e) {
   			throw new RuntimeException("Erro ao excluir a entidade: " + e.getMessage(), e);
   		}
   	}

	private String obterNomeDoIdentificador(Class<?> entityClass) {
		for (Field field : entityClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class)) {
				return field.getName();
				
			}
		}
		throw new RuntimeException("Nenhum identificador @Id encontrado na entidade " + entityClass.getSimpleName());
	}

    protected boolean validarInserir(T entityDTO) { return true; }

	protected boolean validarAlterar(T entityDTO) { return true; }

	protected void completarInserir(T entity) { }

	protected void completarAlterar(T entity) { }

	protected void completarPosInserir(T entityDTO) { }

	protected void completarPosAlterar(T entityDTO) { }

	protected void completarPosPesquisar(List<T> entityDTO) { }

	protected void completarPosPesquisarPorId(T entityDTO) { }

}
