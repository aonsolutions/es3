package com.esferalia.es3.demo.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.esferalia.es3.demo.client.dto.Coordenada;

public class EjemploReadDomXml {
	
	// private File fXmlFile;
	private InputStream fXmlFile;
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;

	public EjemploReadDomXml(String name){
		try {
			// FIXME For local XML Path
			// fXmlFile = new File(name);

			URL url = new URL(name);
			URLConnection urlConnection = url.openConnection();
			fXmlFile = new BufferedInputStream(urlConnection.getInputStream());

			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void imprimirXML() {
		System.out.println("Elemento raiz : " + doc.getFirstChild().getNodeName());
		NodeList nList = doc.getElementsByTagName("coordenada");
		System.out.println("Coordenadas totales: " + nList.getLength());
		System.out.println("-----------------------");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				System.out.println("Longitud : " + getTagValue("longitud", eElement));
				System.out.println("Latitud : " + getTagValue("latitud", eElement));
				System.out.println("Fecha : " + getTagValue("fecha", eElement));
				System.out.println("Hora : " + getTagValue("hora", eElement));
				System.out.println("");
			}
		}
	}
	
	public Integer sizeOfRecorrido(int id){
		Element element = doc.getElementById(Integer.toString(id));
		NodeList children = element.getElementsByTagName("coordenada");
		return children.getLength();
	}
	
	public Integer getNumRecorridos(){
		NodeList nList = doc.getElementsByTagName("recorrido");
		return nList.getLength();
	}

	public Vector<Coordenada> getCoordenadas(String id) {
		Vector<Coordenada> coordenadas = new Vector<Coordenada>();
		Element element = doc.getElementById(id);
		NodeList children = element.getElementsByTagName("coordenada");
		for (int temp = 0; temp < children.getLength(); temp++) {
			Node nNode = children.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				Coordenada coordenada = new Coordenada();
				coordenada.setLatitud(Double.parseDouble(getTagValue("latitud", eElement)));
				coordenada.setLongitud(Double.parseDouble(getTagValue("longitud", eElement)));
				coordenada.setFecha(Integer.parseInt(getTagValue("fecha", eElement)));
				coordenada.setHora(Integer.parseInt(getTagValue("hora", eElement)));
				coordenadas.add(coordenada);
			}
		}
		return coordenadas;
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}
}
