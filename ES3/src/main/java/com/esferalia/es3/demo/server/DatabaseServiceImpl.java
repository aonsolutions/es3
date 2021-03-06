package com.esferalia.es3.demo.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.dto.Mission;
import com.esferalia.es3.demo.client.dto.MissionCell;
import com.esferalia.es3.demo.client.exception.DatabaseException;
import com.esferalia.es3.demo.client.exception.DirectoryException;
import com.esferalia.es3.demo.client.service.DatabaseService;
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
	
	@Override
	public Mission selectMission(Integer ident) throws DatabaseException{
		Mission mision = new Mission();
		try {
			dbmanager.getConnection();
			mision = dbmanager.selectMission(ident);
			dbmanager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.toString());
		}
		return mision;
	}
	
	@Override
	public void insertMission(Mission mision) throws DatabaseException, DirectoryException{
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
				
		try{
			dirmanager.createMission(id);
		}catch(IOException e){
			e.printStackTrace();
			throw new DirectoryException(e.toString());
		}
		
	}

	@Override
	public void updateMission(Mission mission) throws DatabaseException {
		try {
			dbmanager.getConnection();
			dbmanager.updateMission(mission.getId(), mission.getName(), mission.getAlias(), mission.getDescription(), mission.getStart_date() , mission.getEnd_date());
			dbmanager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.toString());
		}
	}

	@Override
	public void deleteMission(int id) throws DatabaseException, DirectoryException {
		try {
			dbmanager.getConnection();
			dbmanager.deleteMission(id);
			dbmanager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.toString());
		}
		
		try{
			dirmanager.deleteMision(id);
		}catch(IOException e){
			e.printStackTrace();
			throw new DirectoryException(e.toString());
		}
	}
	
	@Override
	public File insertFile(File file) throws DatabaseException, DirectoryException {
		int id;
		try {
			dbmanager.getConnection();
			dbmanager.insertFile(file.getMission(), file.getName(), file.getFileType(),
					file.getDescription(), file.getDate_time(), file.getMD5());
			id = dbmanager.lastInsertID("file");
			dbmanager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.toString());
		}

		try {
			file.setId(id);
			dirmanager.changeFileDirectory(file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new DirectoryException(e.toString());
		}
		return file;
		
	}

	@Override
	public void updateFile(File file) throws DatabaseException {
		try {
			dbmanager.getConnection();
			dbmanager.updateFile(file.getId(), file.getName(), file.getDescription(), file.getDate_time() , file.getMD5());
			dbmanager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.toString());
		}
	}

	@Override
	public void deleteFile(File file) throws DatabaseException, DirectoryException {
		try{
			dirmanager.deleteFile(file);
		}catch(IOException e){
			e.printStackTrace();
			throw new DirectoryException(e.toString());
		}
		
		try {
			dbmanager.getConnection();
			dbmanager.deleteFile(file.getId());
			dbmanager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.toString());
		}
	}

	@Override
	public File selectFile(Integer id) throws DatabaseException {
		File file = new File();
		try {
			dbmanager.getConnection();
			file = dbmanager.selectFile(id.intValue());
			dbmanager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.toString());
		}
		return file;
	}

	@Override
	public Vector<MissionCell> fillTree() throws DatabaseException {
		Vector<MissionCell> missions = new Vector<MissionCell>();
		try {
			dbmanager.getConnection();
			missions = dbmanager.getTree();
			dbmanager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.toString());
		}
		return missions;
	}
}