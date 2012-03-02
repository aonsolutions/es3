package com.esferalia.es3.demo.client.dto;

import java.io.Serializable;

public class Coordenada implements Serializable{
	
	private double latitud;
	private double longitud;
	private int fecha;
	private int hora;
	
	public Coordenada(){}
	
	public Coordenada(double lat, double lng, int date, int time){
		this.latitud = lat;
		this.longitud = lng;
		this.fecha = date;
		this.hora = time;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	
	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public int getFecha() {
		return fecha;
	}

	public void setFecha(int fecha) {
		this.fecha = fecha;
	}

	public int getHora() {
		return hora;
	}

	public void setHora(int hora) {
		this.hora = hora;
	}
	

}
