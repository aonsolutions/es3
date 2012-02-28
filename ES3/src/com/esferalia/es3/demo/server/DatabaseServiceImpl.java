package com.esferalia.es3.demo.server;

import java.sql.SQLException;

import com.esferalia.es3.demo.client.DatabaseException;
import com.esferalia.es3.demo.client.DatabaseService;
import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.dto.Mission;
import com.esferalia.es3.demo.server.database.DatabaseManager;
import com.esferalia.es3.demo.server.directory.DirectoryManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DatabaseServiceImpl extends RemoteServiceServlet implements
		DatabaseService {

	private DatabaseManager dbmanager = new DatabaseManager();
	private DirectoryManager dirmanager = new DirectoryManager();
	
	@Override
	public void loadDatabase() throws DatabaseException{
		
	}
	
	public void insertMission(Mission mision) throws DatabaseException{
		int id;
		try {
			dbmanager.getConnection();
			dbmanager.insertMission(mision.getName(), mision.getAlias(), mision.getDescription(), mision.getStart_date() , mision.getEnd_date());
			id = dbmanager.lastInsertID("mission");
			dbmanager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.toString());
		}
		dirmanager.createMission(id);
	}
	
	public void insertFile(File file) throws DatabaseException{
		try {
			dbmanager.getConnection();
			dbmanager.insertFile(file.getMission(), file.getName(), file.getDescription(), file.getDate_time(), file.getMD5());
			dbmanager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.toString());
		}
	}
}