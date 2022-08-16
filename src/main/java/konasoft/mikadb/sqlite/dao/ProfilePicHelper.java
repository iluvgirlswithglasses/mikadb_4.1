package konasoft.mikadb.sqlite.dao;

import konasoft.mikadb.api.DataAccess;

import java.io.File;
import java.net.URISyntaxException;

public class ProfilePicHelper {
    public static final String[] PROFILE_PIC_SUFFIXS = {".png", ".jpg", ".jpeg", ".webp"};

    // given folder and basename
    // returns filedir if exists
    // otherwise returns default
    public static String getProfilePic(String url, String basename) throws URISyntaxException {
        String prefix = DataAccess.getProjectDir() + "/static";
        //
        for (String suffix: PROFILE_PIC_SUFFIXS) {
            File file = new File(prefix + url + basename + suffix);
            if (file.exists()) {
                return url + basename + suffix;
            }
        }
        return url + "unknown.png";
    }
}
