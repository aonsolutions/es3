package com.esferalia.es3.demo.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;

import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.dto.Mission;

public class DatabaseManager extends DatabaseConnection {

	private Connection connection;
	
	public DatabaseManager() {
	}

	public void getConnection() throws SQLException {
		connection = getConn();
	}
	
	public Mission selectMission(Integer ident) throws SQLException {
		Statement stmt = null;
		ResultSet rs1 = null;
		int id = ident.intValue();
		Mission mision = new Mission();
		
		try {
			stmt = connection.createStatement();
			String query =
					"SELECT * " +
					" FROM" +
					"  `mission`" +
					" WHERE `id` = '" + id + "';";
			rs1 = stmt.executeQuery(query);
			rs1.next();
			
			mision.setId(rs1.getInt(1));
			mision.setName(rs1.getString(2));
			mision.setAlias(rs1.getString(3));
			mision.setDescription(rs1.getString(4));
			mision.setStart_date(rs1.getDate(5));
			mision.setEnd_date(rs1.getDate(6));
			
			rs1.close();
//			connection.close();
		} catch (SQLException e) {
			throw e;
		}
		return mision;
	}

	public void insertMission(String name, String alias, String description, Date start, Date end) throws SQLException {
		Statement stmt = null;
		
		java.sql.Date start_date = new java.sql.Date(start.getTime());
		java.sql.Date end_date = new java.sql.Date(end.getTime());

		try {
			stmt = connection.createStatement();
			String query =
					"INSERT INTO `mission`(" +
//					"  `id`," +
					"  `name`," +
					"  `alias`," +
					"  `description`," +
					"  `start_date`," +
					"  `end_date`" +
					")" +
					"VALUE (" +
//					"  :id," +
					"  '" + name + "'," +
					"  '" + alias + "'," +
					"  '" + description + "'," +
					"  '" + start_date + "'," +
					"  '" + end_date +
					"');";
			stmt.executeUpdate(query);
//			connection.close();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public void updateMission(Integer ident, String name, String alias, String description, Date start, Date end) throws SQLException {
		Statement stmt = null;
		
		java.sql.Date start_date = new java.sql.Date(start.getTime());
		java.sql.Date end_date = new java.sql.Date(end.getTime());
		int id = ident.intValue();

		try {
			stmt = connection.createStatement();
			String query =
					"UPDATE `mission`" +
					" SET" +
					"  `name` = '" + name + "'," +
					"  `alias` = '" + alias + "'," +
					"  `description` = '" + description + "'," +
					"  `start_date` = '" + start_date + "'," +
					"  `end_date` = '" + end_date + 
					"' WHERE" +
					"  `id` = '" + id +
					"';";
			stmt.executeUpdate(query);
//			connection.close();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public void deleteMission(Integer ident) throws SQLException {
		Statement stmt = null;
		
		int id = ident.intValue();

		try {
			stmt = connection.createStatement();
			String query = "DELETE FROM `mission` WHERE `id` = '" + id + "';";
			stmt.executeUpdate(query);
//			connection.close();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public File selectFile(Integer ident) throws SQLException {
		Statement stmt = null;
		ResultSet rs1 = null;
		File file = new File();
		
		int id = ident.intValue();
		
		try {
			stmt = connection.createStatement();
			String query =
					"SELECT * " +
					" FROM" +
					"  `file`" +
					" WHERE `id` = '" + id + "';";
			rs1 = stmt.executeQuery(query);
			rs1.next();
			
			file.setId(rs1.getInt(1));
			file.setMission(rs1.getInt(2));
			file.setName(rs1.getString(3));
			file.setDescription(rs1.getString(4));
			file.setDate_time(rs1.getDate(5));
			file.setMD5(rs1.getString(6));
			
			rs1.close();
//			connection.close();
		} catch (SQLException e) {
			throw e;
		}
		return file;
	}
	
	public void insertFile(Integer identMission, String name, String description, Date date, String MD5) throws SQLException {
		Statement stmt = null;
		
		int mission = identMission.intValue();
		java.sql.Date date_time = new java.sql.Date(date.getTime());

		try {
			stmt = connection.createStatement();
			String query =
					"INSERT INTO `file` (" +
//					"  `id`," +
					"  `mission`," +
					"  `name`," +
					"  `description`," +
					"  `date_time`," +
					"  `md5`" +
					")" +
					"VALUE (" +
//					"  :id," +
					"  '" + mission + "'," +
					"  '" + name + "'," +
					"  '" + description + "'," +
					"  '" + date_time + "'," +
					"  '" + MD5 +
					"');";
			stmt.executeUpdate(query);
//			connection.close();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public void updateFile(Integer ident, String name, String description, Date date, String MD5) throws SQLException {
		Statement stmt = null;
		
		int id = ident.intValue();
		java.sql.Date date_time = new java.sql.Date(date.getTime());

		try {
			stmt = connection.createStatement();
			String query =
					"UPDATE `file`" +
					" SET" +
//					"  `mission` = :mission," +
					"  `name` = '" + name + "'," +
					"  `description` = '" + description + "'," +
					"  `date_time` = '" + date_time + "'," +
					"  `md5` = '" + MD5 +
					"' WHERE" +
					"  `id` = '" + id +
					"';";
			stmt.executeUpdate(query);
//			connection.close();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public void deleteFile(Integer ident) throws SQLException {
		Statement stmt = null;
		
		int id = ident.intValue();

		try {
			stmt = connection.createStatement();
			String query = "DELETE FROM `file` WHERE `id` = '" + id + "';";
			stmt.executeUpdate(query);
//			connection.close();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public int lastInsertID(String nameTable) throws SQLException {
		Statement stmt = null;
		ResultSet rs1 = null;
		int id;
		
		try {
			stmt = connection.createStatement();
			String query = "SELECT LAST_INSERT_ID() FROM " + nameTable + ";";
			rs1 = stmt.executeQuery(query);
			rs1.next();
			id= rs1.getInt(1);
			rs1.close();
//			connection.close();
		} catch (SQLException e) {
			throw e;
		}
		return id;
	}

	public void closeConnection() throws SQLException {
		try {
			connection.close();
		} catch (SQLException e) {
			throw e;
		}
	}
}
