package konasoft.mikadb.model.leaderboards.lists;

import java.net.URISyntaxException;

public class LeaderboardEntryModel {
    /**
     * fields & constructors
     * */
    private String entryTable;
    private String entryId;
    private String title;

    public LeaderboardEntryModel() {
        entryTable = "anime";
        entryId = "-1";
        title = "untitled";
    }

    /**
     * fields an entry must have
     * */
    public String getEntryTable() {
        return entryTable;
    }

    public String getEntryId() {
        return entryId;
    }

    public String getTitle() {
        return title;
    }

    public String getProfilePic() throws URISyntaxException {
        return null;
    }

    public String getLink() {
        return "/lists";
    }

    /**
     * setters
     * */
    public void setEntryTable(String entryTable) {
        this.entryTable = entryTable;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public void setTitle(String s) {
        title = s;
    }

    public void setProfilePic(String s) {

    }

    public void setLink(String s) {

    }
}
