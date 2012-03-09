package com.esferalia.es3.demo.client;

import com.esferalia.es3.demo.client.dto.Action;
import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.event.FileEvent;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Button;

import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.TextBox;

public class UpdateFileWidget extends DialogBox {

	private VerticalPanel mainPanel;
	private Label titleLabel;
	private TextArea descriptionTextArea;
	private DateBox startDatePicker;
	private Button okButton;
	private Button cancelButton;
	private TextBox nameTextBox;
	private HorizontalPanel horizontalPanel;
	private VerticalPanel verticalPanel;
	private VerticalPanel verticalPanel_1;
	private HorizontalPanel buttonPanel;
	private Label lblNewLabel;
	private VerticalPanel vPanel;
	private Label nameLabel;
	private Label descriptionLabel;
	private Label errorLabel;

	public UpdateFileWidget(final HandlerManager eventbus, final File oldFile) {
		mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(10);

		titleLabel = new Label("Añadir archivo a la misión");
		mainPanel.add(titleLabel);

		horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		mainPanel.add(horizontalPanel);

		verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(5);
		horizontalPanel.add(verticalPanel);
		
		nameLabel = new Label("Nombre");
		verticalPanel.add(nameLabel);
		
		nameTextBox = new TextBox();
		final int punto = oldFile.getName().lastIndexOf(".");
		nameTextBox.setText(oldFile.getName().substring(0, punto));
		verticalPanel.add(nameTextBox);
		
		descriptionLabel = new Label("Descripción");
		verticalPanel.add(descriptionLabel);

		descriptionTextArea = new TextArea();
		descriptionTextArea.setText(oldFile.getDescription());
		verticalPanel.add(descriptionTextArea);

		verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setSpacing(5);
		horizontalPanel.add(verticalPanel_1);

		lblNewLabel = new Label("Fecha creación");
		verticalPanel_1.add(lblNewLabel);

		// Create a basic date picker
		startDatePicker = new DateBox();
		startDatePicker.setValue(oldFile.getDate_time());
		startDatePicker.setFormat(new DefaultFormat(com.google.gwt.i18n.client.DateTimeFormat.getFormat("dd/MM/yyyy")));

		// Combine the widgets into a panel and return them
		vPanel = new VerticalPanel();
		vPanel.add(startDatePicker);
		verticalPanel_1.add(vPanel);
		
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
			public void onClick(ClickEvent clickEvent) {
				if (nameTextBox.getText().trim().isEmpty()
						|| startDatePicker.getValue() == null) {
					errorLabel.setText("Recuerde que los campos 'Nombre' y 'Fecha creación'");
					errorLabel.setVisible(true);
				} else {
					errorLabel.setVisible(false);
					FileEvent updateFileEvent = new FileEvent();
					File updatedFile = new File();
					updatedFile.setId(oldFile.getId());
					updatedFile.setMission(oldFile.getMission());
					updatedFile.setName(nameTextBox.getText() + oldFile.getName().substring(punto));
					updatedFile.setDescription(descriptionTextArea.getText());
					updatedFile.setDate_time(startDatePicker.getValue());
					updateFileEvent.setAccion(Action.UPDATE);
					updateFileEvent.setFile(updatedFile);
					eventbus.fireEvent(updateFileEvent);
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
