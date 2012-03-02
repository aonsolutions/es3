package com.esferalia.es3.demo.client.service;

import java.util.Vector;

import com.esferalia.es3.demo.client.dto.Coordenada;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("xml")
public interface XMLService extends RemoteService {

	int getNumRecorridos(String xmlPath);

	int sizeOfRecorrido(int index, String xmlPath);

	Vector<Coordenada> getCoordenadas(String selected, String xmlPath);
}
