package com.esferalia.es3.demo.client.tree;

import java.util.Iterator;
import java.util.Vector;

import com.esferalia.es3.demo.client.Images;
import com.esferalia.es3.demo.client.dto.FileCell;
import com.esferalia.es3.demo.client.dto.FileType;
import com.esferalia.es3.demo.client.dto.MissionCell;
import com.esferalia.es3.demo.client.event.CategoryEvent;
import com.esferalia.es3.demo.client.event.PlaySelectedEvent;
import com.esferalia.es3.demo.client.event.SelectedMissionEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class MissionTree extends Composite {
	
	// FIXME MissionTree BASEPATH
	private static String BASEPATH = "http://192.168.2.107:80/es3/mission/";
	
	private Tree missionTree;
	private Images images = (Images) GWT.create(Images.class);
	private final HandlerManager eventBus;
	private String imageHTML;
	
	public MissionTree(Vector<MissionCell> missions, final HandlerManager Bus){
		eventBus = Bus;

		fillTree(missions);
		
		missionTree.addSelectionHandler(new SelectionHandler<TreeItem>(){

			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				if (event.getSelectedItem().getUserObject() == null) {
					CategoryEvent categoryEvent = new CategoryEvent();
					eventBus.fireEvent(categoryEvent);
				}
				else if(event.getSelectedItem().getUserObject() instanceof MissionCell){
					SelectedMissionEvent selectedMissionEvent= new SelectedMissionEvent();
					selectedMissionEvent.setNode(event.getSelectedItem());
					eventBus.fireEvent(selectedMissionEvent);
				}
				else {
					PlaySelectedEvent selectedFileEvent = new PlaySelectedEvent();
					String originalName = ((FileCell)event.getSelectedItem().getUserObject()).getOriginalName();
					int id = ((FileCell)event.getSelectedItem().getUserObject()).getId();
					int mission = ((FileCell)event.getSelectedItem().getUserObject()).getMission();
					FileType type = ((FileCell)event.getSelectedItem().getUserObject()).getType();
					int punto = originalName.lastIndexOf(".");
					String extension = originalName.substring(punto);
					String relativePath = getRelativePath(id, mission, type, extension);
					selectedFileEvent.setNode(event.getSelectedItem());
					selectedFileEvent.setPath(relativePath);
					eventBus.fireEvent(selectedFileEvent);
				}
			}
			
			private String getRelativePath(int id, int mission, FileType type, String extension) {
				return BASEPATH + Integer.toString(mission) + "/" + type.toString() + "/" + Integer.toString(id) + extension;
			}
		});
		
		missionTree.setAnimationEnabled(true);
		
		initWidget(missionTree);
	}

	private void fillTree(Vector<MissionCell> missions) {
		missionTree = new Tree();
		Iterator<MissionCell> itM = missions.iterator();
		while(itM.hasNext()){
			MissionCell mission = itM.next();
			imageHTML = AbstractImagePrototype.create(images.mission()).getHTML();
			TreeItem missionItem = new TreeItem(setTreeItemHeader(imageHTML, mission.getName()));
			missionItem.setUserObject(mission);
			if (mission.getAudios().size() > 0){
				imageHTML = AbstractImagePrototype.create(images.audio()).getHTML();
				TreeItem audioFolderItem = new TreeItem(setTreeItemHeader(imageHTML, "audio"));
				audioFolderItem.setUserObject(null);
				Iterator<FileCell> itAudio = mission.getAudios().iterator();
				while(itAudio.hasNext()){
					FileCell audio = itAudio.next();
					TreeItem audioItem = new TreeItem(audio.getShortName());
					audioItem.setUserObject(audio);
					audioFolderItem.addItem(audioItem);
				}
				missionItem.addItem(audioFolderItem);
			}
			if (mission.getVideos().size() > 0){
				imageHTML = AbstractImagePrototype.create(images.video()).getHTML();
				TreeItem videoFolderItem = new TreeItem(setTreeItemHeader(imageHTML, "video"));
				videoFolderItem.setUserObject(null);
				Iterator<FileCell> itVideo = mission.getVideos().iterator();
				while(itVideo.hasNext()){
					FileCell video = itVideo.next();
					TreeItem videoItem = new TreeItem(video.getShortName());
					videoItem.setUserObject(video);
					videoFolderItem.addItem(videoItem);
				}
				missionItem.addItem(videoFolderItem);
			}
			if (mission.getImages().size() > 0){
				imageHTML = AbstractImagePrototype.create(images.image()).getHTML();
				TreeItem imageFolderItem = new TreeItem(setTreeItemHeader(imageHTML, "imagen"));
				imageFolderItem.setUserObject(null);
				Iterator<FileCell> itImage = mission.getImages().iterator();
				while(itImage.hasNext()){
					FileCell image = itImage.next();
					TreeItem imageItem = new TreeItem(image.getShortName());
					imageItem.setUserObject(image);
					imageFolderItem.addItem(imageItem);
				}
				missionItem.addItem(imageFolderItem);
			}
			if (mission.getGps().size() > 0){
				imageHTML = AbstractImagePrototype.create(images.gps()).getHTML();
				TreeItem gpsFolderItem = new TreeItem(setTreeItemHeader(imageHTML, "cartografia"));
				gpsFolderItem.setUserObject(null);
				Iterator<FileCell> itGPS = mission.getGps().iterator();
				while(itGPS.hasNext()){
					FileCell gps = itGPS.next();
					TreeItem gpsItem = new TreeItem(gps.getShortName());
					gpsItem.setUserObject(gps);
					gpsFolderItem.addItem(gpsItem);
				}
				missionItem.addItem(gpsFolderItem);
			}
			if (mission.getDocs().size() > 0){
				imageHTML = AbstractImagePrototype.create(images.doc()).getHTML();
				TreeItem docFolderItem = new TreeItem(setTreeItemHeader(imageHTML, "documento"));
				docFolderItem.setUserObject(null);
				Iterator<FileCell> itDoc = mission.getDocs().iterator();
				while(itDoc.hasNext()){
					FileCell doc = itDoc.next();
					TreeItem docItem = new TreeItem(doc.getShortName());
					docItem.setUserObject(doc);
					docFolderItem.addItem(docItem);
				}
				missionItem.addItem(docFolderItem);
			}
			missionTree.addItem(missionItem);
		}
	}
	
	private SafeHtml setTreeItemHeader(String imagen, String nombre){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendHtmlConstant(imagen).appendEscaped(" ");
        sb.appendEscaped(nombre);
		return sb.toSafeHtml();
	}

}
