package com.esferalia.es3.demo.client.service;

import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.dto.Mission;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DatabaseService</code>.
 */
public interface DatabaseServiceAsync {

	void loadDatabase(AsyncCallback<Void> callback);

	void insertMission(Mission mission, AsyncCallback<Void> callback);

	void insertFile(File file, AsyncCallback<Void> callback);

	void selectMission(Integer integer, AsyncCallback<Mission> callback);

	void updateMission(Mission mission, AsyncCallback<Void> callback);

	void deleteMission(int id, AsyncCallback<Void> callback);

	void updateFile(File file, AsyncCallback<Void> callback);

	void deleteFile(File file, AsyncCallback<Void> callback);

	void selectFile(Integer valueOf, AsyncCallback<File> callback);
}
