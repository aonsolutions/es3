package com.esferalia.es3.demo.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.TreeItem;

public class SelectedMissionEvent extends GwtEvent<SelectedMissionEventHandler>{
	
	private TreeItem node;
	
	public static final Type<SelectedMissionEventHandler> TYPE = new Type<SelectedMissionEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SelectedMissionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelectedMissionEventHandler handler) {
		handler.onSelectedMission(this);
	}

	public TreeItem getNode() {
		return node;
	}

	public void setNode(TreeItem node) {
		this.node = node;
	}

}
