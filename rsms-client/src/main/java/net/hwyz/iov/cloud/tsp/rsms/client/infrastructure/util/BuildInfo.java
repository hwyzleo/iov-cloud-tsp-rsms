package net.hwyz.iov.cloud.tsp.rsms.client.infrastructure.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BuildInfo {
    private static final Properties buildProps = new Properties();

    static {
        try (InputStream in = BuildInfo.class.getResourceAsStream("/version.properties")) {
            if (in != null) {
                buildProps.load(in);
            }
        } catch (IOException e) {
            // 处理异常
        }
    }

    public static String getBuildNumber() {
        return buildProps.getProperty("build.number", "unknown");
    }
}
