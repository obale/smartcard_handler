/**
 * SmartCard Access Library
 *
 * Copyright (C) 2010 by Networld Project
 * Written by Alex Oberhauser <oberhauseralex@networld.to>
 * All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>
 */

package to.networld.schandler.security;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Persistent storage of the key values in combination with hash values of the RFID UIDs.
 * Sqlite3 is used as database backend with the filename of the database as constructor
 * parameter.
 * 
 * @author Alex Oberhauser
 */
public class KeyHandler {	
	private final String DB_FILENAME;
	private final String SQL_DATABASE;
	
	protected KeyHandler(String _dbFilename) throws ClassNotFoundException, SQLException {
		this.DB_FILENAME = _dbFilename;
		this.SQL_DATABASE = "jdbc:sqlite:" + DB_FILENAME;
		
		Class.forName("org.sqlite.JDBC");

		File fd = new File(DB_FILENAME);
		boolean fileExists = fd.exists();
		
		Connection connection;
		connection = DriverManager.getConnection(SQL_DATABASE);

		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);
		
		if ( !fileExists )
			statement.executeUpdate("CREATE TABLE acl (uidHash STRING PRIMARY KEY, clearToken BLOB)");
	}
	
	/**
	 * Returns the related key of a RFID card.
	 * 
	 * @param _cardUIDHash The UID of the card as hash value.
	 * @return The related key to the given card.
	 */
	protected String getKey(String _cardUIDHash) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(SQL_DATABASE);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			ResultSet rs = statement.executeQuery("SELECT clearToken FROM acl WHERE uidHash = '" + _cardUIDHash.replace(" ", "") + "'");
			if ( rs.next() )
				return rs.getString("clearToken");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        if(connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
	
	/**
	 * Stores or updates the related key of a RFID card into the database.
	 * 
	 * @param _cardUIDHash The hash value of the RFID UID.
	 * @param _key The key in clear text.
	 */
	protected void storeKey(String _cardUIDHash, String _key) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(SQL_DATABASE);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("DELETE FROM acl WHERE uidHash = '" + _cardUIDHash.replace(" ", "") + "'");
			statement.executeUpdate("INSERT INTO acl VALUES('" + _cardUIDHash + "' , '" + _key + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        if(connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}
