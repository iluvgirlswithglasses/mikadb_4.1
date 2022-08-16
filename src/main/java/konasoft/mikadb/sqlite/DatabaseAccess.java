package konasoft.mikadb.sqlite;

import konasoft.mikadb.api.DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseAccess {
    // instance
    private static DatabaseAccess instance;
    private static DatabaseAccess systemInstance;

    public static DatabaseAccess getInstance() {
        if (instance == null) instance = new DatabaseAccess("db.sqlite3");
        return instance;
    }

    public static DatabaseAccess getSystemInstance() {
        if (systemInstance == null) systemInstance = new DatabaseAccess("sys.sqlite3");
        return systemInstance;
    }

    // fields
    private String databaseDir;
    private Connection conn;

    // constructor
    protected DatabaseAccess(String filename) {
        //
        try {
            // get database location
            databaseDir = DataAccess.getProjectDir() + "/" + filename;
            // establish database connection
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(String.format("jdbc:sqlite:/%s", databaseDir));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // getters
    public String getDatabaseDir() {
        return databaseDir;
    }

    public Connection getConn() {
        return conn;
    }
}
