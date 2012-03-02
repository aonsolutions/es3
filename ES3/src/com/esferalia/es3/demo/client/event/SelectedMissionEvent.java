package com.esferalia.es3.demo.client.event;

import com.esferalia.es3.demo.client.tree.CustomNode;
import com.google.gwt.event.shared.GwtEvent;

public class SelectedMissionEvent extends GwtEvent<SelectedMissionEventHandler>{
	
	private String name;
	private CustomNode node;
	
	public static final Type<SelectedMissionEventHandler> TYPE = new Type<SelectedMissionEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<SelectedMissionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelectedMissionEventHandler handler) {
		handler.onSelectedMission(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomNode getNode() {
		return node;
	}

	public void setNode(CustomNode node) {
		this.node = node;
	}

}
