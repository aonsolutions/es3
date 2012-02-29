package com.esferalia.es3.demo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class TestMenuBar implements EntryPoint {

	private RootPanel rootPanel;
	private PopupPanel popup;
	private Label label;
	private Button button;
	private HorizontalPanel hPanel;
	
	@Override
	public void onModuleLoad() {
		rootPanel = RootPanel.get();
				
		label = new Label("Bienvenido");
		rootPanel.add(label);
				
		hPanel = new HorizontalPanel();
		hPanel.setWidth("200px");
		
		button = new Button("");
		button.setPixelSize(5, 10);
		button.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				popup = new PopupPanel();
				popup.setAutoHideEnabled(true);
				final int x = event.getClientX();
				final int y = event.getClientY();
				popup.add(contextMenu());
				popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			          public void setPosition(int offsetWidth, int offsetHeight) {
			              popup.setPopupPosition(x, y);
			            }
			          });
			}
			
		});
		hPanel.add(new HTML("Misión 1"));
		hPanel.add(button);
		hPanel.setCellHorizontalAlignment(button, HasHorizontalAlignment.ALIGN_RIGHT);

		
		TreeItem item1 = new TreeItem(hPanel);
		TreeItem item11 = new TreeItem(new HTML("Fichero 1.1"));
		TreeItem item12 = new TreeItem(new HTML("Fichero 1.2"));
		item1.addItem(item11);
		item1.addItem(item12);
		TreeItem item2 = new TreeItem(new HTML("Misión 2"));
		TreeItem item3 = new TreeItem(new HTML("Misión 3"));
		Tree tree = new Tree();
		tree.addSelectionHandler(new SelectionHandler<TreeItem>(){

			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				label.setText("Has pulsado sobre " + event.getSelectedItem().getText());
			}
			
		});
		tree.addItem(item1);
		tree.addItem(item2);
		tree.addItem(item3);
		tree.setAnimationEnabled(true);
		
	    ScrollPanel staticTreeWrapper = new ScrollPanel(tree);
	    staticTreeWrapper.setSize("240px", "240px");

	    // Wrap the static tree in a DecoratorPanel
	    DecoratorPanel staticDecorator = new DecoratorPanel();
	    staticDecorator.setWidget(staticTreeWrapper);
		
		rootPanel.add(staticDecorator);
	}
	
	private MenuBar contextMenu(){
		// Create commands
		Command addFile = new Command() {
			public void execute() {
				label.setText("Has hecho clic sobre Añadir archivo");
			}
		};
		Command update = new Command() {
			public void execute() {
				label.setText("Has hecho clic sobre Modificar misión");
			}
		};
		Command delete = new Command() {
			public void execute() {
				label.setText("Has hecho clic sobre Borrar misión");
			}
		};

		// Create a menu bar
		MenuBar menu = new MenuBar(true);
		menu.setAutoOpen(true);
		menu.setAnimationEnabled(true);
	    menu.addItem(new MenuItem("Añadir archivo", addFile));
	    menu.addItem(new MenuItem("Modificar misión", update));
	    menu.addItem(new MenuItem("Borrar misión", delete));
		return menu;
	}

}
