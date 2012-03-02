package com.esferalia.es3.demo.client.event;

import com.esferalia.es3.demo.client.dto.Mission;
import com.esferalia.es3.demo.client.dto.Action;
import com.google.gwt.event.shared.GwtEvent;

public class MissionEvent extends GwtEvent<MissionEventHandler> {

	private Mission mision;
	private Action accion;
	
	public static final Type<MissionEventHandler> TYPE = new Type<MissionEventHandler>();

	protected void dispatch(MissionEventHandler handler) {
		handler.onMission(this);
	}

	public com.google.gwt.event.shared.GwtEvent.Type<MissionEventHandler> getAssociatedType() {
		return TYPE;
	}

	public Mission getMision() {
		return mision;
	}

	public void setMision(Mission mision) {
		this.mision = mision;
	}

	public Action getAccion() {
		return accion;
	}

	public void setAccion(Action accion) {
		this.accion = accion;
	}
}
