package com.esferalia.es3.demo.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.esferalia.es3.demo.client.ES3.PathPlayer.ResizableMapWidget;
import com.esferalia.es3.demo.client.dto.Coordenada;
import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.dto.FileCell;
import com.esferalia.es3.demo.client.dto.FileType;
import com.esferalia.es3.demo.client.dto.MissionCell;
import com.esferalia.es3.demo.client.event.PlaySelectedEvent;
import com.esferalia.es3.demo.client.event.PlaySelectedEventHandler;
import com.esferalia.es3.demo.client.flowplayer.Clip;
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
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.DragEvent;
import com.google.gwt.event.dom.client.DragHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
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
import com.google.gwt.maps.client.overlay.HasPolylineOptions;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerImage;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.maps.client.overlay.PolylineOptions;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
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

		Collection<MDIWindow> openMDIWindows() {
			return pathMap.values();
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
								Scheduler.get().scheduleFixedPeriod(
										new RepeatingCommand() {

											@Override
											public boolean execute() {
												PlayerMap.this.player
														.getPlayer().seek(
																dragSecs);
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
			String caption = event.getNode() != null ? event.getNode()
					.getText() : event.getShortName();
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
			if (dot < 0) {
				return null;
			}
			int query = path.lastIndexOf('?');

			return query < 0 ? path.substring(dot + 1) : path.substring(
					dot + 1, query);
		}
	}

	static class ImagePlayer extends ExtPlayer {

		static class ResizableImage extends Image implements RequiresResize,
				LoadHandler, DoubleClickHandler, MouseMoveHandler,
				MouseUpHandler, MouseDownHandler {

			public ResizableImage(String url) {
				super();
				setUrl(url);
				addLoadHandler(this);
				addMouseUpHandler(this);
				addMouseDownHandler(this);
				addMouseMoveHandler(this);
				addDoubleClickHandler(this);
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
				

				DOM.setStyleAttribute(getElement(), "clips", null);
				DOM.setStyleAttribute(getElement(), "width", width + "px");
				DOM.setStyleAttribute(getElement(), "height", height + "px");

				int left = Math.max((availWidth - width) / 2, 0);
				int top = Math.max((availHeight - height) / 2, 0);
				DOM.setStyleAttribute(getElement(), "position", "absolute");
				DOM.setStyleAttribute(getElement(), "left", left + "px");
				DOM.setStyleAttribute(getElement(), "top", top + "px");
			}

			@Override
			public void onLoad(LoadEvent event) {
				MDIWindow mdiWindow = null;
				for (Widget parent = getParent(); parent != null; parent = parent
						.getParent()) {
					if (parent instanceof RootPanel) {
						return;
					}
					if (parent instanceof MDIWindow) {
						mdiWindow = (MDIWindow) parent;
					}
					if (parent instanceof MDIContainer) {
						
						
						MDIContainer mdiContainer = (MDIContainer) parent;
						int availWidth = mdiContainer.getClientWidth();
						int availHeight = mdiContainer.getClientHeight();
						if ( getHeight() > availHeight || getWidth() > availWidth ){
							mdiContainer.onMaximize(mdiWindow);
						}
						else {
							mdiContainer.center(mdiWindow);
						}
						return;
					}
				}
			}

			private static RegExp CLIP_REGEXP = RegExp
					.compile("([0-9]+)px\\s*,\\s*([0-9]+)px\\s*,\\s*([0-9]+)px\\s*,\\s*([0-9]+)px");

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				try {
					float ZOOM = 1.5f;
					
					Widget parent = getParent();

					int clipTop = 0;
					int clipLeft = 0;
					int clipRigth = parent.getElement().getClientWidth();
					int clipBottom = parent.getElement().getClientHeight();
					String clip = DOM.getStyleAttribute(getElement(), "clip");
					MatchResult matchResult = clip != null ? CLIP_REGEXP
							.exec(clip) : null;
					if (matchResult != null) {
						clipTop = Integer.valueOf(matchResult.getGroup(1));
						clipRigth = Integer.valueOf(matchResult.getGroup(2)); 
						clipBottom = Integer.valueOf(matchResult.getGroup(3)); 
						clipLeft = Integer.valueOf(matchResult.getGroup(4));
					}
					int clipWidth = clipRigth - clipLeft;
					int clipHeight = clipBottom - clipTop;

					int x = event.getRelativeX(getElement());
					int y = event.getRelativeY(getElement());
					int offsetX = x - clipWidth / 2 ;
					int offsetY = y - clipHeight / 2 ;
					

					int imageWidth = getOffsetWidth();
					int imageHeight = getOffsetHeight();
					imageWidth  = Math.round(imageWidth * ZOOM);
					imageHeight = Math.round(imageHeight * ZOOM);
					
					clipLeft += x * ZOOM - clipWidth / 2;//Math.round(offsetX * ZOOM);
					clipTop += y * ZOOM - clipHeight / 2 ;//Math.round(offsetY * ZOOM);
					
					if ( clipTop < 0 ) 
						clipTop = 0;
					if ( clipLeft < 0 ) 
						clipLeft = 0;
					if ( clipTop + clipHeight > imageHeight ) 
						clipTop = imageHeight - clipHeight;
					if ( clipLeft + clipWidth > imageWidth ) 
						clipLeft = imageWidth - clipWidth;
					
					clipRigth = clipLeft + clipWidth;
					clipBottom = clipTop + clipHeight;
					
					parent.setPixelSize(parent.getOffsetWidth(), parent.getOffsetHeight());
					
					DOM.setStyleAttribute(getElement(), "position", "absolute");
					DOM.setStyleAttribute(getElement(), "left", -clipLeft + "px");
					DOM.setStyleAttribute(getElement(), "top", -clipTop + "px");
					DOM.setStyleAttribute(getElement(), "clip", "rect( " 
							+ clipTop + "px,"
							+ clipRigth + "px," 
							+ clipBottom + "px,"
							+ clipLeft + "px)");
					DOM.setStyleAttribute(getElement(), "width", imageWidth + "px");
					DOM.setStyleAttribute(getElement(), "height", imageHeight + "px");

				} catch (Exception e) {
					Window.alert(e.getLocalizedMessage());
				}
			}
			
			protected int dragStartX;
			protected int dragStartY;
			protected boolean dragging = false;


			public void onMouseDown(MouseDownEvent event) {
				dragging = true;
				Widget widget = (Widget) event.getSource();
				DOM.setCapture(widget.getElement());
				dragStartX = event.getClientX();
				dragStartY = event.getClientY();
				DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
			}

			public void onMouseMove(MouseMoveEvent event) {
				if (dragging) {
					handleDrag(event.getClientX() - dragStartX, event.getClientY()
							- dragStartY);
					dragStartX = event.getClientX();
					dragStartY = event.getClientY();
				}
			}

			public void onMouseUp(MouseUpEvent event) {
				dragging = false;
				Widget widget = (Widget) event.getSource();
				DOM.releaseCapture(widget.getElement());
			}
			
			protected void handleDrag(int absX, int absY) {
				int clipTop = 0;
				int clipLeft = 0;
				int clipRigth = getOffsetWidth();
				int clipBottom = getOffsetHeight();
				String clip = DOM.getStyleAttribute(getElement(), "clip");
				MatchResult matchResult = clip != null ? CLIP_REGEXP
						.exec(clip) : null;
				if (matchResult != null) {
					clipTop = Integer.valueOf(matchResult.getGroup(1));
					clipRigth = Integer.valueOf(matchResult.getGroup(2)); 
					clipBottom = Integer.valueOf(matchResult.getGroup(3)); 
					clipLeft = Integer.valueOf(matchResult.getGroup(4));
				}
				int clipWidth = clipRigth - clipLeft;
				int clipHeight = clipBottom - clipTop;

				clipTop -= absY;
				clipLeft -= absX;
				if ( clipTop < 0 ) 
					clipTop = 0;
				if ( clipLeft < 0 ) 
					clipLeft = 0;
				int imageWidth = getOffsetWidth();
				int imageHeight = getOffsetHeight();
				if ( clipTop + clipHeight > imageHeight ) 
					clipTop = imageHeight - clipHeight;
				if ( clipLeft + clipWidth > imageWidth ) 
					clipLeft = imageWidth - clipWidth;
				clipRigth = clipLeft + clipWidth;
				clipBottom = clipTop + clipHeight;
				
				
				Widget parent = getParent();
				parent.setPixelSize(parent.getOffsetWidth(), parent.getOffsetHeight());
				DOM.setStyleAttribute(getElement(), "position", "absolute");
				DOM.setStyleAttribute(getElement(), "left", -clipLeft + "px");
				DOM.setStyleAttribute(getElement(), "top", -clipTop + "px");
				DOM.setStyleAttribute(getElement(), "clip", "rect( " 
						+ clipTop + "px,"
						+ clipRigth + "px," 
						+ clipBottom + "px,"
						+ clipLeft + "px)");
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
			/*
			 * for (int i = 0; i < path.size() ; i++) {
			 * 
			 * HasLatLng latLng = path.get(i / 10); FileCell image = getImage(i
			 * * 10, latLng, event, es3); if (image == null) { continue; }
			 * Marker marker = new Marker(); marker.setZIndex(0);
			 * marker.setVisible(true); marker.setPosition(latLng);
			 * marker.setClickable(true); marker.setMap(map.getMap());
			 * marker.setTitle(image.getShortName()); String url =
			 * getPath(image); MarkerImage.Builder builder = new
			 * MarkerImage.Builder(url); builder.setScaledSize(new Size(32,
			 * 32)); marker.setIcon(builder.build()); Event.addListener(marker,
			 * "click", new ImageClickHadler(es3, getNode(image, mission))); }
			 */

			MissionCell missionCell = (MissionCell) mission.getUserObject();
			FileCell gpsFile = (FileCell) event.getNode().getUserObject();

			RegExp regExp = RegExp.compile(gpsFile.getShortName()
					+ " ([0-9]{4})");

			Vector<FileCell> images = missionCell.getImages();
			for (FileCell image : images) {
				MatchResult matchResult = regExp.exec(image.getShortName());
				if (matchResult != null) {
					int secs = Integer.parseInt(matchResult.getGroup(1));

					HasLatLng latLng = map.getLatLng(secs * 1000);

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
					Event.addListener(marker, "click", new ImageClickHadler(
							es3, getNode(image, mission)));

					// Window.alert(image.getShortName() + " " + secs);
				}
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

		static FileCell getImage(int time, HasLatLng latLng,
				PlaySelectedEvent event, ES3 es3) {

			TreeItem node = event.getNode();

			TreeItem mission = getMission(node);
			MissionCell missionCell = (MissionCell) mission.getUserObject();

			FileCell gpsFile = (FileCell) node.getUserObject();

			String secs = "000" + time;

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

			void addImageMarker(FileCell image, long time, ES3 es3,
					TreeItem mission) {
				HasLatLng latLng = getLatLng(time);

				Marker marker = new Marker();
				marker.setZIndex(0);
				marker.setVisible(true);
				marker.setPosition(latLng);
				marker.setClickable(true);
				marker.setMap(getMap());
				marker.setTitle(image.getShortName());
				String url = getPath(image);
				MarkerImage.Builder builder = new MarkerImage.Builder(url);
				builder.setScaledSize(new Size(32, 32));
				marker.setIcon(builder.build());
				Event.addListener(marker, "click", new ImageClickHadler(es3,
						getNode(image, mission)));
			}

			/**
			 * Fetches angle relative to screen centre point where 3 O'Clock is
			 * 0 and 12 O'Clock is 270 degrees
			 */
			public double getAngle(double time) {
				double latLngTime = time / 10000;

				int index = (int) Math.floor(latLngTime);
				HasLatLng start = path.get(index);
				HasLatLng end = path.get(index + 1);

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

	static interface VideoPlayer {
		double getTime();

		String getVideoId();

		String getVideoUrl();

	}

	static class FlashVideoPlayer extends ExtPlayer {

		private static class ResizableFlowPlayer extends FlowPlayer implements
				RequiresResize, VideoPlayer {

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

			@Override
			public double getTime() {
				return getPlayer().getTime();
			}

			@Override
			public String getVideoUrl() {
				Clip clip = getPlayer().getClip();
				return clip != null ? clip.getCompleteUrl() : null;
			}

			@Override
			public String getVideoId() {
				String url = getVideoUrl();
				String file = url.substring(url.lastIndexOf('/') + 1);
				int lastDot = file.lastIndexOf('.');
				String id = file.substring(0,
						lastDot != -1 ? lastDot : file.length());
				return id;
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

	private MissionTree missionTree;
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

	protected void save() {
		try {
			final MDIWindow mdiWindow = mdiContainer.getFocus();

			Widget widget = mdiWindow.getWidget();
			if (widget instanceof Image) {
				Image image = (Image) widget;

				File file = new File();
				file.setMD5("");
				file.setDescription("");
				file.setMission(getMission());
				file.setDate_time(new Date());
				file.setFileType(FileType.imagen);
				file.setName(mdiWindow.getCaption() + ".png");

				databaseService.insertFile(file, new AsyncCallback<File>() {

					@Override
					public void onSuccess(File file) {
						FileCell imageCell = new FileCell();
						imageCell.setId(file.getId());
						imageCell.setType(file.getFileType());
						imageCell.setOriginalName(file.getName());
						imageCell.setShortName(mdiWindow.getCaption());
						imageCell.setMission(file.getMission());
						TreeItem imageItem = new TreeItem(imageCell
								.getShortName());
						imageItem.setUserObject(imageCell);

						addImage(imageCell);

						syncMap(imageCell);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
				});
			}

		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
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

	protected void captureScreen() {
		try {

			for (MDIWindow mdiWindow : openManager.openMDIWindows()) {

				if (mdiWindow.getWidget() instanceof VideoPlayer) {
					VideoPlayer videoPlayer = (VideoPlayer) mdiWindow
							.getWidget();
					String videoId = videoPlayer.getVideoId();
					double time = videoPlayer.getTime();

					PlaySelectedEvent playEvent = new PlaySelectedEvent();

					String path = GWT.getModuleBaseURL() + "capture/" + videoId
							+ ".png?time=" + (time * 1000);
					playEvent.setPath(path);

					String secs = "000" + (int) Math.floor(time);
					String shortName = mdiWindow.getCaption() + " "
							+ secs.substring(secs.length() - 4);
					playEvent.setShortName(shortName);
					playEvent.setOriginalName(shortName);

					play(playEvent);

					return;
				}
			}
		} catch (Exception e) {
			Window.alert(e.getMessage());
		}
	}

	private int getMission() {
		return 49;
	}

	private void syncMap(FileCell imageCell) {
		Collection<MDIWindow> openMDIWindows = openManager.openMDIWindows();
		for (MDIWindow mdiWindow : openMDIWindows) {
			if (mdiWindow.getWidget() instanceof ResizableMapWidget) {
				ResizableMapWidget mapWidget = (ResizableMapWidget) mdiWindow
						.getWidget();
				Tree tree = missionTree.getTree();
				TreeItem missionItem = tree.getItem(0);
				String name = imageCell.getShortName();
				long time = Long.valueOf(name.substring(name.length() - 4));
				mapWidget.addImageMarker(imageCell, time * 1000, this,
						missionItem);
				return;
			}
		}
	}

	private void addImage(FileCell fileCell) {
		Tree tree = missionTree.getTree();
		TreeItem missionItem = tree.getItem(0);

		MissionCell missionCell = (MissionCell) missionItem.getUserObject();
		missionCell.addImage(fileCell);

		TreeItem imagesItem = null;
		for (int i = 0; i < missionItem.getChildCount(); i++) {
			TreeItem treeItem = missionItem.getChild(i);
			Object userObject = treeItem.getUserObject();
			if (userObject != null && userObject.equals("imagen")) {
				imagesItem = treeItem;
				break;
			}
		}
		add(imagesItem, fileCell);
	}

	private void add(TreeItem parent, FileCell newCell) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			TreeItem imageItem = parent.getChild(i);
			FileCell cell = (FileCell) imageItem.getUserObject();
			if (cell.getShortName().compareTo(newCell.getShortName()) > 0) {
				TreeItem newItem = new TreeItem(newCell.getShortName());
				newItem.setUserObject(newCell);
				parent.insertItem(i, newItem);
				return;
			}
		}
		TreeItem item = new TreeItem(newCell.getShortName());
		item.setUserObject(newCell);
		parent.addItem(item);
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
				if (missionTree != null) {
					westPanel.remove(missionTree);
				}
				missionTree = new MissionTree(missions, handlerManager);
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
