package to.networld.schandler.security;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Persistent storage of the access rights in combination with hash values of the RFID UIDs.
 * Sqlite3 is used as database backend with the filename of the database as constructor
 * parameter.
 * 
 * @author Alex Oberhauser
 */
public class AccessHandler {
	private final String DB_FILENAME;
	private final String SQL_DATABASE;
	
	protected AccessHandler(String _dbFilename) throws ClassNotFoundException, SQLException {
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
			statement.executeUpdate("CREATE TABLE rights (uidHash STRING PRIMARY KEY, accessRights INTEGER)");
	}
	
	/**
	 * Returns the related access rights of a RFID card.
	 * 
	 * @param _cardUIDHash The UID of the card as hash value.
	 * @return The access rights for the given card.
	 */
	protected int getAccessRights(String _cardUIDHash) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(SQL_DATABASE);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			ResultSet rs = statement.executeQuery("SELECT accessRights FROM rights WHERE uidHash = '" + _cardUIDHash.replace(" ", "") + "'");
			if ( rs.next() )
				return rs.getInt("accessRights");
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
		return -1;
	}
	
	/**
	 * Stores or updates the access rights of a RFID card into the database.
	 * 
	 * @param _cardUIDHash The hash value of the RFID UID.
	 * @param _key The key in clear text.
	 */
	protected void storeAccessRights(String _cardUIDHash, int _accessRights) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(SQL_DATABASE);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("DELETE FROM rights WHERE uidHash = '" + _cardUIDHash + "'");
			statement.executeUpdate("INSERT INTO rights VALUES('" + _cardUIDHash + "' , " + _accessRights + ")");
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
