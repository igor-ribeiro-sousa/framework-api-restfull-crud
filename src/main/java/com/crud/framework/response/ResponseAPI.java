package com.crud.framework.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseAPI<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private T data;
    private List<String> errors;
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public List<String> getErrors() {
        if (this.errors == null) {
            this.errors = new ArrayList<String>();
        }
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
