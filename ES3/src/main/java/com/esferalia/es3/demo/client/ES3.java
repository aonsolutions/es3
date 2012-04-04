package com.esferalia.es3.demo.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.esferalia.es3.demo.client.ES3.PathPlayer.ResizableMapWidget;
import com.esferalia.es3.demo.client.dto.Coordenada;
import com.esferalia.es3.demo.client.dto.FileCell;
import com.esferalia.es3.demo.client.dto.FileType;
import com.esferalia.es3.demo.client.dto.MissionCell;
import com.esferalia.es3.demo.client.event.PlaySelectedEvent;
import com.esferalia.es3.demo.client.event.PlaySelectedEventHandler;
import com.esferalia.es3.demo.client.flowplayer.FlowPlayer;
import com.esferalia.es3.demo.client.flowplayer.State;
import com.esferalia.es3.demo.client.mdi.MDIContainer;
import com.esferalia.es3.demo.client.mdi.MDIWindow;
import com.esferalia.es3.demo.client.service.DatabaseService;
import com.esferalia.es3.demo.client.service.DatabaseServiceAsync;
import com.esferalia.es3.demo.client.service.XMLService;
import com.esferalia.es3.demo.client.service.XMLServiceAsync;
import com.esferalia.es3.demo.client.tree.MissionTree;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.HasMap;
import com.google.gwt.maps.client.HasMapOptions;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.Size;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.HasMouseEvent;
import com.google.gwt.maps.client.event.MouseEventCallback;
import com.google.gwt.maps.client.overlay.HasMarkerOptions;
import com.google.gwt.maps.client.overlay.HasPolylineOptions;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerImage;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.maps.client.overlay.PolylineOptions;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ClientBundle.Source;
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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TreeItem;
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

	static class MDIContainerListener implements MDIContainer.Listener {

		@Override
		public void onOpen(MDIContainer mdiContainer, MDIWindow... mdiWindows) {
		}

		@Override
		public void onClose(MDIContainer mdiContainer, MDIWindow... mdiWindows) {
		}

		@Override
		public void onFocus(MDIContainer mdiContainer, MDIWindow... mdiWindows) {
		}

	}

	static class MDIWindowOpenManager extends MDIContainerListener {

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

	}

	static class VideoMapSynchronizer implements RepeatingCommand {

		private static String ICON_URL = GWT.getModuleBaseURL() + "image/"
				+ "com/esferalia/es3/demo/client/plane_24.png";
		private static String SHADOW_URL = GWT.getModuleBaseURL() + "image/"
				+ "com/esferalia/es3/demo/client/plane_shadow_24.png";

		private static class PlayerMap {
			MapWidget map;
			FlowPlayer player;
			double time;
			Marker marker;
			
			boolean isDragging;
			int dragSecs;

			public PlayerMap() {
				time = -1;
				map = null;
				player = null;
				marker = null;
				isDragging = false;
			}

			private Marker newMarker() {
				Marker marker = new Marker();
				marker.setZIndex(1);
				marker.setDraggable(true);
				MarkerImage.Builder builder = new MarkerImage.Builder(ICON_URL);
				marker.setIcon(builder.build());
				Event.addListener(marker, "drag", new MouseEventCallback() {
					@Override
					public void callback(HasMouseEvent event) {
						HasLatLng latLng = event.getLatLng();
						int secs = ((ResizableMapWidget) PlayerMap.this.map)
								.getTime(latLng);
						if (secs != -1) {
							State state = PlayerMap.this.player.getPlayer()
									.getState();
							if (state == State.UNSTARTED) {
								PlayerMap.this.player.getPlayer().play();
								PlayerMap.this.player.getPlayer().pause();
							}
							PlayerMap.this.dragSecs = secs;
							syncMarker(dragSecs);
						}
					}
				});

				Event.addListener(marker, "dragstart",
						new MouseEventCallback() {
							@Override
							public void callback(HasMouseEvent event) {
								PlayerMap.this.isDragging = true;
								Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
									
									@Override
									public boolean execute() {
										PlayerMap.this.player.getPlayer().seek(dragSecs);
										return isDragging;
									}
								}, DELAY);
							}
						});

				Event.addListener(marker, "dragend", new MouseEventCallback() {
					@Override
					public void callback(HasMouseEvent event) {
						PlayerMap.this.isDragging = false;
					}
				});

				marker.setMap(this.map.getMap());

				return marker;
			}

			public void setMap(MapWidget map) {
				this.map = map;
				if (map != null) {
					this.marker = newMarker();
					this.marker.setVisible(this.player != null);
				} else {
					this.time = -1;
					this.marker = null;
				}

			}

			public void setPlayer(FlowPlayer player) {
				this.player = player;
				if (this.marker != null) {
					this.marker.setVisible(this.player != null);
				}
			}

			private void syncMarker(int secs) {
				double currentTime = secs * 1000;
				if (this.time == currentTime) {
					return;
				}
				this.time = currentTime;

				HasLatLng latLng = ((ResizableMapWidget) this.map)
						.getLatLng(this.time);
				this.marker.setPosition(latLng);

				double radians = ((ResizableMapWidget) this.map)
						.getAngle(this.time);
				MarkerImage.Builder builder = new MarkerImage.Builder(ICON_URL
						+ "?radians=" + radians);
				this.marker.setIcon(builder.build());
			}

		}

		private static int DELAY = 1000;

		private Map<String, PlayerMap> playerMaps;

		public VideoMapSynchronizer() {
			playerMaps = new HashMap<String, PlayerMap>();
			Scheduler.get().scheduleFixedPeriod(this, DELAY);
		}

		public void registerMap(String name, MapWidget map) {
			PlayerMap playerMap = getPlayerMap(name);
			playerMap.setMap(map);
		}

		public void registerPlayer(String name, FlowPlayer player) {
			PlayerMap playerMap = getPlayerMap(name);
			playerMap.setPlayer(player);
		}

		private PlayerMap getPlayerMap(String name) {
			PlayerMap playerMap = playerMaps.get(name);
			if (playerMap == null) {
				playerMap = new PlayerMap();
				playerMaps.put(name, playerMap);
			}
			return playerMap;
		}

		@Override
		public boolean execute() {
			try {
				for (PlayerMap playerMap : playerMaps.values()) {
					if (playerMap.isDragging) {
						continue;
					}
					if (playerMap.player == null || playerMap.map == null) {
						continue;
					}
					if (!isVisible(playerMap.map)) {
						playerMap.setMap(null);
						continue;
					}
					if (!isVisible(playerMap.player)) {
						playerMap.setPlayer(null);
						continue;
					}
					int secs = (int) playerMap.player.getPlayer().getTime();
					playerMap.syncMarker(secs);

				}
			} catch (Throwable t) {
				Window.alert(t.getMessage());
			}
			return true;
		}

		static boolean isVisible(Widget w) {
			while (w != null) {
				if (!w.isVisible()) {
					return false;
				}
				if (!w.isAttached()) {
					return false;
				}
				w = w.getParent();

			}
			return true;
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

				int left = Math.max((availWidth - width) / 2, 0);
				int top = Math.max((availHeight - height) / 2, 0);
				DOM.setStyleAttribute(getElement(), "position", "absolute");
				DOM.setStyleAttribute(getElement(), "left", left + "px");
				DOM.setStyleAttribute(getElement(), "top", top + "px");
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
							try {
								showCoordinates(coordinates, event, es3);
							} catch (Throwable e) {
								Window.alert(e.getMessage());
							}
						}
					});
		}

		private void showCoordinates(Vector<Coordenada> coordinates,
				final PlaySelectedEvent event, ES3 es3) {

			List<HasLatLng> path = new ArrayList<HasLatLng>(coordinates.size());

			for (Coordenada coord : coordinates) {
				LatLng latLng = new LatLng(coord.getLatitud(),
						coord.getLongitud());
				path.add(latLng);
			}

			MapOptions options = new MapOptions();
			// Zoom level. Required
			options.setZoom(18);
			// Map type. Required.
			options.setMapTypeId(new MapTypeId().getRoadmap());
			// Enable maps drag feature. Disabled by default.

			options.setDraggable(true);
			// Enable and add default navigation control. Disabled by
			// default.
			options.setNavigationControl(true);

			// Enable and add map type control. Disabled by default.
			options.setMapTypeControl(true);

			// Open a map centered on first coordinate. Required
			options.setCenter(path.get(0));

			ResizableMapWidget map = new ResizableMapWidget(options);

			HasPolylineOptions polylineOptions = new PolylineOptions();
			polylineOptions.setPath(path);
			polylineOptions.setStrokeWeight(7);
			polylineOptions.setStrokeOpacity(1.0);
			polylineOptions.setStrokeColor("#8ABB30");

			Polyline polyLine = new Polyline(polylineOptions);
			polyLine.setPath(path);
			polyLine.setMap(map.getMap());
			map.setPath(path);

			TreeItem mission = getMission(event.getNode());
			for (int i = 0; i < path.size(); i++) {
				HasLatLng latLng = path.get(i);
				FileCell image = getImage(i, latLng, event, es3);
				if (image == null) {
					continue;
				}
				Marker marker = new Marker();
				marker.setZIndex(0);
				marker.setVisible(true);
				marker.setPosition(latLng);
				marker.setClickable(true);
				marker.setMap(map.getMap());
				marker.setTitle(image.getShortName());
				String url = getPath(image);
				MarkerImage.Builder builder = new MarkerImage.Builder(url);
				builder.setScaledSize(new Size(32, 32));
				marker.setIcon(builder.build());
				Event.addListener(marker, "click", new ImageClickHadler(es3,
						getNode(image, mission)));
			}
			MDIWindow mdiWindow = createMDIWindow(event, es3);

			mdiWindow.setWidget(map);

			int availWidth = es3.mdiContainer.getClientWidth();
			int availHeight = es3.mdiContainer.getClientHeight();

			map.setPixelSize(Math.min(availWidth, 700),
					Math.min(availHeight, 600));
			es3.mdiContainer.addWindow(mdiWindow);

			es3.openManager.put(event.getPath(), mdiWindow);

			// TODO : I hate this but for now I'm trying to fix the demo.
			String name = event.getNode().getText();
			es3.videoMapSynchronizer.registerMap(name, map);
		}

		@Override
		Widget createWidget(PlaySelectedEvent event, ES3 es3) {
			// NOT used
			return null;
		}

		static FileCell getImage(int index, HasLatLng latLng,
				PlaySelectedEvent event, ES3 es3) {

			TreeItem node = event.getNode();

			TreeItem mission = getMission(node);
			MissionCell missionCell = (MissionCell) mission.getUserObject();

			FileCell gpsFile = (FileCell) node.getUserObject();

			String secs = "000" + index * 10;

			String imageName = gpsFile.getShortName() + " "
					+ secs.substring(secs.length() - 4);

			Vector<FileCell> images = missionCell.getImages();
			for (FileCell image : images) {
				if (image.getShortName().equals(imageName)) {
					return image;
				}
			}

			return null;
		}

		static TreeItem getNode(FileCell fileCell, TreeItem node) {
			if (fileCell == node.getUserObject()) {
				return node;
			}

			for (int i = 0; i < node.getChildCount(); i++) {
				TreeItem found = getNode(fileCell, node.getChild(i));
				if (found != null) {
					return found;
				}
			}
			return null;
		}

		static TreeItem getMission(TreeItem node) {
			while (node != null) {
				Object userObject = node.getUserObject();
				if (userObject instanceof MissionCell) {
					return node;
				}
				node = node.getParentItem();
			}
			return null;
		}

		static String getPath(FileCell file) {
			String originalName = file.getOriginalName();
			int id = file.getId();
			int mission = file.getMission();
			FileType type = file.getType();
			int dot = originalName.lastIndexOf(".");
			String extension = originalName.substring(dot);
			return GWT.getHostPageBaseURL() + "/mission/"
					+ Integer.toString(mission) + "/" + type + "/" + id
					+ extension;
		}

		static class ResizableMapWidget extends MapWidget implements
				RequiresResize {

			private List<HasLatLng> path;

			public ResizableMapWidget(HasMapOptions options) {
				super(options);
			}

			@Override
			public void onResize() {
				HasMap map = getMap();

				HasLatLng center = map.getCenter();

				Element parentEl = getParent().getElement();
				int availWidth = parentEl.getClientWidth();
				int availHeight = parentEl.getClientHeight();

				setPixelSize(availWidth, availHeight);

				Event.trigger(map, "resize");
				map.setCenter(center);
			}

			public void setPath(List<HasLatLng> path) {
				this.path = path;
			}

			/**
			 * Fetches angle relative to screen centre point where 3 O'Clock is
			 * 0 and 12 O'Clock is 270 degrees
			 */
			public double getAngle(double time) {
				double latLngTime = time / 10000;
				
				int index = (int) Math.floor(latLngTime);
				HasLatLng start = path.get(index);
				HasLatLng end = path.get(index +1 );

				double dlng = end.getLongitude() - start.getLongitude();
				double dlat = -(end.getLatitude() - start.getLatitude());

				double inRads = Math.atan2(dlng, dlat);

				// We need to map to coord system when 0 degree is at 3 O'clock,
				// 270 at 12 O'clock
				if (inRads < 0)
					inRads = Math.abs(inRads);
				else
					inRads = 2 * Math.PI - inRads;
				return inRads;
			}

			public HasLatLng getLatLng(double time) {
				double latLngTime = time / 10000;

				HasLatLng start = path.get((int) Math.floor(latLngTime));
				HasLatLng end = path.get((int) Math.ceil(latLngTime));

				double offset = latLngTime - (int) Math.floor(latLngTime);

				double offsetLat = (end.getLatitude() - start.getLatitude())
						* offset;
				double offsetLng = (end.getLongitude() - start.getLongitude())
						* offset;

				return new LatLng(start.getLatitude() + offsetLat,
						start.getLongitude() + offsetLng);
			}

			public int getClossestSegment(HasLatLng c) {
				int segment = -1;
				double minDistance = Double.MAX_VALUE;
				for (int i = 1; i < path.size(); i++) {
					HasLatLng start = path.get(i - 1);
					HasLatLng end = path.get(i);
					double distance = getDistance(c, start, end);
					if (minDistance > distance) {
						segment = i;
						minDistance = distance;
					}
				}
				return segment;
			}

			public int getTime(HasLatLng latLng) {
				int segment = getClossestSegment(latLng);
				HasLatLng start = path.get(segment - 1);
				HasLatLng end = path.get(segment);

				double offset = (latLng.getLatitude() - start.getLatitude())
						/ (end.getLatitude() - start.getLatitude());
				return (segment - 1) * 10 + ((int) (offset * 10));
			}

			static double getDistance(HasLatLng latLng, HasLatLng latLng1,
					HasLatLng latLng2) {
				return getDistance(latLng.getLatitude(), latLng.getLongitude(),
						latLng1.getLatitude(), latLng1.getLongitude(),
						latLng2.getLatitude(), latLng2.getLongitude());
			}

			static double getDistance(double lat, double lng, double lat1,
					double lng1, double lat2, double lng2) {
				double a = lat - lat1;
				double b = lng - lng1;
				double c = lat2 - lat1;
				double d = lng2 - lng1;

				return Math.abs(a * d - c * b) / Math.sqrt(c * c + d * d);
			}

		}

		static class ImageClickHadler extends MouseEventCallback {

			private ES3 es3;
			private TreeItem imageNode;

			public ImageClickHadler(ES3 es3, TreeItem imageNode) {
				this.es3 = es3;
				this.imageNode = imageNode;
			}

			@Override
			public void callback(HasMouseEvent event) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						PlaySelectedEvent playEvent = new PlaySelectedEvent();
						FileCell image = (FileCell) imageNode.getUserObject();
						playEvent.setPath(getPath(image));
						playEvent.setNode(imageNode);
						es3.play(playEvent);
					}
				});

			}
		}
	}

	static class FlashVideoPlayer extends ExtPlayer {

		private static class ResizableFlowPlayer extends FlowPlayer implements
				RequiresResize {

			public ResizableFlowPlayer() {
				super();
			}

			/*
			 * public ResizableFlowPlayer(String video) { super(video); }
			 */

			@Override
			public void onResize() {
				Element parentEl = getParent().getElement();
				int availWidth = parentEl.getClientWidth();
				int availHeight = parentEl.getClientHeight();
				setPixelSize(availWidth, availHeight);
			}
		}

		private FlashVideoPlayer() {
			super("flv", "mp4");
		}

		@Override
		Widget createWidget(PlaySelectedEvent event, ES3 es3) {

			FlowPlayer flowPlayer = new ResizableFlowPlayer();
			int availWidth = es3.mdiContainer.getClientWidth();
			flowPlayer.setPixelSize(Math.min(availWidth, 640),
					Math.min(availWidth * 390 / 640, 390));
			flowPlayer.play(event.getPath());

			// TODO : I hate this but for now I'm trying to fix the demo.
			String name = event.getNode().getText();
			es3.videoMapSynchronizer.registerPlayer(name, flowPlayer);

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
	private VideoMapSynchronizer videoMapSynchronizer;

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
		videoMapSynchronizer = new VideoMapSynchronizer();
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

	private void play(PlaySelectedEvent event) {
		Player.playSelectedEvent(event, this);
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
