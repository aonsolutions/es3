package com.esferalia.es3.demo.client.dto;

import java.io.Serializable;

public class FolderOrFile implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String absolutePath;
	
	public FolderOrFile(){
		this.setName("");
		this.setAbsolutePath("");
	}
	
	public FolderOrFile(String n, String p){
		this.setName(n);
		this.setAbsolutePath(p);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

}
