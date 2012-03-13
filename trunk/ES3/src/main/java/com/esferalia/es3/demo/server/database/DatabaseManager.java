package com.esferalia.es3.demo.server.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.esferalia.es3.demo.client.dto.File;
import com.esferalia.es3.demo.client.dto.FileCell;
import com.esferalia.es3.demo.client.dto.FileType;
import com.esferalia.es3.demo.client.dto.Mission;
import com.esferalia.es3.demo.client.dto.MissionCell;

public class DatabaseManager extends DatabaseConnection {

	private Connection connection;
	
	// FIXME Logging vars Path
/*	static private FileHandler fileTxt;
	private final static Logger LOGGER = Logger.getLogger(DatabaseServiceImpl.class.getName());
	private final String LOG_PATH = "/srv/www/lighttpd/es3/Logging.txt";
		// "C:\\workspace\\ES3\\src\\com\\esferalia\\es3\\demo\\public\\Logging.txt";
		// "/srv/www/lighttpd/es3/Logging.txt";
*/	
	public DatabaseManager() {
/*		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.ALL);
		try {
			java.io.File logFile = new java.io.File(LOG_PATH);
			logFile.createNewFile();
			fileTxt = new FileHandler(LOG_PATH);
			logger.addHandler(fileTxt);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	public void getConnection() throws SQLException {
		connection = getConn();
/*		if (connection == null)
			LOGGER.severe("ERROR: I CAN'T GET A MYSQL DATABASE CONNECTION");
		else
			LOGGER.severe("I GET THE MYSQL DATABASE CONNECTION");*/
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
			file.setFileType(FileType.valueOf(rs1.getString(4)));
			file.setDescription(rs1.getString(5));
			file.setDate_time(rs1.getDate(6));
			file.setMD5(rs1.getString(7));
			
			rs1.close();
//			connection.close();
		} catch (SQLException e) {
			throw e;
		}
		return file;
	}
	
	public void insertFile(Integer identMission, String name, FileType type, String description, Date date, String MD5) throws SQLException {
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
					"  `type`," +
					"  `description`," +
					"  `date_time`," +
					"  `md5`" +
					")" +
					"VALUE (" +
//					"  :id," +
					"  '" + mission + "'," +
					"  '" + name + "'," +
					"  '" + type.toString() + "'," +
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
	
	public Vector<MissionCell> getTree() throws SQLException {
		Vector<MissionCell> misiones = new Vector<MissionCell>();
		Statement stmt = null;
		ResultSet rs1 = null;
		try {
			stmt = connection.createStatement();
			String query =
					"select mission.`id`, mission.`name`, file.`id`, file.`name`, file.`type` "
					+ " from mission left join file on `mission`.id = `file`.`mission` "
					+ " order by mission.id, `file`.`type` ;";
			rs1 = stmt.executeQuery(query);
			rs1.next();
			while (!rs1.isAfterLast()) {
				if (rs1.isFirst()) {
					MissionCell mision = new MissionCell();
					mision.setId(rs1.getInt(1));
					mision.setName(rs1.getString(2));
					if (rs1.getInt(3) != 0) {
						FileCell archivo = new FileCell();
						archivo.setId(rs1.getInt(3));
						archivo.setMission(rs1.getInt(1));
						archivo.setOriginalName(rs1.getString(4));
						int punto = rs1.getString(4).lastIndexOf(".");
						archivo.setShortName(rs1.getString(4).substring(0, punto));
						if (rs1.getString(5).equals(FileType.audio.toString())) {
							archivo.setType(FileType.audio);
							mision.addAudio(archivo);
						} else if (rs1.getString(5).equals(FileType.video.toString())) {
							archivo.setType(FileType.video);
							mision.addVideo(archivo);
						} else if (rs1.getString(5).equals(FileType.imagen.toString())) {
							archivo.setType(FileType.imagen);
							mision.addImage(archivo);
						} else if (rs1.getString(5).equals(FileType.cartografia.toString())) {
							archivo.setType(FileType.cartografia);
							mision.addGPS(archivo);
						} else {
							archivo.setType(FileType.documento);
							mision.addDoc(archivo);
						}
					}
					misiones.add(mision);
				} else {
					MissionCell lastInserted = misiones.get(misiones.size()-1);
					if (lastInserted.getId() == rs1.getInt(1)) {
						if (rs1.getInt(3) != 0) {
							FileCell archivo = new FileCell();
							archivo.setId(rs1.getInt(3));
							archivo.setMission(rs1.getInt(1));
							archivo.setOriginalName(rs1.getString(4));
							int punto = rs1.getString(4).lastIndexOf(".");
							archivo.setShortName(rs1.getString(4).substring(0, punto));
							if (rs1.getString(5).equals(FileType.audio.toString())) {
								archivo.setType(FileType.audio);
								lastInserted.addAudio(archivo);
							} else if (rs1.getString(5).equals(FileType.video.toString())) {
								archivo.setType(FileType.video);
								lastInserted.addVideo(archivo);
							} else if (rs1.getString(5).equals(FileType.imagen.toString())) {
								archivo.setType(FileType.imagen);
								lastInserted.addImage(archivo);
							} else if (rs1.getString(5).equals(FileType.cartografia.toString())) {
								archivo.setType(FileType.cartografia);
								lastInserted.addGPS(archivo);
							} else {
								archivo.setType(FileType.documento);
								lastInserted.addDoc(archivo);
							}
						}						
						misiones.set(misiones.size()-1, lastInserted);
					}
					else {
						MissionCell mision = new MissionCell();
						mision.setId(rs1.getInt(1));
						mision.setName(rs1.getString(2));
						if (rs1.getInt(3) != 0) {
							FileCell archivo = new FileCell();
							archivo.setId(rs1.getInt(3));
							archivo.setMission(rs1.getInt(1));
							archivo.setOriginalName(rs1.getString(4));
							int punto = rs1.getString(4).lastIndexOf(".");
							archivo.setShortName(rs1.getString(4).substring(0, punto));
							if (rs1.getString(5).equals(FileType.audio.toString())) {
								archivo.setType(FileType.audio);
								mision.addAudio(archivo);
							} else if (rs1.getString(5).equals(FileType.video.toString())) {
								archivo.setType(FileType.video);
								mision.addVideo(archivo);
							} else if (rs1.getString(5).equals(FileType.imagen.toString())) {
								archivo.setType(FileType.imagen);
								mision.addImage(archivo);
							} else if (rs1.getString(5).equals(FileType.cartografia.toString())) {
								archivo.setType(FileType.cartografia);
								mision.addGPS(archivo);
							} else {
								archivo.setType(FileType.documento);
								mision.addDoc(archivo);
							}
						}
						misiones.add(mision);
					}
				}
				rs1.next();
			}

			rs1.close();
		} catch (SQLException e) {
			throw e;
		}
		return misiones;
	}
}
