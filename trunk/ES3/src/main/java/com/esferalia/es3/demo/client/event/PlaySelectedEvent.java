package com.esferalia.es3.demo.client.event;

import com.esferalia.es3.demo.client.tree.CustomNode;
import com.google.gwt.event.shared.GwtEvent;

public class PlaySelectedEvent extends GwtEvent<PlaySelectedEventHandler> {

	private String nameFile;
	private String path;
	private CustomNode node;
	
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

	public CustomNode getNode() {
		return node;
	}

	public void setNode(CustomNode node) {
		this.node = node;
	}

}
