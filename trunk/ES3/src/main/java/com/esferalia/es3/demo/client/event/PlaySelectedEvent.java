package com.esferalia.es3.demo.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.TreeItem;

public class PlaySelectedEvent extends GwtEvent<PlaySelectedEventHandler> {

	private String originalName;
	private String shortName;
	private String path;
	private TreeItem node;
	
	public static final Type<PlaySelectedEventHandler> TYPE = new Type<PlaySelectedEventHandler>();
	
	protected void dispatch(PlaySelectedEventHandler handler) {
		handler.onPlaySelected(this);
	}

	public com.google.gwt.event.shared.GwtEvent.Type<PlaySelectedEventHandler> getAssociatedType() {
		return TYPE;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public TreeItem getNode() {
		return node;
	}

	public void setNode(TreeItem node) {
		this.node = node;
	}

}
