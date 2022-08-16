package konasoft.mikadb.model.gallery;

public class AudioModel {
    private int id;
    private String cat;
    private String title;
    private String src;
    private String srcLink;
    private boolean available;
    private int tier;

    /**
     * constructors
     * */
    public AudioModel(int id, String cat, String title, String src, String srcLink, boolean available, int tier) {
        this.id = id;
        this.cat = cat;
        this.title = title;
        this.src = src;
        this.srcLink = srcLink;
        this.available = available;
        this.tier = tier;
    }

    /**
     * setters
     * */
    public void setId(int id) {
        this.id = id;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setSrcLink(String srcLink) {
        this.srcLink = srcLink;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    /**
     * getters
     * */
    public int getId() {
        return id;
    }

    public String getCat() {
        return cat;
    }

    public String getTitle() {
        return title;
    }

    public String getSrc() {
        return src;
    }

    public String getSrcLink() {
        return srcLink;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getTier() {
        return tier;
    }
}
