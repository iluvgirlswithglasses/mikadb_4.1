package konasoft.mikadb.model.lists;

public class FranchiseModel {
    private int id;
    private String name;
    private String db;

    /**
     * constructors
     * */
    public FranchiseModel() {
        id = -1;
        name = "";
        db = "";
    }

    public FranchiseModel(String name, String db) {
        id = -1;
        this.name = name;
        this.db = db;
    }

    public FranchiseModel(int id, String name, String db) {
        this.id = id;
        this.name = name;
        this.db = db;
    }

    /**
     * getters
     * */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDb() {
        return db;
    }

    /**
     * setters
     * */
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDb(String db) {
        this.db = db;
    }
}
