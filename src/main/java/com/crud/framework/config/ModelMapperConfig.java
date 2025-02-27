package com.crud.framework.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Configuração extra para listas aninhadas e evitar valores nulos
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)  // Permite melhor correspondência de campos
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);  // Acessa atributos privados
        
        return modelMapper;
    }
}