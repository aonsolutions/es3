package com.esferalia.es3.demo.server;

import java.util.Vector;

import com.esferalia.es3.demo.client.dto.Coordenada;
import com.esferalia.es3.demo.client.service.XMLService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class XMLServiceImpl extends RemoteServiceServlet implements XMLService {

	private EjemploReadDomXml lectorXML;

	public int getNumRecorridos(String xmlPath) {
		lectorXML = new EjemploReadDomXml(xmlPath);
		return lectorXML.getNumRecorridos();
	}

	public int sizeOfRecorrido(int selected, String xmlPath) {
		lectorXML = new EjemploReadDomXml(xmlPath);
		return lectorXML.sizeOfRecorrido(selected);
	}

	public Vector<Coordenada> getCoordenadas(String selected, String xmlPath) {
		lectorXML = new EjemploReadDomXml(xmlPath);
		return lectorXML.getCoordenadas(selected);
	}

}
