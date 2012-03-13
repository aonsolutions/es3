package com.esferalia.es3.demo.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class CategoryEvent extends GwtEvent<CategoryEventHandler> {
	
	public static final Type<CategoryEventHandler> TYPE = new Type<CategoryEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<CategoryEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CategoryEventHandler handler) {
		handler.onCategory(this);
	}

}
