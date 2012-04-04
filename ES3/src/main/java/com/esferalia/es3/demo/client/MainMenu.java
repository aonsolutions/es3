package com.esferalia.es3.demo.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DirectionalTextHelper;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class MainMenu extends Composite {

	private static class CheckMenuItem extends MenuItem {
		
		private InputElement inputElem;
		private LabelElement labelElem;
		private DirectionalTextHelper directionalTextHelper;
		
		public CheckMenuItem(String text, Command command) {
			super("", command);
			initElement(text);
		}

		private void initElement(String text) {
			
		    Element elem = getElement();
		    
		    inputElem = InputElement.as(DOM.createInputCheck());
		    labelElem = Document.get().createLabelElement();

		    elem.appendChild(inputElem);
		    elem.appendChild(labelElem);

		    String uid = DOM.createUniqueId();
		    inputElem.setPropertyString("id", uid);
		    labelElem.setHtmlFor(uid);

		    directionalTextHelper = new DirectionalTextHelper(labelElem, true);
		    directionalTextHelper.setTextOrHtml(text, false);
		}
		/*
		@Override
		public void setText(String text) {
			directionalTextHelper.setTextOrHtml(text, false);
		}
		
		@Override
		public void setHTML(String html) {
			directionalTextHelper.setTextOrHtml(html, true);
		}
		
		@Override
		public void setHTML(SafeHtml html) {
			directionalTextHelper.setTextOrHtml(html.asString(), true);
		}*/
	}

	private ES3 es3;

	private MenuBar menu;

	public MainMenu() {

		menu = new MenuBar();
		menu.setAutoOpen(true);
		menu.setAnimationEnabled(false);

		// Create the mission menu
		MenuBar missionMenu = new MenuBar(true);
		menu.addItem(new MenuItem("Misión", missionMenu));
		missionMenu.addItem("Nueva", new NewMissionCommand());
		missionMenu.addItem("Eliminar", new AboutCommand());
		missionMenu.addSeparator();
		missionMenu.addItem("Propiedades", new EditMissionCommand());

		// Create the file menu
		MenuBar fileMenu = new MenuBar(true);
		menu.addItem(new MenuItem("Archivo", fileMenu));
		fileMenu.addItem("Nuevo", new HelpCommand());
		fileMenu.addItem("Eliminar", new AboutCommand());
		fileMenu.addSeparator();
		fileMenu.addItem("Propiedades", new AboutCommand());
		fileMenu.addSeparator();
		fileMenu.addItem("Cerrar", new AboutCommand());

		// Create the view menu
		//MenuBar viewMenu = new MenuBar(true);
		//menu.addItem(new MenuItem("Ver", viewMenu));
		//viewMenu.addItem( new CheckMenuItem("Mostrar Barra Lateral", new AboutCommand() ));

		// viewMenu.addItem(new CheckBox("Mostrar Barra de Estado").getHTML(),
		// new AboutCommand()).setEnabled(false);
		// viewMenu.addItem(new
		// CheckBox("Mostrar Barra de Herramientas").getHTML(), new
		// AboutCommand()).setEnabled(false);

		// Create the window menu
		MenuBar windowMenu = new MenuBar(true);
		menu.addItem(new MenuItem("Ventanas", windowMenu));
		windowMenu.addItem("En Cascada", new CascadeCommand());
		windowMenu.addItem("Mosaico Horizontal", new TileHorizontaCommand());
		windowMenu.addItem("Mosaico Vertical", new TileVerticalCommand());
		windowMenu.addSeparator();
		windowMenu.addItem("Cerrar Todas", new CloseAllCommand());

		// Create the help menu
		MenuBar helpMenu = new MenuBar(true);
		menu.addItem(new MenuItem("Ayuda", helpMenu));
		helpMenu.addItem("Ayuda", new HelpCommand());
		helpMenu.addSeparator();
		helpMenu.addItem("Acerca de ...", new AboutCommand());

		initWidget(menu);
	}

	public void setEs3(ES3 es3) {
		this.es3 = es3;
	}

	private class HelpCommand implements Command {
		@Override
		public void execute() {
			// TODO Auto-generated method stub

		}
	}

	private class AboutCommand implements Command {
		@Override
		public void execute() {
			// TODO Auto-generated method stub
			CustomDialog dialog = new CustomDialog();
			dialog.show();
			dialog.center();
		}
	}

	private class NewMissionCommand implements Command {
		@Override
		public void execute() {
			MissionDialog dialog = new MissionDialog();
			dialog.show();
			dialog.center();
		}
	}

	private class EditMissionCommand implements Command {
		@Override
		public void execute() {
			MissionDialog dialog = new MissionDialog();
			dialog.show();
			dialog.center();
		}
	}

	private class CloseAllCommand implements Command {
		@Override
		public void execute() {
			es3.closeAll();
		}
	}

	private class CascadeCommand implements Command {
		@Override
		public void execute() {
			es3.cascade();
		}
	}

	private class TileVerticalCommand implements Command {
		@Override
		public void execute() {
			es3.tileVertical();
		}
	}

	private class TileHorizontaCommand implements Command {
		@Override
		public void execute() {
			es3.tileHorizontal();
		}
	}
}
