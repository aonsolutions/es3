package com.esferalia.es3.demo.client.exception;

import java.io.Serializable;

public class DirectoryException extends Exception implements Serializable{
	
	private String exception;
	
	public DirectoryException(){
		
	}
	
	public DirectoryException(String exception){
		this.exception = exception;
	}

	public String getException() {
		return exception;
	}

}
