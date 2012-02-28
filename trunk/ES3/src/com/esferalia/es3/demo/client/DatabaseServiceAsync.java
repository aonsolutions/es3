package com.esferalia.es3.demo.client;

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
}
