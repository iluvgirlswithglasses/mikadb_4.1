package konasoft.mikadb.sqlite.dao.lists;

import konasoft.mikadb.api.DataAccess;
import konasoft.mikadb.decoder.DateDecoder;
import konasoft.mikadb.decoder.game.RatingDecoder;
import konasoft.mikadb.model.lists.GameModel;
import konasoft.mikadb.model.lists.FranchiseModel;
import konasoft.mikadb.model.comps.SeasonalDate;
import konasoft.mikadb.parser.CalendarParser;
import konasoft.mikadb.sqlite.dao.DAO;
import konasoft.mikadb.sqlite.dao.ProfilePicHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Function;

public class GameDAO extends DAO<GameModel> {
    /**
     * infos
     * */
    public static final String TABLE_NAME = "game";

    private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();
    private static DateDecoder dateDecoder = DateDecoder.getInstance();

    public static final String[] FIELDS = {
            "id",
            "title",
            "franchise",
            "rating",
            "start_date",
            "complete_date",
            "comment",
    };

    @Override
    public String[] getFields() {
        return FIELDS;
    }


    /**
     * special getters
     * */
    public String getProfilePic(int id) throws URISyntaxException {
        return ProfilePicHelper.getProfilePic("/webcontent/lists/game/entry/pp/", "" + id);
    }


    /**
     * overriding
     * */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public GameModel objectConstructor(ResultSet rs) throws SQLException {
        CalendarParser calendarParser = new CalendarParser();
        return new GameModel(
            rs.getInt("id"),
            rs.getString("title"),
            new FranchiseModel(rs.getString("franchise"), "game"),
            rs.getInt("rating"),
            calendarParser.sqliteStringToSeasonalDate(rs.getString("start_date")),
            calendarParser.sqliteStringToSeasonalDate(rs.getString("complete_date")),
            rs.getString("comment")
        );
    }

    @Override
    public void add(GameModel e) throws SQLException {
        String statement = String.format(
            "insert into %s (%s) values (%s)", TABLE_NAME, prepareSqliteColumns(), prepareSqliteValues()
        );
        executeUpdate(e, statement);
    }

    @Override 
    public void update(GameModel e) throws SQLException {
        String statement = String.format(
            "update %s set %s where id=%s", TABLE_NAME, prepareSqliteSettings(), e.getId()
        );
        executeUpdate(e, statement);
    }

    @Override
    // follow STRICTLY the order from FIELDS
    public void passParams(GameModel e, PreparedStatement st) throws SQLException {
        CalendarParser calendarParser = new CalendarParser();
        int i = 0;
        st.setString(++i, e.getTitle());
        st.setString(++i, e.getFranchise().getName());
        st.setInt(++i, e.getRating());
        st.setString(++i, calendarParser.seasonalDateToSQLiteString(e.getStartDate()));
        st.setString(++i, calendarParser.seasonalDateToSQLiteString(e.getCompleteDate()));
        st.setString(++i, e.getComment());
    }

    private void executeUpdate(GameModel e, String statement) throws SQLException {
        // auto generate franchise if not exist
        FranchiseDAO frDAO = new FranchiseDAO();
        if (e.getFranchise().getName().equals("")) {
            // set franchise as title
            e.setFranchise(new FranchiseModel(e.getTitle(), "game"));
            // add franchise to db
            if (!frDAO.isExists(e.getFranchise())) frDAO.add(e.getFranchise());
        }
        //
        PreparedStatement ps = getDatabaseAccess().getConn().prepareStatement(statement);
        passParams(e, ps);
        ps.executeUpdate();
    }

    /**
     * querying
     * */
    public LinkedHashMap<String, List<GameModel>> loadFromFranchise(String s) throws SQLException {
        List<GameModel> res = new ArrayList<>();
        //
        try {
            ResultSet rs = getDatabaseAccess().getConn().createStatement().executeQuery(
                    "select * from game where franchise='" + s.replace("'", "''") + "' order by id;"
            );
            while (rs.next()) res.add(objectConstructor(rs));
        } catch (SQLException e) {
            return sortByCompleteDate("");
        }
        //
        return new LinkedHashMap<>(){{
            put(s, res);
        }};
    }

    /**
     * sort utils
     * */
    public LinkedHashMap<String, List<GameModel>> sortByTitle(String filterQuery) throws SQLException {
        return sortBy(
                String.format("select * from game %s order by title asc", filterQuery),
                (game) -> game.getTitle().substring(0, 1).toUpperCase()
        );
    }

    public LinkedHashMap<String, List<GameModel>> sortByRating(String filterQuery) throws SQLException {
        return sortBy(
                String.format("select * from game %s order by rating asc, title asc", filterQuery),
                (game) -> ratingDecoder.RATING_FULL[game.getRating()]
        );
    }

    public LinkedHashMap<String, List<GameModel>> sortByCompleteDate(String filterQuery) throws SQLException {
        return sortBy(
                String.format("select * from game %s order by complete_date desc, id desc", filterQuery),
                (game) -> String.format("%s", dateDecoder.get(game.getCompleteDate().getYear()))
        );
    }

    public LinkedHashMap<String, List<GameModel>> sortByFranchise(String filterQuery) throws SQLException {
        return sortBy(
                String.format("select * from game %s order by franchise asc, id asc", filterQuery),
                (game) -> String.format("%s", game.getFranchise().getName())
        );
    }

    /**
     * basically object constructor from servlet request
     * */
    public void updateModelFromServletRequest(GameModel game, HttpServletRequest request) {
        game.setTitle( request.getParameter("title") );
        game.setFranchise(new FranchiseModel( request.getParameter("franchise"), "game" ));
        game.setRating(Integer.parseInt( request.getParameter("rating") ));
        game.setStartDate(new SeasonalDate(
            Integer.parseInt( request.getParameter("start-date-year") ),
            Integer.parseInt( request.getParameter("start-date-season") ),
            Integer.parseInt( request.getParameter("start-date-month") ),
            Integer.parseInt( request.getParameter("start-date-day") )
        ));
        game.setCompleteDate(new SeasonalDate(
            Integer.parseInt( request.getParameter("complete-date-year") ),
            Integer.parseInt( request.getParameter("complete-date-season") ),
            Integer.parseInt( request.getParameter("complete-date-month") ),
            Integer.parseInt( request.getParameter("complete-date-day") )
        ));
        game.setComment( request.getParameter("comment") );
    }
}
