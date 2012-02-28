package com.esferalia.es3.demo.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class ES3 implements EntryPoint {

	private RootPanel rootPanel;
	private Date date;
	
	@Override
	public void onModuleLoad() {
		rootPanel = RootPanel.get();
	}
	
	public Widget datePickerInitialize() {
	    // Create a basic date picker
	    DatePicker datePicker = new DatePicker();
	    final TextBox text = new TextBox();
	    text.setReadOnly(true);

	    // Set the value in the text box when the user selects a date
	    datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
	      public void onValueChange(ValueChangeEvent<Date> event) {
	        date = event.getValue();
	        String dateString = DateTimeFormat.getFormat("dd/MM/yyyy").format(date);
/*	        String[] fecha = date.toString().split(" ");
	        // Mes
	        String mes = fecha[1];
	        // Dia del mes
	        String diaMes = fecha[2];
	        // Año
	        String anho = fecha[5];
	        text.setText(diaMes + " " + mes + " " +  anho);*/
	        text.setText(dateString);
	      }
	    });

	    // Set the default value
	    datePicker.setValue(new Date(), true);

	    // Combine the widgets into a panel and return them
	    VerticalPanel vPanel = new VerticalPanel();
	    vPanel.add(new HTML(""));
	    vPanel.add(text);
	    vPanel.add(datePicker);
	    return vPanel;
	  }

}
