package com.github.abator.utils;

import java.io.File;

/**
 * @author xiebiao[谢彪]
 */
public final class FileUtils {

    /**
     * 清除指定目录下的文件,不删除目录
     * @param filePath
     */
    public static void cleanFiles(String filePath) {
        File f = new File(filePath);
        if (f.exists() && f.isDirectory()) {
            File delFile[] = f.listFiles();
            if (delFile.length > 0) {
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        cleanFiles(delFile[j].getAbsolutePath());
                    } else {
                        System.out.println("删除" + delFile[j].getAbsolutePath());
                        delFile[j].delete();
                    }
                }
            }
        }
    }

    /**
     * 删除目录，以及目录下所有文件
     * @param filePath
     */
    public static void delete(String filePath) {

        File f = new File(filePath);
        if (f.exists() && f.isDirectory()) {
            File delFile[] = f.listFiles();
            if (delFile.length == 0) {
                f.delete();
            } else {
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        delete(delFile[j].getAbsolutePath());
                    } else {
                        delFile[j].delete();
                    }
                }
            }
        }
    }
}
