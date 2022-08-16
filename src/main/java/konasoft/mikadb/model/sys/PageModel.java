package konasoft.mikadb.model.sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageModel {
    private List<String> path;
    private String displayName;
    private String lord;
    private String subLord;

    public PageModel(String path, String displayName, String lord, String subLord) {
        this.displayName = displayName;
        this.lord = lord;
        this.subLord = subLord;
        //
        this.path = new ArrayList<>();
        setPath(path);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", getStringPath(), getDisplayName());
    }

    // setters
    public void setPath(String p) {
        path.clear();
        path.addAll(Arrays.asList(
                p.substring(1).split("/"))
        );
    }

    // getters
    public List<String> getPath() {
        return path;
    }

    public String getStringPath() {
        StringBuilder s = new StringBuilder();
        for (String i: path) {
            s.append("/"); s.append(i);
        }
        return s.toString();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLord() {
        return lord;
    }

    public String getSubLord() {
        return subLord;
    }

    public String getName() {
        return path.get(path.size() - 1);
    }

    public String getMajorName() {
        return path.get(0);
    }
}
