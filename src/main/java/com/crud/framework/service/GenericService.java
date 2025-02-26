package com.crud.framework.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;

import com.crud.framework.repository.GenericRepository;

import jakarta.persistence.Id;
import jakarta.transaction.Transactional;

public abstract class GenericService<T, ID> {
	
    protected final GenericRepository<T, ID> repository;
    
    public GenericService(GenericRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Transactional
    public List<T> pesquisar() {
    	try {
    		List<T> result = repository.findAll();
    		completarPosPesquisar(result);
    		return result;
    	} catch(Exception e) {
			throw new RuntimeException("Erro ao pesquisar: " + e.getMessage(), e);
    	}
    }
    
    @Transactional
	public T pesquisarPorId(ID id) {
		T objResponse = repository.findById(id).orElseThrow(() -> new RuntimeException("Entidade não encontrada"));
		completarPosPesquisarPorId(objResponse);
		return objResponse;
	}

    @Transactional
	public T inserir(T entity) {
		try {
			if (!validarInserir(entity)) {
				throw new RuntimeException("Falha na validação da inserção.");
			}
			completarInserir(entity);
			T objResponse = repository.save(entity);
			completarPosInserir(objResponse);
			return objResponse;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao inserir: " + e.getMessage(), e);
		}
	}
    
    @Transactional
	public T alterar(ID id, T entity) {
		try {
			if (!validarAlterar(entity)) {
				throw new RuntimeException("Falha na validação da alteração.");
			}
			completarAlterar(entity);
			
			T entidadeExistente = repository.findById(id).orElseThrow(() -> new RuntimeException("Entidade não encontrada"));

			// Copia apenas os atributos modificáveis, sem alterar o identificador
			BeanUtils.copyProperties(entity, entidadeExistente, obterNomeDoIdentificador(entity.getClass()));

			T objAlterado = repository.save(entidadeExistente);
			completarPosAlterar(objAlterado);
			return objAlterado;
		} catch (Exception e) {
			throw new RuntimeException("Erro ao alterar a entidade: " + e.getMessage(), e);
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

    @Transactional
	public void deletar(ID id) {
		try {
			T entidade = repository.findById(id).orElseThrow(() -> new RuntimeException("Entidade não encontrada para exclusão"));
			repository.delete(entidade);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao excluir a entidade: " + e.getMessage(), e);
		}
	}
    
    protected boolean validarInserir(T entity) { return true; }

	protected boolean validarAlterar(T entity) { return true; }

	protected void completarInserir(T entity) { }

	protected void completarAlterar(T entity) { }

	protected void completarPosInserir(T entity) { }

	protected void completarPosAlterar(T entity) { }

	protected void completarPosPesquisar(List<T> entity) { }

	protected void completarPosPesquisarPorId(T entity) { }

}
