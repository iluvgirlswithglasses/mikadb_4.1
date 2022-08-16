package konasoft.mikadb.sqlite.dao.lists;

import konasoft.mikadb.api.DataAccess;
import konasoft.mikadb.decoder.DateDecoder;
import konasoft.mikadb.decoder.anime.RatingDecoder;
import konasoft.mikadb.decoder.anime.StateDecoder;
import konasoft.mikadb.model.lists.AnimeModel;
import konasoft.mikadb.model.lists.FranchiseModel;
import konasoft.mikadb.model.comps.SeasonalDate;
import konasoft.mikadb.parser.CalendarParser;
import konasoft.mikadb.parser.ListParser;
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

public class AnimeDAO extends DAO<AnimeModel> {
    /**
     * infos
     * */
    public static final String TABLE_NAME = "anime";

    private static RatingDecoder ratingDecoder = RatingDecoder.getInstance();
    private static StateDecoder stateDecoder = StateDecoder.getInstance();
    private static DateDecoder dateDecoder = DateDecoder.getInstance();

    public static final String[] FIELDS = {
            "id",
            "title",
            "franchise",
            "comment",
            "type",
            "state",
            "seen",
            "rating",
            "start_date",
            "complete_date",
            "episodes",
            "duration",
            "rewatch_notes",

            "aired",
            "ending_themes",
            "finished_airing",
            "licensors",
            "mal_id",
            "opening_themes",
            "premiered",
            "producers",
            "source",
            "status",
            "studios",
            "genres",
            "title_english",
            "title_japanese",
            "mal_url",
    };

    @Override
    public String[] getFields() {
        return FIELDS;
    }


    /**
     * special getters
     * */
    public String getProfilePic(int id) throws URISyntaxException {
        return ProfilePicHelper.getProfilePic("/webcontent/lists/anime/entry/pp/", "" + id);
    }


    /**
     * overriding
     * */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public AnimeModel objectConstructor(ResultSet rs) throws SQLException {
        //
        CalendarParser calendarParser = new CalendarParser();
        ListParser listParser = new ListParser();
        //
        return new AnimeModel(
                rs.getInt("id"),
                rs.getString("title"),
                new FranchiseModel(rs.getString("franchise"), "anime"),
                rs.getString("comment"),
                rs.getInt("type"),
                rs.getInt("state"),
                rs.getInt("seen"),
                rs.getInt("rating"),
                calendarParser.sqliteStringToSeasonalDate(rs.getString("start_date")),
                calendarParser.sqliteStringToSeasonalDate(rs.getString("complete_date")),
                rs.getInt("episodes"),
                rs.getInt("duration"),
                rs.getString("rewatch_notes"),

                calendarParser.sqliteStringToCalendar(rs.getString("aired")),
                listParser.stringToList(rs.getString("ending_themes")),
                calendarParser.sqliteStringToCalendar(rs.getString("finished_airing")),
                listParser.stringToList(rs.getString("licensors")),
                rs.getInt("mal_id"),
                listParser.stringToList(rs.getString("opening_themes")),
                rs.getString("premiered"),
                listParser.stringToList(rs.getString("producers")),
                rs.getString("source"),
                rs.getString("status"),
                listParser.stringToList(rs.getString("studios")),
                listParser.stringToList(rs.getString("genres")),
                rs.getString("title_english"),
                rs.getString("title_japanese"),
                rs.getString("mal_url")
        );
    }

    @Override
    public void add(AnimeModel e) throws SQLException {
        String statement = String.format(
                "insert into %s (%s) values (%s)", TABLE_NAME, prepareSqliteColumns(), prepareSqliteValues()
        );
        executeUpdate(e, statement);
    }

    @Override
    public void update(AnimeModel e) throws SQLException {
        String statement = String.format(
                "update %s set %s where id=%s", TABLE_NAME, prepareSqliteSettings(), e.getId()
        );
        executeUpdate(e, statement);
    }

    @Override
    // follow STRICTLY the order from FIELDS
    public void passParams(AnimeModel e, PreparedStatement state) throws SQLException {
        // prepare parsers
        CalendarParser calendarParser = new CalendarParser();
        ListParser listParser = new ListParser();
        // strictly follow the order of FIELDS
        // and then nothing wrong would happen
        int i = 0;
        i++; state.setString(i, e.getTitle());
        i++; state.setString(i, e.getFranchise().getName());
        i++; state.setString(i, e.getComment());
        i++; state.setInt(i, e.getType());
        i++; state.setInt(i, e.getState());
        i++; state.setInt(i, e.getSeen());
        i++; state.setInt(i, e.getRating());
        i++; state.setString(i, calendarParser.seasonalDateToSQLiteString( e.getStartDate() ));
        i++; state.setString(i, calendarParser.seasonalDateToSQLiteString( e.getCompleteDate() ));
        i++; state.setInt(i, e.getEpisodes());
        i++; state.setInt(i, e.getDuration());
        i++; state.setString(i, e.getRewatchNotes());

        i++; state.setString(i, calendarParser.calendarToSqliteString( e.getAired() ));
        i++; state.setString(i, listParser.listToString( e.getEndingThemes() ));
        i++; state.setString(i, calendarParser.calendarToSqliteString( e.getFinishedAiring() ));
        i++; state.setString(i, listParser.listToString( e.getLicensors() ));
        i++; state.setInt(i, e.getMalId());
        i++; state.setString(i, listParser.listToString( e.getOpeningThemes() ));
        i++; state.setString(i, e.getPremiered());
        i++; state.setString(i, listParser.listToString( e.getProducers() ));
        i++; state.setString(i, e.getSource());
        i++; state.setString(i, e.getStatus());
        i++; state.setString(i, listParser.listToString( e.getStudios() ));
        i++; state.setString(i, listParser.listToString( e.getGenres() ));
        i++; state.setString(i, e.getTitleEnglish());
        i++; state.setString(i, e.getTitleJapanese());
        i++; state.setString(i, e.getMalURL());
    }

    private void executeUpdate(AnimeModel e, String statement) throws SQLException {
        // auto generate franchise if not exist
        FranchiseDAO frDAO = new FranchiseDAO();
        if (e.getFranchise().getName().equals("")) {
            // set franchise as title
            e.setFranchise(new FranchiseModel(e.getTitle(), "anime"));
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
    public LinkedHashMap<String, List<AnimeModel>> loadFromFranchise(String s) throws SQLException {
        List<AnimeModel> res = new ArrayList<>();
        //
        try {
            ResultSet rs = getDatabaseAccess().getConn().createStatement().executeQuery(
                    "select * from anime where franchise='" + s.replace("'", "''") + "' order by aired asc;"
            );
            while (rs.next()) res.add(objectConstructor(rs));
        } catch (SQLException e) {
            return sortByState("");
        }
        //
        return new LinkedHashMap<>(){{
            put(s, res);
        }};
    }


    /**
     * sortby-utils
     * */
    public LinkedHashMap<String, List<AnimeModel>> customSortBy(String filter, String orderby) throws SQLException {
        // prepare
        LinkedHashMap<String, List<AnimeModel>> res = new LinkedHashMap<>();
        res.put("Entries", new ArrayList<>());
        // query
        Statement state = getDatabaseAccess().getConn().createStatement();
        ResultSet rs = state.executeQuery("select * from anime " + filter + " order by " + orderby + ";");
        // handling results
        while (rs.next()) {
            res.get("Entries").add(objectConstructor(rs));
        }
        //
        return res;
    }

    public LinkedHashMap<String, List<AnimeModel>> sortByTitle(String filterQuery) throws SQLException {
        return sortBy(
                String.format("select * from anime %s order by title asc", filterQuery),
                (anime) -> anime.getTitle().substring(0, 1).toUpperCase()
        );
    }

    public LinkedHashMap<String, List<AnimeModel>> sortByState(String filterQuery) throws SQLException {
        return sortBy(
                String.format("select * from anime %s order by state asc, title asc", filterQuery),
                (anime) -> stateDecoder.get(stateDecoder.STATE, anime.getState())
        );
    }

    public LinkedHashMap<String, List<AnimeModel>> sortByRating(String filterQuery) throws SQLException {
        return sortBy(
                String.format("select * from anime %s order by rating asc, title asc", filterQuery),
                (anime) -> ratingDecoder.RATING_FULL[anime.getRating()]
        );
    }

    public LinkedHashMap<String, List<AnimeModel>> sortByCompleteDate(String filterQuery) throws SQLException {
        return sortBy(
                String.format("select * from anime %s order by complete_date desc, state asc, id desc", filterQuery),
                (anime) -> String.format("%s", dateDecoder.get(anime.getCompleteDate().getYear()))
        );
    }

    public LinkedHashMap<String, List<AnimeModel>> sortByAired(String filterQuery) throws SQLException {
        return sortBy(
                String.format("select * from anime %s order by aired desc, title asc", filterQuery),
                (anime) -> String.format("%s", dateDecoder.get(anime.getAired().get(Calendar.YEAR)))
        );
    }

    // O(n), great !
    public LinkedHashMap<String, List<AnimeModel>> sortByFranchise(String filterQuery) throws SQLException {
        return sortBy(
                String.format("select * from anime %s order by franchise asc, aired asc", filterQuery),
                (anime) -> String.format("%s", anime.getFranchise().getName())
        );
    }


    /**
     * basically object constructor from servlet request
     * */
    public void updateModelFromServletRequest(AnimeModel anime, HttpServletRequest request) {
        // form 1
        anime.setTitle( request.getParameter("title") );
        anime.setFranchise(new FranchiseModel( request.getParameter("franchise"), "anime" ));
        anime.setComment( request.getParameter("comment") );
        anime.setType(Integer.parseInt( request.getParameter("type") ));
        anime.setState(Integer.parseInt( request.getParameter("state") ));
        anime.setSeen(Integer.parseInt( request.getParameter("seen") ));
        anime.setRating(Integer.parseInt( request.getParameter("rating") ));
        anime.setStartDate(new SeasonalDate(
                Integer.parseInt( request.getParameter("start-date-year") ),
                Integer.parseInt( request.getParameter("start-date-season") ),
                Integer.parseInt( request.getParameter("start-date-month") ),
                Integer.parseInt( request.getParameter("start-date-day") )
        ));
        anime.setCompleteDate(new SeasonalDate(
                Integer.parseInt( request.getParameter("complete-date-year") ),
                Integer.parseInt( request.getParameter("complete-date-season") ),
                Integer.parseInt( request.getParameter("complete-date-month") ),
                Integer.parseInt( request.getParameter("complete-date-day") )
        ));
        anime.setEpisodes(Integer.parseInt( request.getParameter("episodes") ));
        anime.setDuration(Integer.parseInt( request.getParameter("duration") ));
        anime.setRewatchNotes( request.getParameter("rewatch-notes") );

        // form 2
        anime.setMalId(Integer.parseInt( request.getParameter("mal-id") ));
        anime.setMalURL( request.getParameter("mal-url") );
        anime.setTitleEnglish( request.getParameter("title-english") );
        anime.setTitleJapanese( request.getParameter("title-japanese") );
        anime.setSource( request.getParameter("source") );
        anime.setStatus( request.getParameter("status") );
        anime.setPremiered( request.getParameter("premiered") );

        // aired
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt( request.getParameter("aired-year") ));
        calendar.set(Calendar.MONTH, Integer.parseInt( request.getParameter("aired-month") ));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt( request.getParameter("aired-day") ));
        anime.setAired(calendar);

        // finished airing
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, Integer.parseInt( request.getParameter("finished-airing-year") ));
        calendar2.set(Calendar.MONTH, Integer.parseInt( request.getParameter("finished-airing-month") ));
        calendar2.set(Calendar.DAY_OF_MONTH, Integer.parseInt( request.getParameter("finished-airing-day") ));
        anime.setFinishedAiring(calendar2);

        // temporarily, we skip OPs, EDs, Studios, Producers and Licensors
    }
}
