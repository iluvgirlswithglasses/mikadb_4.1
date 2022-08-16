package konasoft.mikadb.sqlite.dao;

import konasoft.mikadb.sqlite.DatabaseAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Function;

public abstract class DAO<E> {
    DatabaseAccess access;

    public DAO() {
        access = DatabaseAccess.getInstance();
    }


    /**
     * must override
     * */
    public E objectConstructor(ResultSet rs) throws SQLException {
        return null;
    }

    public void add(E e) throws SQLException {

    }

    public void update(E e) throws SQLException {

    }


    /**
     * infomations
     * */
    public DatabaseAccess getDatabaseAccess() {
        return access;
    }

    public String getTableName() {
        return null;
    }

    public String[] getFields() {
        return null;
    }


    /**
     * querying tools
     * */
    public ResultSet executeQuery(String query) {
        try {
            Statement state = getDatabaseAccess().getConn().createStatement();
            return state.executeQuery(query);
        } catch (SQLException e) {
            return null;
        }
    }

    public List<E> all() throws SQLException {
        // query
        Statement state = getDatabaseAccess().getConn().createStatement();
        ResultSet rs = state.executeQuery("select * from " + getTableName());
        // handling result
        List<E> res = new ArrayList<>();
        while (rs.next()) res.add(objectConstructor(rs));
        //
        return res;
    }

    public int rowsCount() throws SQLException {
        Statement state = getDatabaseAccess().getConn().createStatement();
        ResultSet rs = state.executeQuery("select Count(*) from " + getTableName());
        rs.next();
        return rs.getInt(1);
    }

    public E get(int id) throws SQLException {
        // query
        Statement state = getDatabaseAccess().getConn().createStatement();
        ResultSet rs = state.executeQuery("select * from " + getTableName() + " where id=" + id);
        // handling result
        rs.next();
        return objectConstructor(rs);
    }


    /**
     * sort utils
     * */
    public LinkedHashMap<String, List<E>> sortBy(String query, Function<E, String> keyGen) throws SQLException {
        // prepare
        LinkedHashMap<String, List<E>> res = new LinkedHashMap<>();
        // query
        Statement state = getDatabaseAccess().getConn().createStatement();
        ResultSet rs = state.executeQuery(query);
        // handling results
        while (rs.next()) {
            E e = objectConstructor(rs);
            String key = keyGen.apply(e);
            //
            if (res.size() == 0) {
                //
                res.put(key, new ArrayList<>(Collections.singletonList(e)));
            } else {
                //
                if (res.containsKey(key)) {
                    res.get(key).add(e);
                } else {
                    res.put(key, new ArrayList<>(Collections.singletonList(e)));
                }
            }
        }
        //
        return res;
    }


    /**
     * inserting / updating tools
     * */
    // never do anything to id field
    // the following functions work based on the static FIELDS only
    public String prepareSqliteColumns() {
        StringBuilder b = new StringBuilder();
        for (int i = 1; i < getFields().length; i++) {
            b.append(getFields()[i]).append(", ");
        }
        // remove the last comma and return
        return b.toString().substring(0, b.length() - 2);
    }

    public String prepareSqliteValues() {
        String str = "?, ".repeat(getFields().length - 1);
        // remove the last comma and return
        return str.substring(0, str.length() - 2);
    }

    public String prepareSqliteSettings() {
        StringBuilder b = new StringBuilder();
        for (int i = 1; i < getFields().length; i++) {
            b.append(getFields()[i]).append("=?, ");
        }
        // remove last comma and return
        return b.toString().substring(0, b.length() - 2);
    }

    public void passParams(E e, PreparedStatement state) throws SQLException {

    }
}
