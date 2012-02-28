package com.esferalia.es3.demo.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class TestBD implements EntryPoint {
	
	private RootPanel rootPanel;
	private DatabaseServiceAsync databaseService = GWT.create(DatabaseService.class);

	@Override
	public void onModuleLoad() {
		rootPanel = RootPanel.get();
		loadDatabase();
	}

	private void loadDatabase() {
		// Initialize the service proxy.
		if (databaseService == null) {
			databaseService = GWT.create(DatabaseService.class);
		}

		// Set up the callback object.
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				if (caught instanceof DatabaseException) {
					String error = caught.getMessage();
					System.out.println(error);
				} else {
					System.out.println(caught.toString());
				}
			}

			@Override
			public void onSuccess(Void result) {
				System.out.println("Succesful connection");
			}
		};

		databaseService.loadDatabase(callback);
	}
	
}
