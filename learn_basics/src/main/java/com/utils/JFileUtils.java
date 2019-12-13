package com.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具
 * 2019-11-15
 */
public class JFileUtils {
    /**
     * 读取文件中的文件  带路径
     *
     * @param path
     * @return
     */
    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        if (tempList == null || tempList.length == 0){
            System.out.println("没有文件");
            return files;
        }

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
                //文件名，不包含路径
                //String fileName = tempList[i].getName();
            }
            if (tempList[i].isDirectory()) {
                //这里就不递归了，
            }
        }
        return files;
    }
}
