package com.esferalia.es3.demo.client;

import com.esferalia.es3.demo.client.dto.Action;
import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.event.FileEvent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DeleteFileWidget extends DialogBox {
	
	private VerticalPanel mainPanel;
	private Label titleLabel;
	private Button okButton;
	private Button cancelButton;
	
	public DeleteFileWidget(final HandlerManager eventbus, final int id){
	mainPanel = new VerticalPanel();
	mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	mainPanel.setSpacing(10);
	
	titleLabel = new Label("¿Está seguro que desea eliminar el archivo?");
	mainPanel.add(titleLabel);
	
	HorizontalPanel buttonPanel = new HorizontalPanel();
	buttonPanel.setSpacing(10);
	buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	mainPanel.add(buttonPanel);
	
	okButton = new Button("Aceptar");
	okButton.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent event) {
			FileEvent deleteFileEvent = new FileEvent();
			File deletedFile = new File();
			deletedFile.setId(id);
			deleteFileEvent.setAccion(Action.DELETE);
			deleteFileEvent.setFile(deletedFile);
			eventbus.fireEvent(deleteFileEvent);
			hide();
		}
	});
	buttonPanel.add(okButton);
	cancelButton = new Button("Cancelar");
	cancelButton.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent event) {
			hide();
		}
	});
	buttonPanel.add(cancelButton);
	setWidget(mainPanel);
	}
}
