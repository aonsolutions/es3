package com.esferalia.es3.demo.client.window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.esferalia.es3.demo.client.dto.Coordenada;
import com.esferalia.es3.demo.client.event.CloseEvent;
import com.esferalia.es3.demo.client.event.CloseEventHandler;
import com.esferalia.es3.demo.client.flowplayer.FlowPlayer;

import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polyline;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoWidget;

public class Drag_nDropWindow{
	
	private HandlerManager handler;
	private AbsolutePanel boundaryPanel;
	private WindowController windowController;

	public Drag_nDropWindow(int width, int height) {
		// use the boundary panel as this composite's widget
	    boundaryPanel = new AbsolutePanel();
	    boundaryPanel.setPixelSize(width, height);
	    
	    handler = new HandlerManager(null);
		handler.addHandler(CloseEvent.TYPE, new CloseEventHandler(){
			@Override
			public void onClose(CloseEvent event) {
				boundaryPanel.remove(event.getwPanel());
			}
		});

	    // initialize window controller which provides drag and resize windows
	    windowController = new WindowController(boundaryPanel);
	    windowController.getPickupDragController().addDragHandler(new DemoDragHandler(new HTML("HELLO!")));

	}
	
	public AbsolutePanel getBoundaryPanel(){
		return boundaryPanel;
	}
	
	public void addImageWindow(String sourcePath){
	    HTML header = new HTML(sourcePath);
	    Image image = new Image(sourcePath);
	    image.setPixelSize(100, 100);
	    WindowPanel windowPanel = new WindowPanel(windowController, header, image, false, handler);
	    windowPanel.setStyleName("borderSettings");
	    boundaryPanel.add(windowPanel, 70, 30);
	}
	
	public void addFlowPlayerWindow(String sourcePath){
	    HTML header = new HTML(sourcePath);
		FlowPlayer fp = new FlowPlayer();
		fp.setSize("360px", "240px");
		fp.play(sourcePath);
	    WindowPanel windowPanel = new WindowPanel(windowController, header, fp, false, handler);
	    windowPanel.setStyleName("borderSettings");
	    boundaryPanel.add(windowPanel, 70, 30);
	}
	
	public void addHTML5PlayerWindow(String sourcePath){
	    HTML header = new HTML(sourcePath);
		VideoWidget videoPlayer = new VideoWidget(true, true, "");
		List<VideoSource> sources = new ArrayList<VideoSource>();
		sources.add(new VideoSource(sourcePath));
		videoPlayer.setSources(sources);
		if (sourcePath.endsWith(".ogv") || sourcePath.endsWith(".webm"))
			videoPlayer.setPixelSize(360, 240);
		else
			videoPlayer.setPixelSize(360, 140);
	    WindowPanel windowPanel = new WindowPanel(windowController, header, videoPlayer, false, handler);
	    windowPanel.setStyleName("borderSettings");
	    boundaryPanel.add(windowPanel, 70, 30);
	}
	
	public void addGPSWindow(Vector<Coordenada> result, LatLng[] listaLatLng){
	    HTML header = new HTML("GoogleMaps");
		MapWidget map = new MapWidget();
		map.setSize("300px", "300px");
		map.setScrollWheelZoomEnabled(true);
		int i = 0;
		Iterator<Coordenada> it = result.iterator();
		while(it.hasNext()){
			Coordenada coord = it.next();
			listaLatLng[i] = LatLng.newInstance(coord.getLatitud(), coord.getLongitud());
			i++;
		}
		Polyline polilinea = new Polyline(listaLatLng);
		map.setCenter(listaLatLng[0]);
		map.setZoomLevel(12);
		map.addOverlay(polilinea);
	    WindowPanel windowPanel = new WindowPanel(windowController, header, map, false, handler);
	    windowPanel.setStyleName("borderSettings");
	    boundaryPanel.add(windowPanel, 70, 30);
	}

}
