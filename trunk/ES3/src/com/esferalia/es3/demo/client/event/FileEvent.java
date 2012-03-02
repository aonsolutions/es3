package com.esferalia.es3.demo.client.event;

import com.esferalia.es3.demo.client.dto.Action;
import com.esferalia.es3.demo.client.dto.File;
import com.google.gwt.event.shared.GwtEvent;

public class FileEvent extends GwtEvent<FileEventHandler>{
	
	private File file;
	private Action accion;
	
	public static final Type<FileEventHandler> TYPE = new Type<FileEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FileEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FileEventHandler handler) {
		handler.onFile(this);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Action getAccion() {
		return accion;
	}

	public void setAccion(Action accion) {
		this.accion = accion;
	}

}
