package com.esferalia.es3.demo.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class PlaySelectedEvent extends GwtEvent<PlaySelectedEventHandler> {

	private String nameFile;
	private String path;
	
	public static final Type<PlaySelectedEventHandler> TYPE = new Type<PlaySelectedEventHandler>();
	
	protected void dispatch(PlaySelectedEventHandler handler) {
		handler.onPlaySelected(this);
	}

	public com.google.gwt.event.shared.GwtEvent.Type<PlaySelectedEventHandler> getAssociatedType() {
		return TYPE;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

}
