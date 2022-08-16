package konasoft.mikadb.model.homepage;

public class ScoreDistributeModel {
    private String score;
    private int entriesCount;

    public ScoreDistributeModel(String score, int entriesCount) {
        this.score = score;
        this.entriesCount = entriesCount;
    }

    public ScoreDistributeModel(String score) {
        this.score = score;
        this.entriesCount = 0;
    }

    public String getScore() {
        return score;
    }

    public int getEntriesCount() {
        return entriesCount;
    }

    public void add() {
        this.entriesCount += 1;
    }
}
