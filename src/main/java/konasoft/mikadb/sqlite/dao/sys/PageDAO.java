package konasoft.mikadb.sqlite.dao.sys;

import konasoft.mikadb.model.sys.PageModel;
import konasoft.mikadb.sqlite.DatabaseAccess;
import konasoft.mikadb.sqlite.dao.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PageDAO extends DAO<PageModel> {
    public static final String TABLE_NAME = "page";

    private DatabaseAccess access;

    // @constructor
    public PageDAO() {
        access = DatabaseAccess.getSystemInstance();
    }

    // @upperclass
    @Override
    public DatabaseAccess getDatabaseAccess() {
        return access;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public PageModel objectConstructor(ResultSet rs) throws SQLException {
        return new PageModel(
                rs.getString("path"),
                rs.getString("display_name"),
                rs.getString("lord"),
                rs.getString("sub_lord")
        );
    }

    // @personal
    public PageModel getPage(String path) throws SQLException {
        PreparedStatement state = getDatabaseAccess().getConn().prepareStatement("select * from page where path=?");
        state.setString(1, path);
        ResultSet rs = state.executeQuery();
        // handle result
        rs.next();
        return objectConstructor(rs);
    }

    public List<PageModel> getChildPages(String path) throws SQLException {
        // init
        path += '/';
        List<PageModel> res = new ArrayList<>();
        // statement
        PreparedStatement state = getDatabaseAccess().getConn().prepareStatement(
                "select * from page where path like ?"
        );
        // 'path%':  must start with path
        // '%path%': contains path
        state.setString(1, path + '%');
        ResultSet rs = state.executeQuery();
        // handle result
        while (rs.next()) {
            res.add(objectConstructor(rs));
        }
        //
        return res;
    }
}
