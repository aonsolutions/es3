package com.esferalia.es3.demo.client;

import java.util.ArrayList;
import java.util.List;

import com.esferalia.es3.demo.client.event.PlaySelectedEvent;
import com.esferalia.es3.demo.client.event.PlaySelectedEventHandler;
import com.esferalia.es3.demo.client.flowplayer.FlowPlayer;
import com.esferalia.es3.demo.client.tree.CustomNode;
import com.esferalia.es3.demo.client.tree.FoldersAndFilesTree;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoWidget;

public class ES3 implements EntryPoint {

	private RootPanel rootPanel;
	private Label lblNewLabel_2;
	private VerticalPanel verticalPanel;
	private VerticalPanel verticalPanel_1;
	private VerticalPanel verticalPanel_3;
	private VerticalPanel verticalPanel_4;
	private VerticalPanel verticalPanel_6;
	private HorizontalPanel horizontalPanel;
	private StackLayoutPanel stackLayoutPanel;
	private FoldersAndFilesTree foldersFilesTree;
	private VideoWidget videoPlayer;
	private FlowPlayer fp;
	
	final HandlerManager eventBus = new HandlerManager(null);
	
	private GreetingServiceAsync greetingSvc = GWT.create(GreetingService.class);

	public void onModuleLoad() {

		rootPanel = RootPanel.get();
		
		eventBus.addHandler(PlaySelectedEvent.TYPE, new PlaySelectedEventHandler(){
			@Override
			public void onPlaySelected(PlaySelectedEvent event) {
//				lblNewLabel_2.setText(event.getPath());
				// Video MP4 y FLV
				if (event.getPath().endsWith(".mp4") || event.getPath().endsWith(".flv")){
					stopPlayers();
					verticalPanel_6.setVisible(true);
					verticalPanel_3.setVisible(false);
					fp.play(event.getPath());
				}
				// GWT-HTML5-VIDEO
				// Video OGV y WEBM
				// Audio MP3 y OGG
				else if (event.getPath().endsWith(".mp3") || event.getPath().endsWith(".ogg") || event.getPath().endsWith(".ogv") || event.getPath().endsWith(".webm")){
					verticalPanel_6.setVisible(false);
					verticalPanel_3.setVisible(true);
					verticalPanel_3.remove(videoPlayer);
					videoPlayer = new VideoWidget(true, true, "es3/pics/audio.jpg");
					List<VideoSource> sources = new ArrayList<VideoSource>();
					sources.add(new VideoSource(event.getPath()));
					videoPlayer.setSources(sources);
					if (event.getPath().endsWith(".ogv") || event.getPath().endsWith(".webm"))
						videoPlayer.setPixelSize(720, 480);
					else
						videoPlayer.setPixelSize(400, 0);
					verticalPanel_3.add(videoPlayer);
					verticalPanel_3.setCellHorizontalAlignment(videoPlayer, HasHorizontalAlignment.ALIGN_CENTER);
				}
				// Imagen JPG y PNG
				else if (event.getPath().endsWith(".jpg") || event.getPath().endsWith(".png")){
					verticalPanel_6.setVisible(false);
					verticalPanel_3.setVisible(false);
					showPicOriginalSize(event.getPath());
				}
				else {
					verticalPanel_6.setVisible(false);
					verticalPanel_3.setVisible(false);
					errorFileFormatPopUp();
				}
			}
		});

		initializeWebBody();
		
//		initializeStackLayoutPanel();

		initializeHTML5video();

		initializeFlowplayer();
		
		mockServiceTest();
	}


	private void mockServiceTest() {
		// Initialize the service proxy.
		if (greetingSvc == null) {
			greetingSvc = GWT.create(GreetingService.class);
		}

		// Set up the callback object.
		AsyncCallback<CustomNode> callback = new AsyncCallback<CustomNode>() {

			@Override
			public void onSuccess(CustomNode result) {
				construirStackPanel(result);
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
		greetingSvc.greetServer("C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public", callback);
//		greetingSvc.greetServer("/srv/www/lighttpd/public", callback);
	}
	
	private void construirStackPanel(CustomNode folderTree) {
		foldersFilesTree = new FoldersAndFilesTree(eventBus, folderTree);
		verticalPanel_4.add(foldersFilesTree);
		verticalPanel_4.setCellHorizontalAlignment(foldersFilesTree, HasHorizontalAlignment.ALIGN_LEFT);
	}

	private void initializeWebBody() {
		verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setSize("688px", "512px");
		lblNewLabel_2 = new Label("Streaming Player");
		lblNewLabel_2.setStyleName("h1");
		lblNewLabel_2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_1.add(lblNewLabel_2);
		rootPanel.add(verticalPanel_1, 10, 10);
		
		horizontalPanel = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel);
		
		verticalPanel_4 = new VerticalPanel();
		verticalPanel_4.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); 
		Label lblNewLabel_4 = new Label("Folders And Files Tree");
		lblNewLabel_4.setStyleName("h3");
		verticalPanel_4.add(lblNewLabel_4);
		verticalPanel_4.setCellHorizontalAlignment(lblNewLabel_4, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_4.setVisible(true);
		horizontalPanel.add(verticalPanel_4);
		
		verticalPanel = new VerticalPanel();
		horizontalPanel.add(verticalPanel);
		verticalPanel.setSpacing(20);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setSize("1000", "1000");
		
		verticalPanel_3 = new VerticalPanel();
		verticalPanel_3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); verticalPanel_3.setSpacing(10);
		verticalPanel.add(verticalPanel_3);
		verticalPanel.setCellHorizontalAlignment(verticalPanel_3, HasHorizontalAlignment.ALIGN_CENTER);
		Label lblNewLabel_3 = new Label("HTML5");
		lblNewLabel_3.setStyleName("h3");
		verticalPanel_3.add(lblNewLabel_3);
		verticalPanel_3.setCellHorizontalAlignment(lblNewLabel_3, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_3.setVisible(false);
		
		verticalPanel_6 = new VerticalPanel();
		verticalPanel_6.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER); verticalPanel_6.setSpacing(10);
		verticalPanel.add(verticalPanel_6);
		verticalPanel.setCellHorizontalAlignment(verticalPanel_6, HasHorizontalAlignment.ALIGN_CENTER);
		Label lblNewLabel_6 = new Label("FlowPlayer");
		lblNewLabel_6.setStyleName("h3"); verticalPanel_6.add(lblNewLabel_6);
		verticalPanel_6.setCellHorizontalAlignment(lblNewLabel_6, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_6.setVisible(false);
	}
	
	private void initializeStackLayoutPanel() {
		// Get the images.
		Images images = (Images) GWT.create(Images.class);
		
		// Add the Mail folders.
		Widget videoHeader = createHeaderWidget("Video", images.video());
		stackLayoutPanel.add(createVideoItem(), videoHeader, 4);

		// Add a list of filters.
		Widget audioHeader = createHeaderWidget("Audio", images.audio());
		stackLayoutPanel.add(createAudioItem(), audioHeader, 4);

		// Add a list of contacts.
		Widget imageHeader = createHeaderWidget("Imagen",images.image());
		stackLayoutPanel.add(createImageItem(), imageHeader, 4);
	}
	
	private void initializeHTML5video() {
		videoPlayer = new VideoWidget(true, true, "es3/pics/play_button_big.jpg");
		// sources.add(new VideoSource("ES3/videos/bbb_trailer_400p.ogv"));
		// sources.add(new VideoSource("ES3/videos/bbb_trailer_360p.webm",	VideoType.WEBM));
		// videoPlayer.setSources(sources);
		// videoPlayer.setPixelSize(300, 0);
		verticalPanel_3.add(videoPlayer);
		verticalPanel_3.setCellHorizontalAlignment(videoPlayer, HasHorizontalAlignment.ALIGN_CENTER);
	}

	private void initializeFlowplayer() {
		fp = new FlowPlayer();
		fp.setSize("720px", "480px");
		// fp.play("http://lighttpd.esferalia.net/mediaplayer-5.9/videos/01.Millenium.flv");
		// fp.play("http://192.168.2.107/mediaplayer-5.9/videos/01.Millenium.flv");
		// fp.play("es3/videos/big-buck-bunny.mp4");
		verticalPanel_6.add(fp);
		verticalPanel_6.setCellHorizontalAlignment(fp, HasHorizontalAlignment.ALIGN_CENTER);
	}


	private Widget createHeaderWidget(String text, ImageResource image) {
		// Add the image and text to a horizontal panel
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setHeight("100%");
		hPanel.setSpacing(5);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.add(new Image(image));
		HTML headerText = new HTML(text);
		hPanel.add(headerText);
		return new SimplePanel(hPanel);
	}


	private Widget createVideoItem() {
		Tree videoPanel = new Tree();
		TreeItem videoPanelRoot = videoPanel.addItem("14 febrero 2012");
		addItem(videoPanelRoot, "MP4 video");
		addItem(videoPanelRoot, "FLV video");
		videoPanelRoot.setState(true);
		
		videoPanel.addSelectionHandler(new SelectionHandler<TreeItem>(){
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				stopPlayers();
				verticalPanel_6.setVisible(true);
				verticalPanel_3.setVisible(false);
				if (event.getSelectedItem().getText().toString().equals("MP4 video")){
					fp.play("es3/videos/big-buck-bunny.mp4");
				}
				else {
					fp.play("http://192.168.2.107/mediaplayer-5.9/videos/01.Millenium.flv");
				}
			}
		});
		
		return videoPanel;
	}


	private Widget createAudioItem() {
		Tree audioPanel = new Tree();
		TreeItem audioPanelRoot = audioPanel.addItem("14 febrero 2012");
		addItem(audioPanelRoot, "MP3 audio");
		addItem(audioPanelRoot, "OGG audio");
		audioPanelRoot.setState(true);
		
		audioPanel.addSelectionHandler(new SelectionHandler<TreeItem>(){
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				verticalPanel_6.setVisible(false);
				verticalPanel_3.setVisible(true);
				
				// GWT-HTML5-VIDEO
				verticalPanel_3.remove(videoPlayer);
				videoPlayer = new VideoWidget(false, true, "es3/pics/audio.jpg");
				List<VideoSource> sources = new ArrayList<VideoSource>();
				if (event.getSelectedItem().getText().toString().equals("MP3 audio")){
					sources.add(new VideoSource("es3/audios/o-na-som.mp3"));
				}
				else {
					sources.add(new VideoSource("es3/audios/vivaldi.ogg"));
				}
				videoPlayer.setSources(sources);
				videoPlayer.setPixelSize(300, 0);
				verticalPanel_3.add(videoPlayer);
				verticalPanel_3.setCellHorizontalAlignment(videoPlayer, HasHorizontalAlignment.ALIGN_CENTER);
			}
		});
		
		return audioPanel;
	}


	private Widget createImageItem() {
		// Create a popup to show the contact info when a contact is clicked
		HorizontalPanel imagePopupContainer = new HorizontalPanel();
		imagePopupContainer.setSpacing(5);

		Image pic = new Image("es3/pics/bbb480.jpg");
		pic.setAltText("Big Buck Bunny");
		pic.setSize("100px", "50px");
		pic.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				verticalPanel_6.setVisible(false);
				verticalPanel_3.setVisible(false);
// ALERT Procedimiento modificado
//				showPicOriginalSize();
			}
		});
		imagePopupContainer.add(pic);

		final HTML imageInfo = new HTML();
		imagePopupContainer.add(imageInfo);
		final PopupPanel imagePopup = new PopupPanel(true, false);
		imagePopup.setWidget(imagePopupContainer);
		final String imageName = "img001.jpeg";
		final String imageEmail = "14 febrero 2012";

		// Create the list of contacts
		VerticalPanel imagePanel = new VerticalPanel();
		imagePanel.setSpacing(4);
		final Anchor imageLink = new Anchor(imageName);
		imagePanel.add(imageLink);

		// Open the contact info popup when the user clicks a contact
		imageLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				stopPlayers();
				verticalPanel_6.setVisible(false);
				verticalPanel_3.setVisible(false);
				// Set the info about the contact
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendEscaped(imageName);
				sb.appendHtmlConstant("<br><i>");
				sb.appendEscaped(imageEmail);
				sb.appendHtmlConstant("</i>");
				imageInfo.setHTML(sb.toSafeHtml());

				// Show the popup of contact info
				int left = imageLink.getAbsoluteLeft() + 14;
				int top = imageLink.getAbsoluteTop() + 14;
				imagePopup.setPopupPosition(left, top);
				imagePopup.show();
			}
		});

		return new SimplePanel(imagePanel);
	}


	private void addItem(TreeItem root, String label) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendEscaped(label);
		root.addItem(sb.toSafeHtml());
	}

	
	private void stopPlayers(){
		if(videoPlayer.isPlayed())
			videoPlayer.playPause();
	}

	
	private void showPicOriginalSize(String picPath) {
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
