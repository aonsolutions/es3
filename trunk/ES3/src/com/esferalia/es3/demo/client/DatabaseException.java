package com.esferalia.es3.demo.client;

import java.io.Serializable;

public class DatabaseException extends Exception implements Serializable{
	
	private String exception;

	public DatabaseException() {
	}

	public DatabaseException(String exception){
		this.exception=exception;
	}
	
	public String getException(){
		return exception;
	}
	
}
