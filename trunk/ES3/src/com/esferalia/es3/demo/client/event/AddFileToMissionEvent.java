package com.esferalia.es3.demo.client.event;

import com.esferalia.es3.demo.client.dto.File;
import com.google.gwt.event.shared.GwtEvent;

public class AddFileToMissionEvent extends GwtEvent<AddFileToMissionEventHandler>{
	
	private File file;
	
	public static final Type<AddFileToMissionEventHandler> TYPE = new Type<AddFileToMissionEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AddFileToMissionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddFileToMissionEventHandler handler) {
		handler.onAddFileToMission(this);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
