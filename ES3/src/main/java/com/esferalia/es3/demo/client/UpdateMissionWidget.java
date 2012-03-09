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

public class UpdateMissionWidget extends DialogBox{

	private VerticalPanel mainPanel;
	private Label titleLabel;
	private TextBox nombreTextBox;
	private TextBox aliasTextBox;
	private TextArea descriptionTextArea;
	private DateBox startDatePicker;
	private DateBox endDatePicker;
	private Button okButton;
	private Button cancelButton;
	private Label errorLabel;
	
	public UpdateMissionWidget(final HandlerManager eventbus, final Mission oldMission){
		mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(10);
		
		titleLabel = new Label("Modificar la misión " + oldMission.getName() + " con ID " + oldMission.getId());
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
		nombreTextBox.setText(oldMission.getName());
		nombreTextBox.setMaxLength(64);
		verticalPanel.add(nombreTextBox);
		
		Label lblNewLabel_1 = new Label("Alias");
		verticalPanel.add(lblNewLabel_1);
		
		aliasTextBox = new TextBox();
		aliasTextBox.setText(oldMission.getAlias());
		aliasTextBox.setMaxLength(32);
		verticalPanel.add(aliasTextBox);
		
		Label lblNewLabel_2 = new Label("Descripción");
		verticalPanel.add(lblNewLabel_2);
		
		descriptionTextArea = new TextArea();
		descriptionTextArea.setText(oldMission.getDescription());
		verticalPanel.add(descriptionTextArea);
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setSpacing(5);
		horizontalPanel.add(verticalPanel_1);
		
		Label lblNewLabel_3 = new Label("Fecha inicio");
		verticalPanel_1.add(lblNewLabel_3);
		
	    // Create a basic date picker
		startDatePicker = new DateBox();
		startDatePicker.setValue(oldMission.getStart_date());
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
		endDatePicker.setValue(oldMission.getEnd_date());
		endDatePicker.setFormat(new DefaultFormat(com.google.gwt.i18n.client.DateTimeFormat.getFormat("dd/MM/yyyy")));

	    // Combine the widgets into a panel and return them
	    VerticalPanel vPanel_2 = new VerticalPanel();
	    vPanel_2.add(endDatePicker);
		verticalPanel_2.add(vPanel_2);
		
		errorLabel = new Label();
		errorLabel.setStyleName("errorLabel");
		errorLabel.setVisible(false);
		mainPanel.add(errorLabel);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
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
				} else if (startDatePicker.getValue().compareTo(
						endDatePicker.getValue()) == 1) {
					errorLabel.setText("Recuerde que 'Fecha fin' no debe ser anterior a 'Fecha inicio'");
					errorLabel.setVisible(true);
				} else {
					errorLabel.setVisible(false);
					MissionEvent updateMissionEvent = new MissionEvent();
					Mission updatedMision = new Mission();
					updatedMision.setId(oldMission.getId());
					updatedMision.setName(nombreTextBox.getText());
					updatedMision.setAlias(aliasTextBox.getText());
					updatedMision.setDescription(descriptionTextArea.getText());
					updatedMision.setStart_date(startDatePicker.getValue());
					updatedMision.setEnd_date(endDatePicker.getValue());
					updateMissionEvent.setAccion(Action.UPDATE);
					updateMissionEvent.setMision(updatedMision);
					eventbus.fireEvent(updateMissionEvent);
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
