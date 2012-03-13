package com.esferalia.es3.demo.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.esferalia.es3.demo.client.dto.Coordenada;
import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.dto.FileCell;
import com.esferalia.es3.demo.client.dto.Mission;
import com.esferalia.es3.demo.client.dto.MissionCell;
import com.esferalia.es3.demo.client.event.CategoryEvent;
import com.esferalia.es3.demo.client.event.CategoryEventHandler;
import com.esferalia.es3.demo.client.event.FileEvent;
import com.esferalia.es3.demo.client.event.FileEventHandler;
import com.esferalia.es3.demo.client.event.MissionEvent;
import com.esferalia.es3.demo.client.event.MissionEventHandler;
import com.esferalia.es3.demo.client.event.PlaySelectedEvent;
import com.esferalia.es3.demo.client.event.PlaySelectedEventHandler;
import com.esferalia.es3.demo.client.event.SelectedMissionEvent;
import com.esferalia.es3.demo.client.event.SelectedMissionEventHandler;
import com.esferalia.es3.demo.client.flowplayer.FlowPlayer;
import com.esferalia.es3.demo.client.service.DatabaseService;
import com.esferalia.es3.demo.client.service.DatabaseServiceAsync;
import com.esferalia.es3.demo.client.service.TreeService;
import com.esferalia.es3.demo.client.service.TreeServiceAsync;
import com.esferalia.es3.demo.client.service.XMLService;
import com.esferalia.es3.demo.client.service.XMLServiceAsync;
import com.esferalia.es3.demo.client.tree.CustomNode;
import com.esferalia.es3.demo.client.tree.FoldersAndFilesTree;
import com.esferalia.es3.demo.client.tree.MissionTree;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.ScaleControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoWidget;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItemSeparator;

public class StreamingWeb implements EntryPoint {
	
	// FIXME directoryPath
	private final String BASE_PATH = "/srv/www/lighttpd/es3/mission/"; 
			// "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\mission";
			// "/srv/www/lighttpd/es3/mission/"; 

	private Images images;
	private RootPanel rootPanel;
	private DockPanel dockPanel;
	private HorizontalPanel northPanel;
	private Image logo;
	private Label titleLabel;
	private Image headerImageNorth;
	private Label versionLabel;
	private MenuBar menuBar;
	private MenuItem missionItem;
	private MenuItem newMissionItem;
	private MenuItem updateMissionItem;
	private MenuItem deleteMissionItem;
	private MenuBar missionSubMenu;
	private MenuItem fileItem;
	private MenuBar fileSubMenu;
	private MenuItem uploadFileItem;
	private MenuItemSeparator separator;
	private MenuItem deleteFileItem;
	private MenuItem updateFileItem;
	private HorizontalPanel dockCentrePanel;
	private SplitLayoutPanel splitLayoutPanel;
	private VerticalPanel splitVerticalPanel;
	private ScrollPanel scrollPanel;
	private Button crearButton;
	private MissionTree missionTree;
	private FoldersAndFilesTree foldersFilesTree;
	private VerticalPanel splitCentrePanel;
	private HorizontalPanel southPanel;
	private DisclosurePanel disclosureInfo;
	private VerticalPanel infoPanel;
	private VerticalPanel missionInfoPanel;
	private Label missionIdLabel;
	private Label missionNameLabel;
	private Label missionAliasLabel;
	private Label missionDescriptionLabel;
	private Label missionStartLabel;
	private Label missionEndLabel;
	private VerticalPanel fileInfoPanel;
	private Label fileIdLabel;
	private Label fileMissionLabel;
	private Label fileNameLabel;
	private Label fileDescriptionLabel;
	private Label fileDateLabel;
	private Label fileMD5Label;
	private DisclosurePanel disclosureHTML;
	private VideoWidget videoPlayer;
	private DisclosurePanel disclosureFlow;
	private FlowPlayer fp;
	private DisclosurePanel disclosureImage;
	private Image pic;
	private DisclosurePanel disclosureMap;
	private LatLng[] listaLatLng;
	private Polyline polilinea;
	private MapWidget map;
	private Marker mrk;
	private Label footerLabel;
	
	private HorizontalPanel horizontalPanel;
	private VerticalPanel buttonPanel;
	private HorizontalPanel missionButtonPanel;
	private Button addFileButton;
	private Button updateMissionButton;
	private Button deleteMissionButton;
	private HorizontalPanel fileButtonPanel;
	private Button updateFileButton;
	private Button deleteFileButton;
	
	private VerticalPanel mappingPanel;

	private Mission selectedMission;
	private File selectedFile;
	private TreeItem selectedCustomNode;
	private boolean isMissionSelected;
	private boolean isNothingSelected;
	
	private TreeServiceAsync treeService = GWT.create(TreeService.class);
	private DatabaseServiceAsync databaseService = GWT.create(DatabaseService.class);
	private XMLServiceAsync xmlService = GWT.create(XMLService.class);

	final HandlerManager eventBus = new HandlerManager(null);

	@Override
	public void onModuleLoad() {
		
		initializeBody();
		
		initializeHeader();
		
		initializeMenuBar();
		
		initializeEventBus();
		
//		initializeMissions();
		
		initializeHTML5video();
		
		initializeFlowplayer();
		
		initializeImageViewer();
		
		initializeMapViewer();
		
		treeService(null);
	}

	private void initializeEventBus() {
		eventBus.addHandler(PlaySelectedEvent.TYPE, new PlaySelectedEventHandler(){

			@Override
			public void onPlaySelected(final PlaySelectedEvent event) {
				selectedCustomNode = event.getNode();
				
				// Initialize the service proxy.
				if (databaseService == null) {
					databaseService = GWT.create(DatabaseService.class);
				}

				// Set up the callback object.
				AsyncCallback<File> callback = new AsyncCallback<File>() {

					@Override
					public void onFailure(Throwable caught) {
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("ERROR: No se ha podido acceder a la base de datos"));
						popup.setTitle("ERROR: No se ha podido acceder a la base de datos");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}

					@Override
					public void onSuccess(File result) {
						selectedFile = result;
						disableMissionMenu();
						initializeFileAditionalInfo();
						showFileDisclosure(event);
					}

					private void showFileDisclosure(final PlaySelectedEvent event) {
						// Video MP4 y FLV
						if (event.getPath().endsWith(".mp4") || event.getPath().endsWith(".flv")){
							stopPlayers();
							disclosureHTML.setOpen(false);
							disclosureFlow.setOpen(true);
							disclosureImage.setOpen(false);
							disclosureMap.setOpen(false);
							fp.play(event.getPath());
						}
						// GWT-HTML5-VIDEO
						// Video OGV y WEBM
						// Audio MP3 y OGG
						else if (event.getPath().endsWith(".mp3") || event.getPath().endsWith(".ogg") || event.getPath().endsWith(".ogv") || event.getPath().endsWith(".webm")){
							disclosureHTML.setOpen(true);
							disclosureFlow.setOpen(false);
							disclosureImage.setOpen(false);
							disclosureMap.setOpen(false);
							disclosureHTML.remove(videoPlayer);
							
							videoPlayer = new VideoWidget(true, true, "");
							List<VideoSource> sources = new ArrayList<VideoSource>();
							sources.add(new VideoSource(event.getPath()));
							videoPlayer.setSources(sources);
							if (event.getPath().endsWith(".ogv") || event.getPath().endsWith(".webm"))
								videoPlayer.setPixelSize(360, 240);
							else
								videoPlayer.setPixelSize(360, 140);
							disclosureHTML.setContent(videoPlayer);
						}
						// Imagen JPG y PNG
						else if (event.getPath().endsWith(".jpg") || event.getPath().endsWith(".png")){
							disclosureHTML.setOpen(false);
							disclosureFlow.setOpen(false);
							disclosureImage.setOpen(true);
							disclosureMap.setOpen(false);
							pic.setUrl(event.getPath());
							pic.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent evnt) {
									showPicOriginalSize(event.getPath());
								}
							});
						}
						// Coordenadas XML
						else if (event.getPath().endsWith(".xml")){
							disclosureInfo.setOpen(false);
							disclosureHTML.setOpen(false);
							disclosureFlow.setOpen(false);
							disclosureImage.setOpen(false);
							sizeOfRecorrido(1, event.getPath());
						}
						else {
							disclosureInfo.setOpen(false);
							disclosureHTML.setOpen(false);
							disclosureFlow.setOpen(false);
							disclosureImage.setOpen(false);
							disclosureMap.setOpen(false);
							errorFileFormatPopUp();
						}
					}

				};
				
				int id = ((FileCell) event.getNode().getUserObject()).getId();
				// Make the call to the database service.
				databaseService.selectFile(Integer.valueOf(id), callback);
			}
		});
		
		eventBus.addHandler(MissionEvent.TYPE, new MissionEventHandler(){

			@Override
			public void onMission(MissionEvent event) {
				switch(event.getAccion()){
				case CREATE:
					createMission(event.getMision());
					break;
				case UPDATE:
					updateMission(event.getMision());
					break;
				case DELETE:
					deleteMission(event.getMision().getId());
					break;
				}
			}

			private void createMission(Mission mission) {
				// Initialize the service proxy.
				if (databaseService == null) {
					databaseService = GWT.create(DatabaseService.class);
				}

				// Set up the callback object.
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("ERROR: No se ha podido crear la misión en el servidor"));
						popup.setTitle("ERROR: No se ha podido crear la misión en el servidor");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}

					@Override
					public void onSuccess(Void result) {
						disableBothMenus();
						treeService(null);
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("Misión creada satisfactoriamente"));
						popup.setTitle("Misión creada satisfactoriamente");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}
				};

				// Make the call to the database service.
				databaseService.insertMission(mission, callback);
			}
			
			private void updateMission(Mission mission) {
				// Initialize the service proxy.
				if (databaseService == null) {
					databaseService = GWT.create(DatabaseService.class);
				}

				// Set up the callback object.
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("ERROR: No se ha podido actualizar la misión en la base de datos"));
						popup.setTitle("ERROR: No se ha podido actualizar la misión en la base de datos");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}

					@Override
					public void onSuccess(Void result) {
						treeService(null);
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("Misión actualizada correctamente"));
						popup.setTitle("Misión actualizada correctamente");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}
				};

				// Make the call to the database service.
				databaseService.updateMission(mission, callback);
			}

			private void deleteMission(int id) {
				// Initialize the service proxy.
				if (databaseService == null) {
					databaseService = GWT.create(DatabaseService.class);
				}

				// Set up the callback object.
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("ERROR: No se ha podido borrar la misión"));
						popup.setTitle("ERROR: No se ha podido borrar la misión");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}

					@Override
					public void onSuccess(Void result) {
						disableBothMenus();
						disclosureInfo.clear();
						treeService(null);
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("Misión eliminada correctamente"));
						popup.setTitle("Misión eliminada correctamente");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}
				};

				// Make the call to the database service.
				databaseService.deleteMission(id, callback);
			}
			
		});
		
		eventBus.addHandler(SelectedMissionEvent.TYPE, new SelectedMissionEventHandler(){

			@Override
			public void onSelectedMission(SelectedMissionEvent event) {
				selectedCustomNode = event.getNode();
				showMissionButtons(((MissionCell)event.getNode().getUserObject()).getId());
			}
			
			private void showMissionButtons(int name) {
				// Initialize the service proxy.
				if (databaseService == null) {
					databaseService = GWT.create(DatabaseService.class);
				}

				// Set up the callback object.
				AsyncCallback<Mission> callback = new AsyncCallback<Mission>() {

					@Override
					public void onFailure(Throwable caught) {
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("ERROR: No se ha podido acceder a la base de datos"));
						popup.setTitle("ERROR: No se ha podido acceder a la base de datos");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}

					@Override
					public void onSuccess(Mission result) {
						selectedMission = result;
						disableFileMenu();
						initializeMissionAditionalInfo();
					}

				};

				// Make the call to the database service.
				databaseService.selectMission(Integer.valueOf(name), callback);
			}

		});
		
		eventBus.addHandler(FileEvent.TYPE, new FileEventHandler(){

			@Override
			public void onFile(FileEvent event) {
					switch(event.getAccion()){
					case CREATE:
						createFile(event.getFile());
						break;
					case UPDATE:
						updateFile(event.getFile());
						break;
					case DELETE:
						deleteFile(selectedFile);
						break;
					}
			}

			private void createFile(File file) {
				// Initialize the service proxy.
				if (databaseService == null) {
					databaseService = GWT.create(DatabaseService.class);
				}

				// Set up the callback object.
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("ERROR: No se ha podido añadir el archivo a la misión en el servidor"));
						popup.setTitle("ERROR: No se ha podido añadir el archivo a la misión en el servidor");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}

					@Override
					public void onSuccess(Void result) {
						disableBothMenus();
						treeService(null);
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("Archivo añadido satisfactoriamente"));
						popup.setTitle("Archivo añadido satisfactoriamente");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}
				};

				// Make the call to the database service.
				databaseService.insertFile(file, callback);
			}
			
			private void updateFile(File file) {
				// Initialize the service proxy.
				if (databaseService == null) {
					databaseService = GWT.create(DatabaseService.class);
				}

				// Set up the callback object.
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("ERROR: No se ha podido actualizar el archivo"));
						popup.setTitle("ERROR: No se ha podido actualizar el archivo");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}

					@Override
					public void onSuccess(Void result) {
						treeService(null);
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("Archivo actualizado correctamente"));
						popup.setTitle("Archivo actualizado correctamente");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}
				};

				// Make the call to the database service.
				databaseService.updateFile(file, callback);
			}

			private void deleteFile(File file) {
				// Initialize the service proxy.
				if (databaseService == null) {
					databaseService = GWT.create(DatabaseService.class);
				}

				// Set up the callback object.
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("ERROR: No se ha podido eliminar el archivo"));
						popup.setTitle("ERROR: No se ha podido eliminar el archivo");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}

					@Override
					public void onSuccess(Void result) {
						disableBothMenus();
						disclosureInfo.clear();
						treeService(null);
						DecoratedPopupPanel popup = new DecoratedPopupPanel();
						popup.add(new Label("Archivo eliminado satisfactoriamente"));
						popup.setTitle("Archivo eliminado satisfactoriamente");
						popup.setAutoHideEnabled(true);
						popup.setGlassEnabled(true);
						popup.center();
						popup.show();
					}
				};

				// Make the call to the database service.
				databaseService.deleteFile(file, callback);
			}
		});
		
		eventBus.addHandler(CategoryEvent.TYPE, new CategoryEventHandler(){

			@Override
			public void onCategory(CategoryEvent event) {
				disableBothMenus();
			}
			
		});

	}

	private void initializeBody() {
		images = (Images) GWT.create(Images.class);
		
		rootPanel = RootPanel.get();
		rootPanel.setStyleName("rootPanel");
		rootPanel.setSize("100%", "100%");
		
		dockPanel = new DockPanel();
		dockPanel.setStyleName("dockPanel");
		rootPanel.add(dockPanel, 0, 0);
		dockPanel.setSize("100%", "100%");
		
		headerImageNorth = new Image("");
		headerImageNorth.setStyleName("separadorHeaderNorth");
		dockPanel.add(headerImageNorth, DockPanel.NORTH);
		headerImageNorth.setSize("100%", "5px");
		
		northPanel = new HorizontalPanel();
		northPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		northPanel.setStyleName("mainHeader");
		dockPanel.add(northPanel, DockPanel.NORTH);
		northPanel.setSize("100%", "70px");
		
		versionLabel = new Label("Versión 0.1-SNAPSHOT");
		versionLabel.setStyleName("separadorHeaderSouth");
		dockPanel.add(versionLabel, DockPanel.NORTH);
		
		horizontalPanel = new HorizontalPanel();
		horizontalPanel.setStyleName("horizontalNorthPanel");
		dockPanel.add(horizontalPanel, DockPanel.NORTH);
		
		buttonPanel = new VerticalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.setStyleName("horizontalNorthPanel");
		horizontalPanel.add(buttonPanel);
		horizontalPanel.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		menuBar = new MenuBar(false);
		menuBar.setAnimationEnabled(true);
		buttonPanel.add(menuBar);
		
		missionButtonPanel = new HorizontalPanel();
		fileButtonPanel = new HorizontalPanel();
		
		dockCentrePanel = new HorizontalPanel();
		dockPanel.add(dockCentrePanel, DockPanel.CENTER);
		dockCentrePanel.setSize("100%", "100%");
		dockPanel.setCellHorizontalAlignment(dockCentrePanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		splitLayoutPanel = new SplitLayoutPanel();
		dockCentrePanel.add(splitLayoutPanel);
		splitLayoutPanel.setSize("100%", "800px");
		
		splitVerticalPanel = new VerticalPanel();
		splitVerticalPanel.setSpacing(10);
		splitLayoutPanel.addWest(splitVerticalPanel, 250.0);

		scrollPanel = new ScrollPanel();
		splitVerticalPanel.add(scrollPanel);
		
		splitCentrePanel = new VerticalPanel();
		splitCentrePanel.setSpacing(5);
		splitLayoutPanel.add(splitCentrePanel);
		splitCentrePanel.setSize("", "");
		
		disclosureInfo = new DisclosurePanel("Información adicional");
		disclosureInfo.setAnimationEnabled(true);
		disclosureInfo.setOpen(false);
		disclosureInfo.setVisible(true);
		splitCentrePanel.add(disclosureInfo);
		
		infoPanel = new VerticalPanel();
		disclosureInfo.setContent(infoPanel);
		
		disclosureHTML = new DisclosurePanel("HTML5 Player");
		disclosureHTML.setAnimationEnabled(true);
		disclosureHTML.setOpen(false);
		disclosureHTML.setVisible(true);
		splitCentrePanel.add(disclosureHTML);

		disclosureFlow = new DisclosurePanel("FlowPlayer");
		disclosureFlow.setAnimationEnabled(true);
		disclosureFlow.setOpen(false);
		disclosureFlow.setVisible(true);
		splitCentrePanel.add(disclosureFlow);
		
		disclosureImage = new DisclosurePanel("Image Viewer");
		disclosureImage.setAnimationEnabled(true);
		disclosureImage.setOpen(false);
		disclosureImage.setVisible(true);
		splitCentrePanel.add(disclosureImage);
		
		disclosureMap = new DisclosurePanel("Map Viewer");
		disclosureMap.setOpen(false);
		disclosureMap.setVisible(true);
		disclosureMap.setAnimationEnabled(true);
		splitCentrePanel.add(disclosureMap);
		
		mappingPanel = new VerticalPanel();
		disclosureMap.setContent(mappingPanel);
		
		southPanel = new HorizontalPanel();
		southPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dockPanel.add(southPanel, DockPanel.SOUTH);
		southPanel.setWidth("100%");
		
		footerLabel = new Label("New label");
		southPanel.add(footerLabel);
		footerLabel.setWidth("100%");
		southPanel.setCellHorizontalAlignment(footerLabel, HasHorizontalAlignment.ALIGN_CENTER);
		southPanel.setVisible(false);
	}
	
	private void initializeHeader() {
		logo = new Image(images.esferalia());
		northPanel.add(logo);
		logo.setSize("150px", "63px");
		
		titleLabel = new Label("ES3 - Electronic Signal Surveillance System");
		titleLabel.setStyleName("titleLabel");
		northPanel.add(titleLabel);
		northPanel.setCellHorizontalAlignment(titleLabel, HasHorizontalAlignment.ALIGN_RIGHT);
	}
	
	private void initializeMenuBar(){
		missionSubMenu = new MenuBar(true);
		missionItem = new MenuItem("Misión", false, missionSubMenu);
		missionItem.setHTML("Misión");
		newMissionItem = new MenuItem("Nueva", false, new Command(){
			@Override
			public void execute() {
				CreateMissionWidget createMissionWidget = new CreateMissionWidget(eventBus);
				createMissionWidget.setGlassEnabled(true);
				createMissionWidget.center();
				createMissionWidget.show();
			}
		});
		newMissionItem.setHTML("Nueva");
		missionSubMenu.addItem(newMissionItem);
		updateMissionItem = new MenuItem("Modificar", false, new Command(){
			@Override
			public void execute() {
				if(isNothingSelected)
					;// DO NOTHING
				else if (isMissionSelected){
					UpdateMissionWidget updateMissionWidget = new UpdateMissionWidget(eventBus, selectedMission);
					updateMissionWidget.setGlassEnabled(true);
					updateMissionWidget.center();
					updateMissionWidget.show();
				} else
					;// DO NOTHING
			}
		});
		updateMissionItem.setHTML("Modificar");
		missionSubMenu.addItem(updateMissionItem);
		deleteMissionItem = new MenuItem("Eliminar", false, new Command(){
			@Override
			public void execute() {
				if(isNothingSelected)
					;// DO NOTHING
				else if (isMissionSelected){
					DeleteMissionWidget deleteMissionWidget = new DeleteMissionWidget(eventBus, selectedMission.getId());
					deleteMissionWidget.setGlassEnabled(true);
					deleteMissionWidget.center();
					deleteMissionWidget.show();
				} else
					;// DO NOTHING
			}
		});
		deleteMissionItem.setHTML("Eliminar");
		missionSubMenu.addItem(deleteMissionItem);
		separator = new MenuItemSeparator();
		missionSubMenu.addSeparator(separator);
		uploadFileItem = new MenuItem("Subir archivo", false, new Command(){
			@Override
			public void execute() {
				if(isNothingSelected)
					;// DO NOTHING
				else if (isMissionSelected){
					CreateFileWidget addFileToMissionWidget = new CreateFileWidget(selectedMission.getId(), eventBus);
					addFileToMissionWidget.setGlassEnabled(true);
					addFileToMissionWidget.center();
					addFileToMissionWidget.show();
				} else
					;// DO NOTHING
			}
		});
		uploadFileItem.setHTML("Subir archivo");
		missionSubMenu.addItem(uploadFileItem);
		menuBar.addItem(missionItem);
		
		fileSubMenu = new MenuBar(true);
		fileItem = new MenuItem("Archivo", false, fileSubMenu);
		updateFileItem = new MenuItem("Modificar", false, new Command(){
			@Override
			public void execute() {
				if(isNothingSelected)
					;// DO NOTHING
				else if (!isMissionSelected){
					UpdateFileWidget updateFileWidget = new UpdateFileWidget(eventBus, selectedFile);
					updateFileWidget.setGlassEnabled(true);
					updateFileWidget.center();
					updateFileWidget.show();
				} else
					;// DO NOTHING
			}
		});
		updateFileItem.setHTML("Modificar");
		fileSubMenu.addItem(updateFileItem);
		deleteFileItem = new MenuItem("Eliminar", false, new Command(){
			@Override
			public void execute() {
				if(isNothingSelected)
					;// DO NOTHING
				else if (!isMissionSelected){
					DeleteFileWidget deleteFileWidget = new DeleteFileWidget(eventBus, selectedFile.getId());
					deleteFileWidget.setGlassEnabled(true);
					deleteFileWidget.center();
					deleteFileWidget.show();
				} else
					;// DO NOTHING
			}
		});
		deleteFileItem.setHTML("Eliminar");
		fileSubMenu.addItem(deleteFileItem);
		fileItem.setHTML("Archivo");
		menuBar.addItem(fileItem);
		
		disableBothMenus();
	}

	private void initializeMissionButtons() {
		buttonPanel.clear();
		missionButtonPanel = new HorizontalPanel();
		missionButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		missionButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		missionButtonPanel.setSpacing(10);
		buttonPanel.add(missionButtonPanel);
		buttonPanel.setCellVerticalAlignment(missionButtonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		buttonPanel.setCellHorizontalAlignment(missionButtonPanel, HasHorizontalAlignment.ALIGN_CENTER);
		addFileButton = new Button("Añadir archivo");
		addFileButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CreateFileWidget addFileToMissionWidget = new CreateFileWidget(selectedMission.getId(), eventBus);
				addFileToMissionWidget.setGlassEnabled(true);
				addFileToMissionWidget.center();
				addFileToMissionWidget.show();
			}
		});
		missionButtonPanel.add(addFileButton);
		updateMissionButton = new Button("Modificar");
		updateMissionButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				UpdateMissionWidget updateMissionWidget = new UpdateMissionWidget(eventBus, selectedMission);
				updateMissionWidget.setGlassEnabled(true);
				updateMissionWidget.center();
				updateMissionWidget.show();
			}
		});
		missionButtonPanel.add(updateMissionButton);
		deleteMissionButton = new Button("Borrar");
		deleteMissionButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				DeleteMissionWidget deleteMissionWidget = new DeleteMissionWidget(eventBus, selectedMission.getId());
				deleteMissionWidget.setGlassEnabled(true);
				deleteMissionWidget.center();
				deleteMissionWidget.show();
			}
		});
		missionButtonPanel.add(deleteMissionButton);
	}


	private void initializeFileButtons() {
		buttonPanel.clear();
		fileButtonPanel = new HorizontalPanel();
		fileButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		fileButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		fileButtonPanel.setSpacing(10);
		buttonPanel.add(fileButtonPanel);
		buttonPanel.setCellVerticalAlignment(fileButtonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		buttonPanel.setCellHorizontalAlignment(fileButtonPanel, HasHorizontalAlignment.ALIGN_CENTER);
		updateFileButton = new Button("Modificar");
		updateFileButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				UpdateFileWidget updateFileWidget = new UpdateFileWidget(eventBus, selectedFile);
				updateFileWidget.setGlassEnabled(true);
				updateFileWidget.center();
				updateFileWidget.show();
			}
		});
		fileButtonPanel.add(updateFileButton);
		deleteFileButton = new Button("Borrar");
		deleteFileButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				DeleteFileWidget deleteFileWidget = new DeleteFileWidget(eventBus, selectedFile.getId());
				deleteFileWidget.setGlassEnabled(true);
				deleteFileWidget.center();
				deleteFileWidget.show();
			}
		});
		fileButtonPanel.add(deleteFileButton);
	}


	private void initializeMissions() {
		crearButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				CreateMissionWidget createMissionWidget = new CreateMissionWidget(eventBus);
				createMissionWidget.setGlassEnabled(true);
				createMissionWidget.center();
				createMissionWidget.show();
			}
		});
	}

	
	private void initializeMissionAditionalInfo() {
		infoPanel.clear();
		missionInfoPanel = new VerticalPanel();
		infoPanel.add(missionInfoPanel);
		missionIdLabel = new Label("ID: " + selectedMission.getId());
		missionInfoPanel.add(missionIdLabel);
		missionNameLabel = new Label("Nombre: " + selectedMission.getName());
		missionInfoPanel.add(missionNameLabel);
		missionAliasLabel = new Label("Alias: " + selectedMission.getAlias());
		missionInfoPanel.add(missionAliasLabel);
		missionDescriptionLabel = new Label("Descripción: " + selectedMission.getDescription());
		missionInfoPanel.add(missionDescriptionLabel);
		missionStartLabel = new Label("Fecha inicio: " + selectedMission.getStart_date().getTime());
		missionInfoPanel.add(missionStartLabel);
		missionEndLabel = new Label("Fecha fin: " + selectedMission.getEnd_date().getTime());
		missionInfoPanel.add(missionEndLabel);
	}

	private void initializeFileAditionalInfo() {
		infoPanel.clear();
		fileInfoPanel = new VerticalPanel();
		infoPanel.add(fileInfoPanel);
		fileIdLabel = new Label("ID: " + selectedFile.getId());
		fileInfoPanel.add(fileIdLabel);
		fileMissionLabel = new Label("Corresponde a misión: " + selectedFile.getMission());
		fileInfoPanel.add(fileMissionLabel);
		fileNameLabel = new Label("Nombre: " + selectedFile.getName());
		fileInfoPanel.add(fileNameLabel);
		fileDescriptionLabel = new Label("Descripción: " + selectedFile.getDescription());
		fileInfoPanel.add(fileDescriptionLabel);
		fileDateLabel = new Label("Fecha: " + selectedFile.getDate_time().getTime());
		fileInfoPanel.add(fileDateLabel);
		fileMD5Label = new Label("MD5: " + selectedFile.getMD5());
		fileInfoPanel.add(fileMD5Label);
	}
	
	private void initializeHTML5video() {
		videoPlayer = new VideoWidget(true, true, "es3/pics/playbutton.png");
		// sources.add(new VideoSource("ES3/videos/bbb_trailer_400p.ogv"));
		// sources.add(new VideoSource("ES3/videos/bbb_trailer_360p.webm",	VideoType.WEBM));
		// videoPlayer.setSources(sources);
		videoPlayer.setPixelSize(360, 240);
		disclosureHTML.setContent(videoPlayer);
		disclosureHTML.addDomHandler(new ContextMenuHandler(){
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
	}

	private void initializeFlowplayer() {
		fp = new FlowPlayer();
		fp.setSize("360px", "240px");
		// fp.play("http://lighttpd.esferalia.net/mediaplayer-5.9/videos/01.Millenium.flv");
		// fp.play("http://192.168.2.107/mediaplayer-5.9/videos/01.Millenium.flv");
		// fp.play("es3/videos/big-buck-bunny.mp4");
		disclosureFlow.setContent(fp);
		disclosureFlow.addDomHandler(new ContextMenuHandler(){
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
	}

	private void initializeImageViewer() {
		pic = new Image("");
		pic.setSize("360px", "240px");
		// IDEA Disable Context Menu in Image Widget
		pic.addDomHandler(new ContextMenuHandler(){
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
		disclosureImage.setContent(pic);
	}
	
	private void initializeMapViewer() {
		// Código cambiado de lugar --> getCoordenadas/onSucces
		// Es necesario cargar el mapa cuando el Disclosure esté abierto
		// Si se hace aquí abrir/cargar/cerrar, se ve y queda mal
		// En su lugar, se abre y se crea el mapa cada vez que se hace clic sobre el objeto del árbol
		// En vez de utilizar siempre el mismo mapa y limpiarlo
	}
	
	private void treeService(final CustomNode selectedUINode) {
		
		// Initialize the service proxy.
		if (databaseService == null) {
			databaseService = GWT.create(DatabaseService.class);
		}

		// Set up the callback object.
		AsyncCallback<Vector<MissionCell>> callback = new AsyncCallback<Vector<MissionCell>>() {

			@Override
			public void onFailure(Throwable caught) {
				DecoratedPopupPanel popup = new DecoratedPopupPanel();
				popup.add(new Label("ERROR: No se han podido obtener los recursos del servidor"));
				popup.setTitle("ERROR: No se han podido obtener los recursos del servidor");
				popup.setAutoHideEnabled(true);
				popup.setGlassEnabled(true);
				popup.center();
				popup.show();
			}

			@Override
			public void onSuccess(Vector<MissionCell> result) {
				initializeTree(result, selectedUINode);
			}
			
		};
		databaseService.fillTree(callback);
	}
	
	private void initializeTree(Vector<MissionCell> missions, CustomNode selectedUINode) {
		scrollPanel.clear();
		missionTree = new MissionTree(missions, eventBus);
		scrollPanel.add(missionTree);
//		scrollPanel.onResize();
	}
	
	private void stopPlayers(){
		if(videoPlayer.isPlayed())
			videoPlayer.playPause();
	}

	private void showPicOriginalSize(String picPath) {
		disclosureHTML.setOpen(false);
		disclosureFlow.setOpen(false);
		ScrollPanel scroll = new ScrollPanel();
		Image pic = new Image(picPath);
		scroll.setSize("1000px", "500px");
		scroll.add(pic);
		DecoratedPopupPanel popup = new DecoratedPopupPanel();
		popup.addDomHandler(new ContextMenuHandler(){
			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, ContextMenuEvent.getType());
		popup.add(scroll);
		popup.setAutoHideEnabled(true);
		popup.setGlassEnabled(true);
		popup.center();
		popup.show();
	}

	private void errorFileFormatPopUp() {
		DecoratedPopupPanel popup = new DecoratedPopupPanel();
		popup.add(new Label("ERROR: El formato del archivo seleccionado no se puede reproducir"));
		popup.setTitle("ERROR: El formato del archivo seleccionado no se puede reproducir");
		popup.setAutoHideEnabled(true);
		popup.setGlassEnabled(true);
		popup.center();
		popup.show();
	}
	
	private void sizeOfRecorrido(final int index, final String name) {
		// TODO Único recorrido por XML, ¿merece la pena tener las dos funciones?
		// Set up the callback object.
		AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				DecoratedPopupPanel popup = new DecoratedPopupPanel();
				popup.setTitle("ERROR: No se han podido obtener los datos del recorrido");
				popup.add(new Label("ERROR: No se han podido obtener los datos del recorrido"));
				popup.setAutoHideEnabled(true);
				popup.setGlassEnabled(true);
				popup.center();
				popup.show();
			}

			@Override
			public void onSuccess(Integer result) {
				listaLatLng = new LatLng[result.intValue()];
				getCoordenadas(index, name);
			}
		};

		// Make the call to the stock price service.
		xmlService.sizeOfRecorrido(index, name, callback);
	}
	
	private void getCoordenadas(int selected, String name) {
		// Set up the callback object.
		AsyncCallback<Vector<Coordenada>> callback = new AsyncCallback<Vector<Coordenada>>() {

			@Override
			public void onFailure(Throwable caught) {
				DecoratedPopupPanel popup = new DecoratedPopupPanel();
				popup.setTitle("ERROR: No se han podido obtener las coordenadas");
				popup.add(new Label("ERROR: No se han podido obtener las coordenadas"));
				popup.setAutoHideEnabled(true);
				popup.setGlassEnabled(true);
				popup.center();
				popup.show();
			}

			@Override
			public void onSuccess(final Vector<Coordenada> result) {
				Maps.loadMapsApi("", "2", false, new Runnable() {
					public void run() {
						mappingPanel.clear();
						disclosureMap.setOpen(true);
						mappingPanel.add(buildUi());
					}

					private Widget buildUi() {
						map = new MapWidget();
						map.setSize("400px", "400px");
						// añadimos control selector de tipo de mapa
						map.addControl(new MapTypeControl());
						// añadimos control de desplazamiento con zoom
						map.addControl(new LargeMapControl());
						// añadimos control escala de mapa
						map.addControl(new ScaleControl());
						// permitimos zoom com ratón
						map.setScrollWheelZoomEnabled(true);

						// limpiamos el mapa antes de introducir los nuevos datos
						map.clearOverlays();
						
						// creamos las coordenadas
						int i = 0;
						Iterator<Coordenada> it = result.iterator();
						while(it.hasNext()){
							Coordenada coord = it.next();
							listaLatLng[i] = LatLng.newInstance(coord.getLatitud(), coord.getLongitud());
							i++;
						}
						// creamos la polilínea
						polilinea = new Polyline(listaLatLng);
						
						// mostramos el mapa centrado con las coordenas iniciales
						map.setCenter(listaLatLng[0]);

						// establecemos en nivel de zoom
						map.setZoomLevel(12);
						
						// creamos un marcador en la coordenada inicial
						/*mrk = new Marker(listaLatLng[0]);
						map.addOverlay(mrk);
						mrk.addMarkerClickHandler(new MarkerClickHandler() {

							@Override
							public void onClick(MarkerClickEvent event) {
								// Widget en InfoWindow Normal
								Image img = new Image();
								img.setUrl("http://lh6.ggpht.com/_oxEB1W000Zc/S5sCOsecjdI/AAAAAAAAAGg/_CDb-vUE7gs/s800/13%20DE%20SET.%20%20PLAZA%20ATMAT.JPG");
								img.setWidth("235px");
								img.setHeight("267px");

								// Widget en InfoWindow Maximizado
								HTML video = new HTML(
										"<object width='180' height='160'><param name='movie' value='http://www.youtube.com/v/8qp_VSmV17I&hl=es_ES&fs=1&rel=0'></param><param name='allowFullScreen' value='true'></param><param name='allowscriptaccess' value='always'></param><embed src='http://www.youtube.com/v/8qp_VSmV17I&hl=es_ES&fs=1&rel=0' type='application/x-shockwave-flash' allowscriptaccess='always' allowfullscreen='false' width='180' height='160'></embed></object>");
								HTML album = new HTML(
										"<embed type='application/x-shockwave-flash' src='http://picasaweb.google.com/s/c/bin/slideshow.swf' width='180' height='160' flashvars='host=picasaweb.google.com&hl=es&feat=flashalbum&RGB=0x000000&feed=http%3A%2F%2Fpicasaweb.google.com%2Fdata%2Ffeed%2Fapi%2Fuser%2FVictorCabreraZolla%2Falbumid%2F5447950544182068785%3Falt%3Drss%26kind%3Dphoto%26hl%3Des' pluginspage='http://www.macromedia.com/go/getflashplayer'></embed>");
								HorizontalPanel hp = new HorizontalPanel();
								hp.setSpacing(20);

								VerticalPanel vp = new VerticalPanel();
								vp.add(new HTML("video"));
								vp.add(video);
								vp.add(new HTML("diapositiva"));
								vp.add(album);
								hp.add(new HTML(
										"<p style=\"text-align: justify;\">Este parque se construyo como un esfuerzo mancomunado del Sr. Alcalde  Jorge Jumanor  con los vecinos, haciendo realidad un sueño muchas veces  postergado por las anteriores autoridadede de Turno</p><p style=\"text-align: justify;\">La Obra se empezo  a contruir a inicios del año 2009 culminandose satisfactoriamente a  mediados del  2009 en el aniversario del Distrito Gregorio  Albarracion  Lanchipa</p>"));
								hp.add(vp);

								// creamos la Ventana de Informacion
								InfoWindowContent info = new InfoWindowContent(img);
								info.setMaxTitle("Parque Perez Gamboa");
								info.setMaxContent(hp);
								map.getInfoWindow().open(mrk.getLatLng(), info);
							}
						});*/
						map.addOverlay(polilinea);
						return map;
					}
				});
			}
		};

		// Make the call to the stock price service.
		xmlService.getCoordenadas(Integer.toString(selected), name, callback);
	}
	
	private void disableFileMenu() {
		isMissionSelected = true;
		isNothingSelected = false;
		
		updateMissionItem.setEnabled(true);
		deleteMissionItem.setEnabled(true);
		uploadFileItem.setEnabled(true);
		
		updateFileItem.setEnabled(false);
		deleteFileItem.setEnabled(false);
	}
	
	private void disableMissionMenu() {
		isMissionSelected = false;
		isNothingSelected = false;
		
		updateMissionItem.setEnabled(false);
		deleteMissionItem.setEnabled(false);
		uploadFileItem.setEnabled(false);
		
		updateFileItem.setEnabled(true);
		deleteFileItem.setEnabled(true);
	}
	
	private void disableBothMenus() {
		isNothingSelected = true;
		
		updateMissionItem.setEnabled(false);
		deleteMissionItem.setEnabled(false);
		uploadFileItem.setEnabled(false);
		
		updateFileItem.setEnabled(false);
		deleteFileItem.setEnabled(false);
	}
	
}
