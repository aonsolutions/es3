package com.esferalia.es3.demo.client.event;

import com.esferalia.es3.demo.client.dto.Mission;
import com.google.gwt.event.shared.GwtEvent;

public class CreateMissionEvent extends GwtEvent<CreateMissionEventHandler> {

	private Mission mision;
	
	public static final Type<CreateMissionEventHandler> TYPE = new Type<CreateMissionEventHandler>();

	protected void dispatch(CreateMissionEventHandler handler) {
		handler.onCreateMission(this);
	}

	public com.google.gwt.event.shared.GwtEvent.Type<CreateMissionEventHandler> getAssociatedType() {
		return TYPE;
	}

	public Mission getMision() {
		return mision;
	}

	public void setMision(Mission mision) {
		this.mision = mision;
	}
}
