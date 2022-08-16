package konasoft.mikadb.api;

import konasoft.mikadb.MikadbApplication;

import java.io.File;
import java.net.URISyntaxException;

public class DataAccess {
    private static String projectDir;

    public static String getProjectDir() throws URISyntaxException {
        if (projectDir == null) {
            File pwd = new File(MikadbApplication.class.getResource("/").toURI());
            projectDir = pwd.getParentFile().getParent();
        }
        return projectDir;
    }
}
