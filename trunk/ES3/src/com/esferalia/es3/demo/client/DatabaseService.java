package com.esferalia.es3.demo.client;

import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.dto.Mission;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("DatabaseService")
public interface DatabaseService extends RemoteService {

	public void loadDatabase() throws DatabaseException;

	void insertMission(Mission mission) throws DatabaseException;

	void insertFile(File file) throws DatabaseException;

}
