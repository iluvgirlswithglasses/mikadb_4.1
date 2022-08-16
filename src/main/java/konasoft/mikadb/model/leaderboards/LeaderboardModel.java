package konasoft.mikadb.model.leaderboards;

import konasoft.mikadb.sqlite.dao.ProfilePicHelper;

import java.net.URISyntaxException;

public class LeaderboardModel {
    /**
     * fields
     * */
    private int id;
    private int priority;
    private String name;
    private String description;
    private boolean ranked;
    private String category;

    /**
     * constructors
     * */
    public LeaderboardModel() {
        this.id = -1;
        this.priority = 100;
        this.name = "untitled";
        this.description = "";
        this.ranked = false;
        this.category = "anime";
    }

    public LeaderboardModel(int id, int priority, String name, String description, boolean ranked, String category) {
        this.id = id;
        this.priority = priority;
        this.name = name;
        this.description = description;
        this.ranked = ranked;
        this.category = category;
    }

    /**
     * getters
     * */
    public String getThumbnail() throws URISyntaxException {
        return ProfilePicHelper.getProfilePic("/webcontent/leaderboards/thumb/", "" + id);
    }

    // generate page sublord for leaderboards
    public String getSubLord() {
        String r = "Ranked";
        if (!ranked) r = "Unranked";
        //
        return String.format(
                "Priority: %s | %s", priority, r
        );
    }

    /**
     * basic getters
     * */
    public int getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRanked() {
        return ranked;
    }

    public String getCategory() {
        return category;
    }

    /**
     * basic setters
     * */
    public void setId(int id) {
        this.id = id;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
