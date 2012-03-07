package com.esferalia.es3.demo.client;

import com.esferalia.es3.demo.client.dto.Mission;
import com.esferalia.es3.demo.client.dto.Action;
import com.esferalia.es3.demo.client.event.MissionEvent;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Button;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.event.shared.HandlerManager;

public class CreateMissionWidget extends DialogBox{

	private VerticalPanel mainPanel;
	private Label titleLabel;
	private TextBox nombreTextBox;
	private TextBox aliasTextBox;
	private TextArea descriptionTextArea;
	private DateBox startDatePicker;
	private DateBox endDatePicker;
	private Button okButton;
	private Button cancelButton;
	
	public CreateMissionWidget(final HandlerManager eventbus){
		mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(10);
		
		titleLabel = new Label("Crear nueva misión");
		mainPanel.add(titleLabel);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		mainPanel.add(horizontalPanel);
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(5);
		horizontalPanel.add(verticalPanel);
		
		Label lblNewLabel = new Label("Nombre");
		verticalPanel.add(lblNewLabel);
		
		nombreTextBox = new TextBox();
		nombreTextBox.setMaxLength(64);
		verticalPanel.add(nombreTextBox);
		
		Label lblNewLabel_1 = new Label("Alias");
		verticalPanel.add(lblNewLabel_1);
		
		aliasTextBox = new TextBox();
		aliasTextBox.setMaxLength(32);
		verticalPanel.add(aliasTextBox);
		
		Label lblNewLabel_2 = new Label("Descripción");
		verticalPanel.add(lblNewLabel_2);
		
		descriptionTextArea = new TextArea();
		verticalPanel.add(descriptionTextArea);
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setSpacing(5);
		horizontalPanel.add(verticalPanel_1);
		
		Label lblNewLabel_3 = new Label("Fecha inicio");
		verticalPanel_1.add(lblNewLabel_3);
		
	    // Create a basic date picker
		startDatePicker = new DateBox();
		startDatePicker.setFormat(new DefaultFormat(com.google.gwt.i18n.client.DateTimeFormat.getFormat("dd/MM/yyyy")));

	    // Combine the widgets into a panel and return them
	    VerticalPanel vPanel_1 = new VerticalPanel();
	    vPanel_1.add(startDatePicker);
		verticalPanel_1.add(vPanel_1);
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		verticalPanel_2.setSpacing(5);
		horizontalPanel.add(verticalPanel_2);
		
		Label lblNewLabel_4 = new Label("Fecha fin");
		verticalPanel_2.add(lblNewLabel_4);
		
	    // Create a basic date picker
		endDatePicker = new DateBox();
		endDatePicker.setFormat(new DefaultFormat(com.google.gwt.i18n.client.DateTimeFormat.getFormat("dd/MM/yyyy")));

	    // Combine the widgets into a panel and return them
	    VerticalPanel vPanel_2 = new VerticalPanel();
	    vPanel_2.add(endDatePicker);
		verticalPanel_2.add(vPanel_2);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(buttonPanel);
		
		okButton = new Button("Aceptar");
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				MissionEvent createMissionEvent = new MissionEvent();
				Mission mision = new Mission();
				mision.setName(nombreTextBox.getText());
				mision.setAlias(aliasTextBox.getText());
				mision.setDescription(descriptionTextArea.getText());
				mision.setStart_date(startDatePicker.getValue());
				mision.setEnd_date(endDatePicker.getValue());
				createMissionEvent.setAccion(Action.CREATE);
				createMissionEvent.setMision(mision);
				eventbus.fireEvent(createMissionEvent);
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
