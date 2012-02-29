package com.esferalia.es3.demo.client;

import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.event.AddFileToMissionEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
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

public class AddFileToMissionWidget extends DialogBox {
	
	private static final String UPLOAD_ACTION_URL = GWT.getModuleBaseURL() + "upload";
	private VerticalPanel mainPanel;
	private Label titleLabel;
	private TextArea descriptionTextArea;
	private DateBox startDatePicker;
	private Button okButton;
	private Button cancelButton;
	private File file;
	private FormPanel form;
	
	private GreetingServiceAsync greetingSvc = GWT.create(GreetingService.class);
	
	public AddFileToMissionWidget(final String mision, final HandlerManager eventbus){
		mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setSpacing(10);
		
		titleLabel = new Label("Añadir archivo a la misión");
		mainPanel.add(titleLabel);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		mainPanel.add(horizontalPanel);
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(5);
		horizontalPanel.add(verticalPanel);
		
		Label lblNewLabel_2 = new Label("Descripción");
		verticalPanel.add(lblNewLabel_2);
		
		descriptionTextArea = new TextArea();
		verticalPanel.add(descriptionTextArea);
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setSpacing(5);
		horizontalPanel.add(verticalPanel_1);
		
		Label lblNewLabel_3 = new Label("Fecha creación");
		verticalPanel_1.add(lblNewLabel_3);
		
	    // Create a basic date picker
		startDatePicker = new DateBox();
		startDatePicker.setFormat(new DefaultFormat(com.google.gwt.i18n.client.DateTimeFormat.getFormat("dd/MM/yyyy")));

	    // Combine the widgets into a panel and return them
	    VerticalPanel vPanel_1 = new VerticalPanel();
	    vPanel_1.add(startDatePicker);
		verticalPanel_1.add(vPanel_1);
		
		// Add FileUploadWidget
		VerticalPanel uploadPanel = new VerticalPanel();
		uploadPanel.setSpacing(10);
		uploadPanel.setSize("300px", "100px");
		Label subirLabel = new Label("Seleccione el archivo");
		uploadPanel.add(subirLabel);
		subirLabel.setWidth("270px");
		
		// Create a FormPanel and point it at a service.
		form = new FormPanel();
		uploadPanel.add(form);
		form.setAction(UPLOAD_ACTION_URL);

		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		// Create a FileUpload widget.
		final FileUpload upload = new FileUpload();
		upload.setName("uploadFormElement");
		panel.add(upload);
		
		// Add an event handler to the form.
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent submitEvent) {
				// This event is fired just before the form is submitted. We can
				// take this opportunity to perform validation.
				if (upload.getFilename().length() == 0) {
					Window.alert("Es necesario subir un archivo");
					submitEvent.cancel();
				}
				else {
					AddFileToMissionEvent addFileToMissionEvent = new AddFileToMissionEvent();
					file.setMission(Integer.parseInt(mision));
					String originalName;
					if(upload.getFilename().contains("\\")){
						int beginIndex;
						beginIndex = upload.getFilename().lastIndexOf("\\");
						originalName = upload.getFilename().substring(beginIndex+1);
					}else
						originalName = upload.getFilename();
					file.setName(originalName);
					file.setDescription(descriptionTextArea.getText());
					file.setDate_time(startDatePicker.getValue());
					addFileToMissionEvent.setFile(file);
					eventbus.fireEvent(addFileToMissionEvent);
				}
			}
		});
		// Add an event handler to the form.
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent submitCompleteEvent) {
				// When the form submission is successfully completed, this
				// event is fired. Assuming the service returned a response of type
				// text/html, we can get the result text here (see the FormPanel
				// documentation for further explanation).
				// Window.alert(submitCompleteEvent.getResults());
				hide();
				changeFileDirectory(file);
			}
			
			private void changeFileDirectory(File file) {
				// Initialize the service proxy.
				if (greetingSvc == null) {
					greetingSvc = GWT.create(GreetingService.class);
				}

				// Set up the callback object.
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.setTitle("ERROR: No se ha podido cambiar la ubicación del archivo en el servidor");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Actualizar el árbol de misiones contenido en StreamingWeb.java
					}
				};

				// Make the call to the greeting service.
				greetingSvc.changeFileDirectory(file, callback);
			}
		});
		mainPanel.add(uploadPanel);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(buttonPanel);
		
		okButton = new Button("Aceptar");
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent clickEvent) {
				file = new File();
				form.submit();
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