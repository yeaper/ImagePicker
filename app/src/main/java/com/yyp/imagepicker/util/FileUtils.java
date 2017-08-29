package com.yyp.imagepicker.util;

import java.io.File;

public class FileUtils {

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean fileIsExists(String path) {
        if (path == null || path.trim().length() <= 0) {
            return false;
        }
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
