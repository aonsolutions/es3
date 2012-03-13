package com.esferalia.es3.demo.client.service;

import java.util.Vector;

import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.dto.Mission;
import com.esferalia.es3.demo.client.dto.MissionCell;
import com.esferalia.es3.demo.client.exception.DatabaseException;
import com.esferalia.es3.demo.client.exception.DirectoryException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("DatabaseService")
public interface DatabaseService extends RemoteService {

	public void loadDatabase() throws DatabaseException;

	void insertMission(Mission mission) throws DatabaseException, DirectoryException;

	void insertFile(File file) throws DatabaseException, DirectoryException;

	Mission selectMission(Integer integer) throws DatabaseException;

	void updateMission(Mission mission) throws DatabaseException;

	void deleteMission(int id) throws DatabaseException, DirectoryException;

	void updateFile(File file) throws DatabaseException;

	void deleteFile(File file) throws DatabaseException, DirectoryException;

	File selectFile(Integer valueOf) throws DatabaseException;

	Vector<MissionCell> fillTree() throws DatabaseException;

}
