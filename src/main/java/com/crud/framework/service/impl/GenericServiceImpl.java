package com.crud.framework.service.impl;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.BeanUtils;
import javax.persistence.Id;


import com.crud.framework.config.GenericMapper;
import com.crud.framework.repository.GenericRepository;
import com.crud.framework.service.GenericService;


public abstract class GenericServiceImpl<T, D, ID> implements GenericService<T, D, ID> {
	
	private final Class<T> entityClass;
	private final Class<D> dtoClass;
	
	protected final GenericRepository<T, ID> repository;
    protected final GenericMapper genericMapper;
    
    public GenericServiceImpl(
    		GenericRepository<T, ID> repository,
    		GenericMapper genericMapper, 
    		Class<T> entityClass, 
    		Class<D> dtoClass) {
        this.repository = repository;
        this.genericMapper = genericMapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public List<D> pesquisar() {
    	try {
    		List<T> resultados = repository.findAll();
    		
    		List<D> resultadosDTO = genericMapper.convertListEntityToListDTO(resultados, dtoClass);
    		
    		completarPosPesquisar(resultadosDTO);
    		
    		return resultadosDTO;
    		
    	} catch(Exception e) {
			throw new RuntimeException("Erro ao pesquisar: " + e.getMessage(), e);
			
    	}
    }
    
    @Override
	public D pesquisarPorId(ID id) {
		T resultado = repository.findById(id).orElseThrow(() -> new RuntimeException("Entidade não encontrada"));
		
        D resultadoDTO = genericMapper.convertToDTO(resultado, dtoClass);
        
		completarPosPesquisarPorId(resultadoDTO);
		
		return resultadoDTO;
	}

    @Override
	public D inserir(D entityDTO) {
		if (!validarInserir(entityDTO)) {
			throw new RuntimeException("Falha na validação da inserção.");
			
		}
		try {
			T entity = genericMapper.convertToEntity(entityDTO, entityClass);
			
			completarInserir(entity);
			
			T resposta = repository.save(entity);
			
			D respostaDTO = genericMapper.convertToDTO(resposta, dtoClass);
			
			completarPosInserir(respostaDTO);
			
			return respostaDTO;
			
		} catch (Exception e) {
			throw new RuntimeException("Erro ao inserir: " + e.getMessage(), e);
			
		}
	}
    
    @Override
	public D alterar(ID id, D entityDTO) {
		if (!validarAlterar(entityDTO)) {
			throw new RuntimeException("Falha na validação da alteração.");
			
		}
		try {
			T entity = genericMapper.convertToEntity(entityDTO, entityClass);
			
			completarAlterar(entity);
			
			T entityBD = repository.findById(id).orElseThrow(() -> new RuntimeException("Entidade não encontrada"));
			
			BeanUtils.copyProperties(entity, entityBD, obterNomeDoIdentificador(entity.getClass()));
			
			D resposta = genericMapper.convertToDTO(repository.save(entityBD), dtoClass);
			
			completarPosAlterar(resposta);
			
			return resposta;
			
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

    @Override
	public void deletar(ID id) {
		try {
			T entidade = repository.findById(id).orElseThrow(() -> new RuntimeException("Entidade não encontrada para exclusão"));
			
			repository.delete(entidade);
			
		} catch (Exception e) {
			throw new RuntimeException("Erro ao excluir a entidade: " + e.getMessage(), e);
		}
	}
    
    protected boolean validarInserir(D entityDTO) { return true; }

	protected boolean validarAlterar(D entityDTO) { return true; }

	protected void completarInserir(T entity) { }

	protected void completarAlterar(T entity) { }

	protected void completarPosInserir(D entityDTO) { }

	protected void completarPosAlterar(D entityDTO) { }

	protected void completarPosPesquisar(List<D> entityDTO) { }

	protected void completarPosPesquisarPorId(D entityDTO) { }

}
