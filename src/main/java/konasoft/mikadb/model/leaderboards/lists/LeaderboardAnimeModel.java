package konasoft.mikadb.model.leaderboards.lists;

import konasoft.mikadb.sqlite.dao.lists.AnimeDAO;

import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaderboardAnimeModel extends LeaderboardEntryModel {
    /**
     * core fields
     * */
    private String entryId;
    private String title;

    /**
     * construtors
     * */
    public LeaderboardAnimeModel(String entryId, String title) {
        super();
        this.entryId = entryId;
        this.title = title;
    }

    /**
     * getters
     * */
    @Override
    public String getEntryTable() {
        return "anime";
    }

    @Override
    public String getEntryId() {
        return entryId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getProfilePic() throws URISyntaxException {
        return new AnimeDAO().getProfilePic(Integer.parseInt(entryId));
    }

    @Override
    public String getLink() {
        return "/lists/anime/view/" + entryId;
    }

    /**
     * setters
     * */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
}
