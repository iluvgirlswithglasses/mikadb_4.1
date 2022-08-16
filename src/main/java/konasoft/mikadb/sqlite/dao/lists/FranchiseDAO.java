package konasoft.mikadb.sqlite.dao.lists;

import konasoft.mikadb.model.lists.FranchiseModel;
import konasoft.mikadb.sqlite.DatabaseAccess;
import konasoft.mikadb.sqlite.dao.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class FranchiseDAO extends DAO<FranchiseModel> {
    public static final String TABLE_NAME = "franchise";

    /**
     * constructors
     * */
    public FranchiseDAO() {
        super();
    }


    /**
     * utils
     * */
    public boolean isExists(FranchiseModel fr) throws SQLException {
        return isExists(fr.getName());
    }

    public boolean isExists(String name) throws SQLException {
        Statement state = getDatabaseAccess().getConn().createStatement();
        ResultSet rs = state.executeQuery(
            "select * from franchise where name='" + name.replace("'", "''") + "';"
        );
        return rs.next();
    }

    public void delete(String s) throws SQLException {
        getDatabaseAccess().getConn().createStatement().executeUpdate(
                "delete from franchise where name='" + s + "';"
        );
    }


    /**
     * overridings
     * */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<FranchiseModel> all() throws SQLException {
        return null;
    }

    public List<FranchiseModel> all(String db) throws SQLException {
        // query
        Statement state = getDatabaseAccess().getConn().createStatement();
        ResultSet rs = state.executeQuery(String.format(
            "select * from %s where db='%s' order by name asc;", getTableName(), db
        ));
        // handling result
        List<FranchiseModel> res = new ArrayList<>();
        while (rs.next()) res.add(objectConstructor(rs));
        //
        return res;
    }

    @Override
    public FranchiseModel objectConstructor(ResultSet rs) throws SQLException {
        return new FranchiseModel(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("db")
        );
    }

    @Override
    public void add(FranchiseModel franchise) throws SQLException {
        PreparedStatement st = getDatabaseAccess().getConn().prepareStatement(
            "insert into franchise (name, db) values (?, ?);"
        );
        st.setString(1, franchise.getName());
        st.setString(2, franchise.getDb());
        st.executeUpdate();
    }

    @Override
    public void update(FranchiseModel franchise) throws SQLException {

    }

    public void rename(String from, String to) throws SQLException {
        // 
        PreparedStatement fstate = getDatabaseAccess().getConn().prepareStatement(
            "update franchise set name=? where name=?;"
        );
        fstate.setString(1, to);
        fstate.setString(2, from);
        fstate.executeUpdate();
        // 
        PreparedStatement astate = getDatabaseAccess().getConn().prepareStatement(
            "update anime set franchise=? where franchise=?;"
        );
        astate.setString(1, to);
        astate.setString(2, from);
        astate.executeUpdate();
    }
}
