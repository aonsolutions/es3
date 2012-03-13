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
	private VerticalPanel verticalPanel;
	private HorizontalPanel horizontalPanel;
	private Label lblNewLabel;
	private Label lblNewLabel_1;
	private Label lblNewLabel_2;
	private VerticalPanel verticalPanel_1;
	private Label lblNewLabel_3;
	private VerticalPanel vPanel;
	private VerticalPanel verticalPanel_2;
	private Label lblNewLabel_4;
	private VerticalPanel vPanel_1;
	private HorizontalPanel buttonPanel;
	private Label errorLabel;
	
	public CreateMissionWidget(final HandlerManager eventbus){
		mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(10);
		
		titleLabel = new Label("Crear nueva misión");
		mainPanel.add(titleLabel);
		
		horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		mainPanel.add(horizontalPanel);
		
		verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(5);
		horizontalPanel.add(verticalPanel);
		
		lblNewLabel = new Label("Nombre");
		verticalPanel.add(lblNewLabel);
		
		nombreTextBox = new TextBox();
		nombreTextBox.setMaxLength(64);
		verticalPanel.add(nombreTextBox);
		
		lblNewLabel_1 = new Label("Alias");
		verticalPanel.add(lblNewLabel_1);
		
		aliasTextBox = new TextBox();
		aliasTextBox.setMaxLength(32);
		verticalPanel.add(aliasTextBox);
		
		lblNewLabel_2 = new Label("Descripción");
		verticalPanel.add(lblNewLabel_2);
		
		descriptionTextArea = new TextArea();
		verticalPanel.add(descriptionTextArea);
		
		verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setSpacing(5);
		horizontalPanel.add(verticalPanel_1);
		
		lblNewLabel_3 = new Label("Fecha inicio");
		verticalPanel_1.add(lblNewLabel_3);
		
	    // Create a basic date picker
		startDatePicker = new DateBox();
		startDatePicker.setFormat(new DefaultFormat(com.google.gwt.i18n.client.DateTimeFormat.getFormat("dd/MM/yyyy")));

	    // Combine the widgets into a panel and return them
	    vPanel = new VerticalPanel();
	    vPanel.add(startDatePicker);
		verticalPanel_1.add(vPanel);
		
		verticalPanel_2 = new VerticalPanel();
		verticalPanel_2.setSpacing(5);
		horizontalPanel.add(verticalPanel_2);
		
		lblNewLabel_4 = new Label("Fecha fin");
		verticalPanel_2.add(lblNewLabel_4);
		
	    // Create a basic date picker
		endDatePicker = new DateBox();
		endDatePicker.setFormat(new DefaultFormat(com.google.gwt.i18n.client.DateTimeFormat.getFormat("dd/MM/yyyy")));

	    // Combine the widgets into a panel and return them
	    vPanel_1 = new VerticalPanel();
	    vPanel_1.add(endDatePicker);
		verticalPanel_2.add(vPanel_1);
		
		errorLabel = new Label();
		errorLabel.setStyleName("errorLabel");
		errorLabel.setVisible(false);
		mainPanel.add(errorLabel);
		
		buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(buttonPanel);
		
		okButton = new Button("Aceptar");
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (nombreTextBox.getText().trim().isEmpty()
						|| startDatePicker.getValue() == null
						|| endDatePicker.getValue() == null) {
					errorLabel.setText("Recuerde que los campos 'Nombre', 'Fecha inicio' y 'Fecha fin' son obligatorios");
					errorLabel.setVisible(true);
					event.stopPropagation();
				} else if (startDatePicker.getValue().compareTo(
						endDatePicker.getValue()) == 1) {
					errorLabel.setText("Recuerde que 'Fecha fin' no debe ser anterior a 'Fecha inicio'");
					errorLabel.setVisible(true);
					event.stopPropagation();
				} else {
					errorLabel.setVisible(false);
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
