package com.esferalia.es3.demo.client;

import java.util.ArrayList;
import java.util.List;

import com.esferalia.es3.demo.client.dto.CustomNode;
import com.esferalia.es3.demo.client.dto.FoldersAndFilesTree;
import com.esferalia.es3.demo.client.event.PlaySelectedEvent;
import com.esferalia.es3.demo.client.event.PlaySelectedEventHandler;
import com.esferalia.es3.demo.client.flowplayer.FlowPlayer;

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

import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoWidget;

public class StreamingWeb implements EntryPoint {
	
	private final String BASE_PATH = "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\mission";

	private Images images;
	private RootPanel rootPanel;
	private DockPanel dockPanel;
	private HorizontalPanel northPanel;
	private HorizontalPanel dockCentrePanel;
	private SplitLayoutPanel splitLayoutPanel;
	private ScrollPanel scrollPanel;
	private VerticalPanel splitCentrePanel;
	private HorizontalPanel southPanel;
//	private VerticalPanel verticalPanel_3;
	private DisclosurePanel disclosureHTML;
//	private VerticalPanel verticalPanel_6;
	private DisclosurePanel disclosureFlow;
	private DisclosurePanel disclosureImage;
	private Image logo;
	private Label titleLabel;
	private FoldersAndFilesTree foldersFilesTree;
	private VideoWidget videoPlayer;
	private FlowPlayer fp;
	private Image pic;
	private Label footerLabel;
	
	private GreetingServiceAsync greetingSvc = GWT.create(GreetingService.class);

	final HandlerManager eventBus = new HandlerManager(null);
	

	@Override
	public void onModuleLoad() {
		
		initializeBody();
		
		initializeHeader();
		
		initializeEventBus();
		
		initializeHTML5video();
		
		initializeFlowplayer();
		
		initializeImageViewer();
		
		treeService();
	}

	private void initializeEventBus() {
		eventBus.addHandler(PlaySelectedEvent.TYPE, new PlaySelectedEventHandler(){
			@Override
			public void onPlaySelected(final PlaySelectedEvent event) {
				// Video MP4 y FLV
				if (event.getPath().endsWith(".mp4") || event.getPath().endsWith(".flv")){
					stopPlayers();
					// OLD verticalPanel_3 y _6
//					verticalPanel_6.setVisible(true);
//					verticalPanel_3.setVisible(false);
					disclosureHTML.setOpen(false);
					disclosureFlow.setOpen(true);
					disclosureImage.setOpen(false);
					fp.play(event.getPath());
				}
				// GWT-HTML5-VIDEO
				// Video OGV y WEBM
				// Audio MP3 y OGG
				else if (event.getPath().endsWith(".mp3") || event.getPath().endsWith(".ogg") || event.getPath().endsWith(".ogv") || event.getPath().endsWith(".webm")){
					// OLD verticalPanel_3 y _6
//					verticalPanel_6.setVisible(false);
//					verticalPanel_3.setVisible(true);
//					verticalPanel_3.remove(videoPlayer);
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
					// OLD verticalPanel_3 y _6
//					verticalPanel_3.add(videoPlayer);
//					verticalPanel_3.setCellHorizontalAlignment(videoPlayer, HasHorizontalAlignment.ALIGN_CENTER);
					disclosureHTML.setContent(videoPlayer);
				}
				// Imagen JPG y PNG
				else if (event.getPath().endsWith(".jpg") || event.getPath().endsWith(".png")){
					// OLD verticalPanel_3 y _6
//					verticalPanel_6.setVisible(false);
//					verticalPanel_3.setVisible(false);
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
					// OLD verticalPanel_3 y _6
//					verticalPanel_6.setVisible(false);
//					verticalPanel_3.setVisible(false);
					disclosureHTML.setOpen(false);
					disclosureFlow.setOpen(false);
					disclosureImage.setOpen(false);
					errorFileFormatPopUp();
				}
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
		
		dockCentrePanel = new HorizontalPanel();
		dockPanel.add(dockCentrePanel, DockPanel.CENTER);
		dockCentrePanel.setSize("", "800px");
		dockPanel.setCellHorizontalAlignment(dockCentrePanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		splitLayoutPanel = new SplitLayoutPanel();
		dockCentrePanel.add(splitLayoutPanel);
		splitLayoutPanel.setSize("1200px", "800px");
		
		scrollPanel = new ScrollPanel();
		splitLayoutPanel.addWest(scrollPanel, 250.0);
		
		splitCentrePanel = new VerticalPanel();
		splitLayoutPanel.add(splitCentrePanel);
		splitCentrePanel.setSize("", "");
		
		// OLD verticalPanel_3
//		verticalPanel_3 = new VerticalPanel();
//		verticalPanel_3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); verticalPanel_3.setSpacing(10);
//		Label lblNewLabel_3 = new Label("HTML5");
//		lblNewLabel_3.setStyleName("h3");
//		disclosureHTML.add(lblNewLabel_3);
//		verticalPanel_3.setCellHorizontalAlignment(lblNewLabel_3, HasHorizontalAlignment.ALIGN_CENTER);
//		verticalPanel_3.setVisible(false);
//		splitCentrePanel.add(verticalPanel_3);
		disclosureHTML = new DisclosurePanel("HTML5 Player");
		disclosureHTML.setAnimationEnabled(true);
		disclosureHTML.setOpen(false);
		disclosureHTML.setVisible(true);
		splitCentrePanel.add(disclosureHTML);
		
		// OLD verticalPanel_6
//		verticalPanel_6 = new VerticalPanel();
//		verticalPanel_6.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); verticalPanel_6.setSpacing(10);
//		Label lblNewLabel_6 = new Label("FlowPlayer");
//		lblNewLabel_6.setStyleName("h3"); verticalPanel_6.add(lblNewLabel_6);
//		verticalPanel_6.setCellHorizontalAlignment(lblNewLabel_6, HasHorizontalAlignment.ALIGN_CENTER);
//		verticalPanel_6.setVisible(false);
//		splitCentrePanel.add(verticalPanel_6);
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
		
		titleLabel = new Label("Streaming Player");
		titleLabel.setStyleName("h1");
		titleLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		northPanel.add(titleLabel);
		titleLabel.setWidth("668px");
		northPanel.setCellHorizontalAlignment(titleLabel, HasHorizontalAlignment.ALIGN_CENTER);
	}
	
	private void initializeHTML5video() {
		videoPlayer = new VideoWidget(true, true, "es3/pics/play_button_big.jpg");
		// sources.add(new VideoSource("ES3/videos/bbb_trailer_400p.ogv"));
		// sources.add(new VideoSource("ES3/videos/bbb_trailer_360p.webm",	VideoType.WEBM));
		// videoPlayer.setSources(sources);
		videoPlayer.setPixelSize(360, 240);
		// OLD verticalPanel_3
//		verticalPanel_3.add(videoPlayer);
//		verticalPanel_3.setCellHorizontalAlignment(videoPlayer, HasHorizontalAlignment.ALIGN_CENTER);
		disclosureHTML.setContent(videoPlayer);
	}

	private void initializeFlowplayer() {
		fp = new FlowPlayer();
		fp.setSize("360px", "240px");
		// fp.play("http://lighttpd.esferalia.net/mediaplayer-5.9/videos/01.Millenium.flv");
		// fp.play("http://192.168.2.107/mediaplayer-5.9/videos/01.Millenium.flv");
		// fp.play("es3/videos/big-buck-bunny.mp4");
		// OLD verticalPanel_6
//		verticalPanel_6.add(fp);
//		verticalPanel_6.setCellHorizontalAlignment(fp, HasHorizontalAlignment.ALIGN_CENTER);
		disclosureFlow.setContent(fp);
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
				popup.setTitle("ERROR: No se han podido obtener los recursos del servidor");
				popup.setAutoHideEnabled(true);
				popup.setGlassEnabled(true);
				popup.center();
				popup.show();
			}
		};

		// Make the call to the stock price service.
		// FIXME directoryPath
		greetingSvc.greetServer(BASE_PATH, callback);
//		greetingSvc.greetServer("/srv/www/lighttpd/public", callback);
	}
	
	private void initializeTree(CustomNode folderTree) {
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
		popup.add(scroll);
		popup.setAutoHideEnabled(true);
		popup.setGlassEnabled(true);
		popup.center();
		popup.show();
	}

	private void errorFileFormatPopUp() {
		DecoratedPopupPanel popup = new DecoratedPopupPanel();
		popup.add(new Image("es3/pics/pantallazo.png"));
		popup.setAutoHideEnabled(true);
		popup.setGlassEnabled(true);
		popup.center();
		popup.show();
	}
	
}
