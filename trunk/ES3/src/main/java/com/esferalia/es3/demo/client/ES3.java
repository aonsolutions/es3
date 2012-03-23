package com.esferalia.es3.demo.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.esferalia.es3.demo.client.dto.Coordenada;
import com.esferalia.es3.demo.client.dto.MissionCell;
import com.esferalia.es3.demo.client.event.PlaySelectedEvent;
import com.esferalia.es3.demo.client.event.PlaySelectedEventHandler;
import com.esferalia.es3.demo.client.flowplayer.FlowPlayer;
import com.esferalia.es3.demo.client.mdi.MDIContainer;
import com.esferalia.es3.demo.client.mdi.MDIWindow;
import com.esferalia.es3.demo.client.service.DatabaseService;
import com.esferalia.es3.demo.client.service.DatabaseServiceAsync;
import com.esferalia.es3.demo.client.service.XMLService;
import com.esferalia.es3.demo.client.service.XMLServiceAsync;
import com.esferalia.es3.demo.client.tree.MissionTree;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoWidget;

public class ES3 implements EntryPoint {

	static interface ES3Resources extends ClientBundle {
		@NotStrict
		@Source("es3.css")
		CssResource css();

		@Source("button.png")
		ImageResource button();

		@Source("menuBar.png")
		ImageResource menuBar();
	}

	static interface Binder extends UiBinder<DockLayoutPanel, ES3> {

	}

	public static class MDIWindowOpenManager implements MDIContainer.Listener {

		Map<String, MDIWindow> pathMap;
		Map<MDIWindow, String> mdiMap;

		/** Creates a new instance of DoubleMap */
		public MDIWindowOpenManager(ES3 es3) {
			pathMap = new HashMap<String, MDIWindow>();
			mdiMap = new IdentityHashMap<MDIWindow, String>();
			es3.mdiContainer.addListener(this);
		}

		String get(MDIWindow mdiWindow) {
			return mdiMap.get(mdiWindow);
		}

		MDIWindow get(String path) {
			return pathMap.get(path);
		}

		void remove(MDIWindow mdiWindow) {
			// Yes, I know that this is very tricky,
			// but it's quite elegant too, isn't it?.
			pathMap.remove(mdiMap.remove(mdiWindow));
		}

		void remove(String path) {
			// Yes, I know that this is very tricky,
			// but it's quite elegant too, isn't it?.
			mdiMap.remove(pathMap.remove(path));
		}

		void put(String path, MDIWindow mdiWindow) {
			pathMap.put(path, mdiWindow);
			mdiMap.put(mdiWindow, path);
		}

		@Override
		public void onClose(MDIContainer mdiContainer, MDIWindow... mdiWindows) {
			for (MDIWindow mdiWindow : mdiWindows) {
				remove(mdiWindow);
			}
		}

		@Override
		public void onOpen(MDIContainer mdiContainer, MDIWindow... mdiWindows) {
		}

		@Override
		public void onFocus(MDIContainer mdiContainer, MDIWindow... mdiWindows) {
		}

	}

	static abstract class Player {

		static List<Player> PLAYERS = new LinkedList<Player>();

		abstract boolean accept(PlaySelectedEvent event);

		abstract void play(PlaySelectedEvent event, ES3 es3);

		static void register(Player player) {
			PLAYERS.add(player);
		}

		static void playSelectedEvent(PlaySelectedEvent event, ES3 es3) {
			for (Player player : PLAYERS) {
				if (player.accept(event)) {
					player.play(event, es3);

				}
			}
		}

	}

	static abstract class MDIPlayer extends Player {

		@Override
		void play(PlaySelectedEvent event, ES3 es3) {

			MDIWindow mdiWindow = es3.openManager.get(event.getPath());

			if (mdiWindow != null) {
				es3.setFocus(mdiWindow);
				return;
			}

			mdiWindow = createMDIWindow(event, es3);

			Widget widget = createWidget(event, es3);
			mdiWindow.setWidget(widget);

			es3.mdiContainer.addWindow(mdiWindow);

			es3.openManager.put(event.getPath(), mdiWindow);

		}

		MDIWindow createMDIWindow(PlaySelectedEvent event, ES3 es3) {
			MDIWindow mdiWindow = new MDIWindow();
			String caption = event.getNode().getText();
			mdiWindow.setCaption(caption);
			return mdiWindow;
		}

		abstract Widget createWidget(PlaySelectedEvent event, ES3 es3);

	}

	static abstract class ExtPlayer extends MDIPlayer {

		private String exts[];

		public ExtPlayer(String... exts) {
			this.exts = exts;
		}

		@Override
		boolean accept(PlaySelectedEvent event) {
			String ext = getExt(event.getPath());
			if (ext == null) {
				return false;
			}
			for (int i = 0; i < exts.length; i++) {
				if (exts[i].equalsIgnoreCase(ext)) {
					return true;
				}
			}
			return false;
		}

		static String getExt(String path) {
			int dot = path.lastIndexOf('.');
			return dot < 0 ? null : path.substring(dot + 1);
		}
	}

	static class ImagePlayer extends ExtPlayer {

		static class ResizableImage extends Image implements RequiresResize {

			public ResizableImage(String url) {
				super();
				setUrl(url);
			}

			@Override
			public void onResize() {

				int imageWidth = getOffsetWidth();
				int imageHeight = getOffsetHeight();

				Element panelElement = getParent().getElement();
				int availWidth = panelElement.getClientWidth();
				int availHeight = panelElement.getClientHeight();

				int scaleWidth = availHeight * imageWidth / imageHeight;
				int scaleHeight = availWidth * imageHeight / imageWidth;

				int width = Math.min(availWidth, scaleWidth);
				int height = Math.min(availHeight, scaleHeight);

				DOM.setStyleAttribute(getElement(), "width", width + "px");
				DOM.setStyleAttribute(getElement(), "height", height + "px");
				DOM.setStyleAttribute(getElement(), "position", "absolute");
				DOM.setStyleAttribute(getElement(), "left",
						(availWidth - width) / 2 + "px");
				DOM.setStyleAttribute(getElement(), "top",
						(availHeight - height) / 2 + "px");

			}

		}

		private ImagePlayer() {
			super("jpg", "jpeg", "png", "gif", "bmp");
		}

		@Override
		Widget createWidget(PlaySelectedEvent event, ES3 es3) {
			Image image = new ResizableImage(event.getPath());
			return image;
		}
	}

	static class PathPlayer extends ExtPlayer {


		private XMLServiceAsync xmlService = GWT.create(XMLService.class);

		private PathPlayer() {
			super("xml");
		}

		@Override
		void play(final PlaySelectedEvent event, final ES3 es3) {
			MDIWindow mdiWindow = es3.openManager.get(event.getPath());

			if (mdiWindow != null) {
				es3.setFocus(mdiWindow);
				return;
			}
			xmlService.getCoordenadas("1", event.getPath(),
					new AsyncCallback<Vector<Coordenada>>() {
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onSuccess(Vector<Coordenada> coordinates) {
							showCoordinates(coordinates, event, es3);
						}
					});
		}

		private void showCoordinates(final Vector<Coordenada> coordinates,
				final PlaySelectedEvent event, final ES3 es3) {
			Maps.loadMapsApi("", "2", false, new Runnable() {
				@Override
				public void run() {

					// es3.mdiContainer.get

					MapWidget map = new MapWidget();
					map.setSize("700px", "600px");
					map.setScrollWheelZoomEnabled(true);

					LatLng latLngs[] = new LatLng[coordinates.size()];
					for (int i = 0; i < latLngs.length; i++) {
						Coordenada coord = coordinates.get(i);
						latLngs[i] = LatLng.newInstance(coord.getLatitud(),
								coord.getLongitud());

					}
					Polyline poliLyne = new Polyline(latLngs);
					map.setCenter(latLngs[0]);
					map.setZoomLevel(12);
					map.addOverlay(poliLyne);

					MDIWindow mdiWindow = createMDIWindow(event, es3);

					mdiWindow.setWidget(new SimplePanel(map));
					es3.mdiContainer.addWindow(mdiWindow);
					es3.openManager.put(event.getPath(), mdiWindow);
				}
			});
		}

		@Override
		Widget createWidget(PlaySelectedEvent event, ES3 es3) {
			// NOT used
			return null;
		}
	}

	static class FlashVideoPlayer extends ExtPlayer {

		private FlashVideoPlayer() {
			super("flv", "mp4");
		}

		@Override
		Widget createWidget(PlaySelectedEvent event, ES3 es3) {
			FlowPlayer flowPlayer = new FlowPlayer();
			flowPlayer.setSize("360px", "240px");
			flowPlayer.play(event.getPath());
			return flowPlayer;
		}
	}

	static class HTML5AudioPlayer extends ExtPlayer {

		private HTML5AudioPlayer() {
			super("mp3", "ogg");
		}

		@Override
		Widget createWidget(PlaySelectedEvent event, ES3 es3) {
			VideoWidget videoPlayer = new VideoWidget(true, true, "");
			List<VideoSource> sources = new ArrayList<VideoSource>();
			sources.add(new VideoSource(event.getPath()));
			videoPlayer.setSources(sources);
			videoPlayer.setPixelSize(360, 140);
			return videoPlayer;
		}
	}

	static class HTML5VideoPlayer extends ExtPlayer {

		private HTML5VideoPlayer() {
			super("ogv", "webm");
		}

		@Override
		Widget createWidget(PlaySelectedEvent event, ES3 es3) {
			VideoWidget videoPlayer = new VideoWidget(true, true, "");
			List<VideoSource> sources = new ArrayList<VideoSource>();
			sources.add(new VideoSource(event.getPath()));
			videoPlayer.setSources(sources);
			videoPlayer.setPixelSize(360, 240);
			return videoPlayer;
		}
	}

	{
		Player.register(new ImagePlayer());
		Player.register(new PathPlayer());
		Player.register(new HTML5AudioPlayer());
		Player.register(new HTML5VideoPlayer());
		Player.register(new FlashVideoPlayer());
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	Panel westPanel;
	@UiField
	MainMenu mainMenu;
	@UiField
	MDIContainer mdiContainer;

	private HandlerManager handlerManager;
	private DatabaseServiceAsync databaseService;
	private MDIWindowOpenManager openManager;

	@Override
	public void onModuleLoad() {

		// Inject ES3 styles.
		GWT.<ES3Resources> create(ES3Resources.class).css().ensureInjected();

		handlerManager = new HandlerManager(null);

		databaseService = GWT.create(DatabaseService.class);

		// Create the UI defined in ES3.ui.xml.
		DockLayoutPanel outer = binder.createAndBindUi(this);

		// Get rid of scrollbars, and clear out the window's built-in margin,
		// because we want to take advantage of the entire client area.
		Window.enableScrolling(false);
		Window.setMargin("0px");

		// Add the outer panel to the RootLayoutPanel, so that it will be
		// displayed.
		RootLayoutPanel root = RootLayoutPanel.get();
		root.add(outer);

		// Init missions tree.
		loadMissions();

		registerPlaySelectedEventHandler();

		mainMenu.setEs3(this);

		openManager = new MDIWindowOpenManager(this);

	}

	protected void closeAll() {
		mdiContainer.removeAll();
	}

	protected void cascade() {
		mdiContainer.cascade();
	}

	protected void tileVertical() {
		mdiContainer.tileVertical();
	}

	protected void tileHorizontal() {
		mdiContainer.tileHorizontal();
	}

	protected void setFocus(MDIWindow mdiWindow) {
		mdiContainer.setFocus(mdiWindow);
	}

	private void loadMissions() {
		databaseService.fillTree(new AsyncCallback<Vector<MissionCell>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(Vector<MissionCell> missions) {
				MissionTree missionTree = new MissionTree(missions,
						handlerManager);
				westPanel.add(missionTree);
			}

		});
	}

	private void registerPlaySelectedEventHandler() {

		handlerManager.addHandler(PlaySelectedEvent.TYPE,
				new PlaySelectedEventHandler() {

					@Override
					public void onPlaySelected(PlaySelectedEvent event) {
						Player.playSelectedEvent(event, ES3.this);
					}
				});
	}

}
