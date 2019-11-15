package com;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        List<String> files = getFiles("F:\\m3u8dir\\01b096c3c5dc4770b42778885f870e31");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("F:\\m3u8dir\\jhs.mp4"));
            byte[] bytes = new byte[1024];
            int length = 0;

            for (int i = 0; i < files.size(); i++) {
                String s = files.get(i);

                File file = new File(s);
                if (!file.exists())
                    continue;
                FileInputStream fis = new FileInputStream(file);
                while ((length = fis.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, length);
                }
                System.out.println(s);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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
