package com.esferalia.es3.demo.client.service;

import java.util.Vector;

import com.esferalia.es3.demo.client.dto.Coordenada;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface XMLServiceAsync {
	
	void getNumRecorridos(String xmlPath, AsyncCallback<Integer> callback);

	void sizeOfRecorrido(int index, String xmlPath,
			AsyncCallback<Integer> callback);

	void getCoordenadas(String selected, String xmlPath,
			AsyncCallback<Vector<Coordenada>> callback);
}
