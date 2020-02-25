package org.dreamcat.common.io;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Create by tuke on 2019-03-31
 */
@Slf4j
public class CmdUtil {

    public static int killProcess(String name) {
        try {
            String command = String.format("taskkill /im %s /f", name);
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor(3, TimeUnit.SECONDS);
            return process.exitValue();
        } catch (Exception e) {
            log.error(e.getMessage());
            return 1;
        }
    }

}
