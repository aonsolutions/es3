package com.esferalia.es3.demo.client;

import com.esferalia.es3.demo.client.dto.Mission;
import com.esferalia.es3.demo.client.dto.MissionAction;
import com.esferalia.es3.demo.client.event.MissionEvent;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Button;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.event.shared.HandlerManager;

public class DeleteMissionWidget extends DialogBox{

	private VerticalPanel mainPanel;
	private Label titleLabel;
	private Button okButton;
	private Button cancelButton;
	
	public DeleteMissionWidget(final HandlerManager eventbus, final int id){
		mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(10);
		
		titleLabel = new Label("¿Está seguro que desea eliminar la misión " + id + " y todos sus archivos?");
		mainPanel.add(titleLabel);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(buttonPanel);
		
		okButton = new Button("Aceptar");
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				MissionEvent deleteMissionEvent = new MissionEvent();
				Mission deletedMision = new Mission();
				deletedMision.setId(id);
				deleteMissionEvent.setAccion(MissionAction.DELETE);
				deleteMissionEvent.setMision(deletedMision);
				eventbus.fireEvent(deleteMissionEvent);
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
