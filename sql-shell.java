import java.sql.*;

import java.io.File;
import java.net.URISyntaxException;

public class Shell {
	static Connection conn;

	/**
	 * write your code here
	 * */
	static void exc() throws Exception {
		ResultSet rs = conn.createStatement().executeQuery(
			"select id from anime;"
		);
		while (rs.next()) {
			PreparedStatement state = conn.prepareStatement(
					"update anime set rating=11 where id=?;"
			);
			int id = rs.getInt("id");
			state.setInt(1, id);
			state.executeUpdate();
		}
	}

	/**
	 * drivers
	 * */
	static void connect() throws Exception {
		conn = DriverManager.getConnection(String.format(
			"jdbc:sqlite:%s/%s", 
			System.getProperty("user.dir"), 
			"db.sqlite3"
		));
	}

	public static void main(String[] args) {
		try {
			connect();
			exc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
