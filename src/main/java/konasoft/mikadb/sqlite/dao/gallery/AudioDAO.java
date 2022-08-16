package konasoft.mikadb.sqlite.dao.gallery;

import konasoft.mikadb.model.gallery.AudioModel;
import konasoft.mikadb.sqlite.dao.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AudioDAO extends DAO<AudioModel> {
    public static final String TABLE_NAME = "audio";

    /**
     * constructors
     * */
    public AudioDAO() {
        super();
    }

    /**
     * utils
     * */
    public List<AudioModel> all(String cat) throws SQLException {
        return query(cat, "");
    }

    public List<AudioModel> query(String cat, String sortBy) throws SQLException {
        String q = "select * from " + getTableName() + " where cat='" + cat + "'";
        if (sortBy.isBlank()) {
            q += " order by tier asc, title desc";
        } else {
            q += " " + sortBy;
        }
        q += ";";
        //
        Statement state = getDatabaseAccess().getConn().createStatement();
        ResultSet rs = state.executeQuery(q);
        //
        List<AudioModel> res = new ArrayList<>();
        while (rs.next()) res.add(objectConstructor(rs));
        return res;
    }

    /**
     * overriding
     * */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public AudioModel objectConstructor(ResultSet rs) throws SQLException {
        return new AudioModel(
                rs.getInt("id"),
                rs.getString("cat"),
                rs.getString("title"),
                rs.getString("src"),
                rs.getString("src_link"),
                rs.getBoolean("available"),
                rs.getInt("tier")
        );
    }
}
