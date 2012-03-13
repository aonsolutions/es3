package com.esferalia.es3.demo.client.dto;

import java.io.Serializable;
import java.util.Vector;

public class MissionCell implements Serializable {
	
	private String name;
	private int id;
	
	private Vector<FileCell> videos;
	private Vector<FileCell> audios;
	private Vector<FileCell> images;
	private Vector<FileCell> gps;
	private Vector<FileCell> docs;
	
	public MissionCell(){
		setVideos(new Vector<FileCell>());
		setAudios(new Vector<FileCell>());
		setImages(new Vector<FileCell>());
		setGps(new Vector<FileCell>());
		setDocs(new Vector<FileCell>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vector<FileCell> getVideos() {
		return videos;
	}

	public void setVideos(Vector<FileCell> videos) {
		this.videos = videos;
	}
	
	public void addVideo(FileCell video){
		this.videos.add(video);
	}

	public Vector<FileCell> getAudios() {
		return audios;
	}

	public void setAudios(Vector<FileCell> audios) {
		this.audios = audios;
	}
	
	public void addAudio(FileCell audio){
		this.audios.add(audio);
	}

	public Vector<FileCell> getImages() {
		return images;
	}

	public void setImages(Vector<FileCell> images) {
		this.images = images;
	}
	
	public void addImage(FileCell image){
		this.images.add(image);
	}

	public Vector<FileCell> getGps() {
		return gps;
	}

	public void setGps(Vector<FileCell> gps) {
		this.gps = gps;
	}
	
	public void addGPS(FileCell gps){
		this.gps.add(gps);
	}

	public Vector<FileCell> getDocs() {
		return docs;
	}

	public void setDocs(Vector<FileCell> docs) {
		this.docs = docs;
	}
	
	public void addDoc(FileCell doc){
		this.docs.add(doc);
	}

}
