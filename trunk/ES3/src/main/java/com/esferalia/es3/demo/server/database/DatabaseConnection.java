package com.esferalia.es3.demo.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConnection {

	public DatabaseConnection() {

	}

	/**
	 * getConn
	 *
	 * Make sure you add a reference library (external jar in build path)
	 * JDBC Connector mysql-connector-java-X.X.X-bin.jar
	 *
	 * Make sure you add the connector in the buildpath
	 * /war/WEB-INF/lib/mysql-connector-java-X.X.X-bin.jar
	 *
	 * @return Connection
	 */
	protected Connection getConn() {

		Connection conn = null;

		String url  = "127.0.0.1:3306/";
		String db = "es3";
		String driver ="com.mysql.jdbc.Driver";
		String user = "dbuser";
		String pass = "serubd2000";
		url = url + db;

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://"+url+"?" +
					"user="+user+"&password="+pass);
		} catch (SQLException sqlEx) {
			System.err.println("Mysql Connection Error: " + sqlEx.toString());
		}
		catch (Exception e) {
			System.err.println("Unknown Error: "+e.toString());
		}

		if (conn == null)  {
			System.out.println("~~~~~~~~~~ can't get a Mysql connection");
		}

		return conn;
	}

}


