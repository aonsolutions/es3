package com.esferalia.es3.demo.client.event;

import com.esferalia.es3.demo.client.window.WindowPanel;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CloseEvent extends GwtEvent<CloseEventHandler>{
	
	private VerticalPanel vPanel;
	private WindowPanel wPanel;
	
	public static final Type<CloseEventHandler> TYPE = new Type<CloseEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CloseEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CloseEventHandler handler) {
		handler.onClose(this);
	}

	public VerticalPanel getvPanel() {
		return vPanel;
	}

	public void setvPanel(VerticalPanel vPanel) {
		this.vPanel = vPanel;
	}

	public WindowPanel getwPanel() {
		return wPanel;
	}

	public void setwPanel(WindowPanel wPanel) {
		this.wPanel = wPanel;
	}

}
