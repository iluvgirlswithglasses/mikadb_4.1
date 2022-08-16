package konasoft.mikadb.sqlite.dao.leaderboards;

import konasoft.mikadb.model.leaderboards.LeaderboardModel;
import konasoft.mikadb.sqlite.DatabaseAccess;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardDAO {
    /**
     * database connection
     * */
    public DatabaseAccess getDatabaseAccess() {
        return DatabaseAccess.getInstance();
    }

    public Statement getCreatedStatement() throws SQLException {
        return getDatabaseAccess().getConn().createStatement();
    }
    
    /**
     * object constructors
     * */
    public LeaderboardModel leaderboardConstructor(ResultSet rs) throws SQLException {
        return new LeaderboardModel(
            rs.getInt("id"), rs.getInt("priority"), rs.getString("name"),
            rs.getString("description"), rs.getBoolean("ranked"), rs.getString("category")
        );
    }

    /**
     * object constructors from http request
     * */
    public LeaderboardModel leaderboardConstructor(HttpServletRequest request) {
        try {
            int id = Integer.parseInt( request.getParameter("id") );
            int priority = Integer.parseInt( request.getParameter("priority") );
            String name = request.getParameter("name");
            String desc = request.getParameter("description");
            boolean ranked = Boolean.parseBoolean( request.getParameter("ranked") );
            String cat = request.getParameter("category");
            //
            return new LeaderboardModel(
                id, priority, name, desc, ranked, cat
            );
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * querying
     * */
    public LeaderboardModel get(int id) {
        try {
            ResultSet rs = getCreatedStatement().executeQuery(
                "select * from leaderboard where id=" + id + ";"
            );
            if (rs.next()) return leaderboardConstructor(rs);
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<LeaderboardModel> all() {
        List<LeaderboardModel> res = new ArrayList<>();
        //
        try {
            ResultSet rs = getCreatedStatement().executeQuery(
                "select * from leaderboard order by priority asc, name desc;"
            );
            while (rs.next()) {
                res.add(leaderboardConstructor(rs));
            }
        } catch (SQLException e) {
            // if anything happens: return empty list
            return new ArrayList<>();
        }
        // nothing happens
        return res;
    }

    /**
     * modifying leaderboards
     * */
    public void delete(int id) throws SQLException {
        getCreatedStatement().executeUpdate("delete from leaderboard where id=" + id + ";");
        new LeaderboardEntryDAO().deleteLeaderboard(id);
    }

    public void add(LeaderboardModel e) throws SQLException {
        PreparedStatement state = getDatabaseAccess().getConn().prepareStatement(
            "insert into leaderboard (priority, name, description, ranked, category) values (?, ?, ?, ?, ?);"
        );
        update(state, e);
    }

    public void update(LeaderboardModel e) throws SQLException {
        PreparedStatement state = getDatabaseAccess().getConn().prepareStatement(
            "update leaderboard set priority=?, name=?, description=?, ranked=?, category=? where id=" + e.getId() + ";"
        );
        update(state, e);
    }

    private void update(PreparedStatement state, LeaderboardModel e) throws SQLException {
        int i = 0;
        i++; state.setInt(i, e.getPriority());
        i++; state.setString(i, e.getName());
        i++; state.setString(i, e.getDescription());
        i++; state.setBoolean(i, e.isRanked());
        i++; state.setString(i, e.getCategory());
        //
        state.executeUpdate();
    }
}
