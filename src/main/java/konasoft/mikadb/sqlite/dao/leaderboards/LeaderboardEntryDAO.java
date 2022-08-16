package konasoft.mikadb.sqlite.dao.leaderboards;

import konasoft.mikadb.model.leaderboards.LeaderboardEntry;
import konasoft.mikadb.model.leaderboards.LeaderboardModel;
import konasoft.mikadb.model.leaderboards.lists.LeaderboardAnimeModel;
import konasoft.mikadb.model.leaderboards.lists.LeaderboardEntryModel;
import konasoft.mikadb.sqlite.DatabaseAccess;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardEntryDAO {
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
    public LeaderboardAnimeModel animeConstructor(ResultSet rs) throws SQLException {
        String id = rs.getString("entry_id");
        ResultSet anime = getCreatedStatement().executeQuery(
            "select title from anime where id=" + id + ";"
        );
        return new LeaderboardAnimeModel(id, anime.getString("title"));
    }

    public LeaderboardEntry<LeaderboardEntryModel> entryConstructor(ResultSet rs) throws SQLException {
        // ranking
        LeaderboardEntry<LeaderboardEntryModel> res = new LeaderboardEntry<>(
            rs.getInt("id"), rs.getInt("leaderboard_id"), rs.getInt("rank"), rs.getString("description")
        );
        // model
        switch (rs.getString("entry_table")) {
            case "anime":
                res.setModel(animeConstructor(rs));
                return res;
            case "film":
            case "game":
            case "music":
            case "char":
            case "misc":
            default:
                return null;
        }
    }
    
    /**
     * object constructors from http servlet request
     * */
    public LeaderboardAnimeModel animeConstructor(HttpServletRequest request) throws SQLException {
        String id = request.getParameter("entry-id");
        ResultSet anime = getCreatedStatement().executeQuery(
            "select title from anime where id=" + id + ";"
        );
        return new LeaderboardAnimeModel(id, anime.getString("title"));
    }

    public LeaderboardEntry<LeaderboardEntryModel> entryConstructor(HttpServletRequest request) {
        try {
            // ranking
            LeaderboardEntry<LeaderboardEntryModel> res = new LeaderboardEntry<>(
                Integer.parseInt(request.getParameter("id")),
                Integer.parseInt(request.getParameter("leaderboard-id")),
                Integer.parseInt(request.getParameter("rank")),
                request.getParameter("description")
            );
            // model
            switch (request.getParameter("entry-table")) {
                case "anime":
                    res.setModel(animeConstructor(request));
                    return res;
                case "film":
                case "game":
                case "music":
                case "char":
                case "misc":
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * querying
     * */
    public LeaderboardEntry<LeaderboardEntryModel> get(int id) {
        try {
            ResultSet rs = getCreatedStatement().executeQuery(
                "select * from leaderboard_entry where id=" + id + ";"
            );
            if (rs.next()) return entryConstructor(rs);
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public List<LeaderboardEntry<LeaderboardEntryModel>> loadLeaderboard(int leaderboardId) throws SQLException {
        List<LeaderboardEntry<LeaderboardEntryModel>> res = new ArrayList<>();
        // get leaderboard
        LeaderboardModel leaderboard = new LeaderboardDAO().get(leaderboardId);
        if (leaderboard == null) return new ArrayList<>();
        //
        ResultSet rs = getCreatedStatement().executeQuery(
            "select * from leaderboard_entry where leaderboard_id=" + leaderboardId + ";"
        );
        while (rs.next()) res.add(entryConstructor(rs));
        //
        if (leaderboard.isRanked()) {
            rankedSort(res);
        } else {
            unrankedSort(res);
        }
        return res;
    }

    public void deleteLeaderboard(int leaderboardId) throws SQLException {
        getCreatedStatement().executeUpdate(
            "delete * from leaderboard_entry where leaderboard_id=" + leaderboardId + ";"
        );
    }

    private void rankedSort(List<LeaderboardEntry<LeaderboardEntryModel>> lst) throws SQLException {
        lst.sort((i, j) -> {
            if (i.getRank() == j.getRank()) {
                return i.getModel().getTitle().compareTo(j.getModel().getTitle());
            } else {
                return i.getRank() - j.getRank();
            }
        });
    }

    private void unrankedSort(List<LeaderboardEntry<LeaderboardEntryModel>> lst) throws SQLException {
        lst.sort(Comparator.comparing(i -> i.getModel().getTitle()));
    }

    /**
     * modifying leaderboards entry
     * */
    public void delete(int id) throws SQLException {
        getCreatedStatement().executeUpdate("delete from leaderboard_entry where id=" + id + ";");
    }

    public void add(LeaderboardEntry<LeaderboardEntryModel> e) throws SQLException {
        PreparedStatement state = getDatabaseAccess().getConn().prepareStatement(
            "insert into leaderboard_entry (entry_table, entry_id, leaderboard_id, rank, description) values (?, ?, ?, ?, ?);"
        );
        update(state, e);
    }

    public void update(LeaderboardEntry<LeaderboardEntryModel> e) throws SQLException {
        PreparedStatement state = getDatabaseAccess().getConn().prepareStatement(
            "update leaderboard_entry set entry_table=?, entry_id=?, leaderboard_id=?, rank=?, description=? where id=" + e.getId() + ";"
        );
        update(state, e);
    }

    private void update(PreparedStatement state, LeaderboardEntry<LeaderboardEntryModel> e) throws SQLException {
        int i = 0;
        i++; state.setString(i, e.getModel().getEntryTable());
        i++; state.setString(i, e.getModel().getEntryId());
        i++; state.setInt(i, e.getLeaderboardId());
        i++; state.setInt(i, e.getRank());
        i++; state.setString(i, e.getDescription());
        //
        state.executeUpdate();
    }
}
