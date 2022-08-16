package konasoft.mikadb.model.leaderboards;

import konasoft.mikadb.model.leaderboards.lists.LeaderboardEntryModel;

public class LeaderboardEntry<E extends LeaderboardEntryModel> {
    /**
     * fields
     * */
    private int id;
    private E model;
    private int leaderboardId;
    private int rank;
    private String description;

    /**
     * constructors
     * */
    public LeaderboardEntry() {
        this.id = -1;
        this.leaderboardId = 1;
        this.rank = 100;
        this.description = "";
    }

    public LeaderboardEntry(int id, int leaderboardId, int rank, String desc) {
        this.id = id;
        this.leaderboardId = leaderboardId;
        this.rank = rank;
        this.description = desc;
    }

    public LeaderboardEntry(int id, E model, int leaderboardId, int rank, String desc) {
        this.id = id;
        this.leaderboardId = leaderboardId;
        this.model = model;
        this.rank = rank;
        this.description = desc;
    }

    /**
     * getters
     * */
    public int getId() {
        return id;
    }

    public E getModel() {
        return model;
    }

    public int getLeaderboardId() {
        return leaderboardId;
    }

    public int getRank() {
        return rank;
    }

    public String getDescription() {
        return description;
    }

    /**
     * setters
     * */
    public void setId(int id) {
        this.id = id;
    }

    public void setModel(E model) {
        this.model = model;
    }

    public void setLeaderboardId(int leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }
}
