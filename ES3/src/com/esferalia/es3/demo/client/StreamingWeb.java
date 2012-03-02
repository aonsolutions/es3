package com.esferalia.es3.demo.client;

import java.util.ArrayList;
import java.util.List;

import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.dto.Mission;
import com.esferalia.es3.demo.client.event.FileEvent;
import com.esferalia.es3.demo.client.event.FileEventHandler;
import com.esferalia.es3.demo.client.event.MissionEvent;
import com.esferalia.es3.demo.client.event.MissionEventHandler;
import com.esferalia.es3.demo.client.event.PlaySelectedEvent;
import com.esferalia.es3.demo.client.event.PlaySelectedEventHandler;
import com.esferalia.es3.demo.client.event.SelectedMissionEvent;
import com.esferalia.es3.demo.client.event.SelectedMissionEventHandler;
import com.esferalia.es3.demo.client.flowplayer.FlowPlayer;
import com.esferalia.es3.demo.client.tree.CustomNode;
import com.esferalia.es3.demo.client.tree.FoldersAndFilesTree;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Button;

import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoWidget;

public class StreamingWeb implements EntryPoint {
	
	// FIXME directoryPath
	private final String BASE_PATH = "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\mission";

	private Images images;
	private RootPanel rootPanel;
	private DockPanel dockPanel;
	private HorizontalPanel northPanel;
	private Image logo;
	private Label titleLabel;
	private HorizontalPanel dockCentrePanel;
	private SplitLayoutPanel splitLayoutPanel;
	private VerticalPanel splitVerticalPanel;
	private ScrollPanel scrollPanel;
	private Button crearButton;
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
	private Label footerLabel;
	
	private HorizontalPanel horizontalPanel;
	private HorizontalPanel titlePanel;
	private Label missionLabel;
	private HorizontalPanel missionButtonPanel;
	private Button addFileButton;
	private Button updateMissionButton;
	private Button deleteMissionButton;
	private HorizontalPanel fileButtonPanel;
	private Button updateFileButton;
	private Button deleteFileButton;

	private Mission selectedMission;
	private File selectedFile;
	
	private GreetingServiceAsync greetingSvc = GWT.create(GreetingService.class);
	private DatabaseServiceAsync databaseService = GWT.create(DatabaseService.class);

	final HandlerManager eventBus = new HandlerManager(null);
	private VerticalPanel buttonPanel;

	@Override
	public void onModuleLoad() {
		
		initializeBody();
		
		initializeHeader();
		
		initializeEventBus();
		
		initializeMissions();
		
		initializeHTML5video();
		
		initializeFlowplayer();
		
		initializeImageViewer();
		
		treeService();
	}

	private void initializeEventBus() {
		eventBus.addHandler(PlaySelectedEvent.TYPE, new PlaySelectedEventHandler(){
			@Override
			public void onPlaySelected(final PlaySelectedEvent event) {
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
						initializeFileButtons();
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
							fp.play(event.getPath());
						}
						// GWT-HTML5-VIDEO
						// Video OGV y WEBM
						// Audio MP3 y OGG
						else if (event.getPath().endsWith(".mp3") || event.getPath().endsWith(".ogg") || event.getPath().endsWith(".ogv") || event.getPath().endsWith(".webm")){
							disclosureHTML.setOpen(true);
							disclosureFlow.setOpen(false);
							disclosureImage.setOpen(false);
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
							pic.setUrl(event.getPath());
							pic.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent evnt) {
									showPicOriginalSize(event.getPath());
								}
							});
						}
						else {
							disclosureHTML.setOpen(false);
							disclosureFlow.setOpen(false);
							disclosureImage.setOpen(false);
							errorFileFormatPopUp();
						}
					}

				};
				
				String[] split = event.getNameFile().split("\\.");
				String id = split[0];
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
						// TODO Mostrar la misión creada seleccionada en el árbol
						treeService();
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
						// TODO Mostrar la misión actualizada seleccionada en el árbol
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
						disclosureInfo.clear();
						buttonPanel.clear();
						treeService();
					}
				};

				// Make the call to the database service.
				databaseService.deleteMission(id, callback);
			}
			
		});
		
		eventBus.addHandler(SelectedMissionEvent.TYPE, new SelectedMissionEventHandler(){

			@Override
			public void onSelectedMission(SelectedMissionEvent event) {
				showMissionButtons(event.getName());
			}
			
			private void showMissionButtons(String name) {
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
						initializeMissionButtons();
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
						// TODO Mostrar el archivo creado seleccionado en el árbol
						treeService();
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
						// TODO Mostrar el archivo actualizado seleccionado en el árbol
						treeService();
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
						buttonPanel.clear();
						disclosureInfo.clear();
						treeService();
					}
				};

				// Make the call to the database service.
				databaseService.deleteFile(file, callback);
			}
		});
	}

	private void initializeBody() {
		images = (Images) GWT.create(Images.class);
		
		rootPanel = RootPanel.get();
		rootPanel.setSize("900px", "900px");
		
		dockPanel = new DockPanel();
		rootPanel.add(dockPanel, 0, 0);
		dockPanel.setSize("900px", "900px");
		
		northPanel = new HorizontalPanel();
		dockPanel.add(northPanel, DockPanel.NORTH);
		northPanel.setSize("900px", "100px");
		
		horizontalPanel = new HorizontalPanel();
		dockPanel.add(horizontalPanel, DockPanel.NORTH);
		horizontalPanel.setSize("900px", "50px");
		
		titlePanel = new HorizontalPanel();
		titlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		titlePanel.setSpacing(10);
		horizontalPanel.add(titlePanel);
		horizontalPanel.setCellVerticalAlignment(titlePanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		missionLabel = new Label("Misiones");
		titlePanel.add(missionLabel);
		horizontalPanel.setCellVerticalAlignment(missionLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		buttonPanel = new VerticalPanel();
		buttonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.add(buttonPanel);
		horizontalPanel.setCellVerticalAlignment(buttonPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		missionButtonPanel = new HorizontalPanel();
		fileButtonPanel = new HorizontalPanel();
		
		dockCentrePanel = new HorizontalPanel();
		dockPanel.add(dockCentrePanel, DockPanel.CENTER);
		dockCentrePanel.setSize("", "800px");
		dockPanel.setCellHorizontalAlignment(dockCentrePanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		splitLayoutPanel = new SplitLayoutPanel();
		dockCentrePanel.add(splitLayoutPanel);
		splitLayoutPanel.setSize("900px", "800px");
		
		splitVerticalPanel = new VerticalPanel();
		splitVerticalPanel.setSpacing(10);
		splitLayoutPanel.addWest(splitVerticalPanel, 250.0);
		
		crearButton = new Button("Crear");
		splitVerticalPanel.add(crearButton);
		splitVerticalPanel.setCellVerticalAlignment(crearButton, HasVerticalAlignment.ALIGN_MIDDLE);
		
		scrollPanel = new ScrollPanel();
		splitVerticalPanel.add(scrollPanel);
		
		splitCentrePanel = new VerticalPanel();
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
		
		southPanel = new HorizontalPanel();
		southPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dockPanel.add(southPanel, DockPanel.SOUTH);
		southPanel.setWidth("900px");
		
		footerLabel = new Label("New label");
		southPanel.add(footerLabel);
		footerLabel.setWidth("900px");
		southPanel.setCellHorizontalAlignment(footerLabel, HasHorizontalAlignment.ALIGN_CENTER);
		southPanel.setVisible(false);
	}
	
	private void initializeHeader() {
		logo = new Image(images.esferalia());
		northPanel.add(logo);
		logo.setSize("100px", "100px");
		
		titleLabel = new Label("ES3 - Electronic Signal Surveillance System");
		titleLabel.setStyleName("h1");
		titleLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		northPanel.add(titleLabel);
		titleLabel.setWidth("668px");
		northPanel.setCellHorizontalAlignment(titleLabel, HasHorizontalAlignment.ALIGN_CENTER);
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
		videoPlayer = new VideoWidget(true, true, "es3/pics/play_button_big.jpg");
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
	
	private void treeService() {
		// Initialize the service proxy.
		if (greetingSvc == null) {
			greetingSvc = GWT.create(GreetingService.class);
		}

		// Set up the callback object.
		AsyncCallback<CustomNode> callback = new AsyncCallback<CustomNode>() {

			@Override
			public void onSuccess(CustomNode result) {
				initializeTree(result);
			}

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
		};

		// Make the call to the greeting service.
		// FIXME directoryPath
		greetingSvc.greetServer(BASE_PATH, callback);
//		greetingSvc.greetServer("/srv/www/lighttpd/public", callback);
	}
	
	private void initializeTree(CustomNode folderTree) {
		scrollPanel.clear();
		foldersFilesTree = new FoldersAndFilesTree(eventBus, folderTree);
		scrollPanel.add(foldersFilesTree);
		scrollPanel.onResize();
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
	
}
