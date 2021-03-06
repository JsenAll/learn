package com.down;


import com.utils.JFileUtils;
import com.utils.UrlRequest;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownM3U8 {
    private static String rootPath = "F:\\m3u8dir";//下载目录
    static int size;
    static final int nThreads = 10;//启动几个线程进行下载

    public static String getIndexFile(String indexPath, String title) {

        final String prePath = indexPath.substring(0, indexPath.lastIndexOf("/") + 1);

        String get = UrlRequest.httpsRequest(indexPath, "GET", null);

        final List<String> videoUrlList = analysisIndex(get);
        size = videoUrlList.size();
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        final String uuid = title;
//                UUID.randomUUID().toString().replaceAll("-", "");

        for (int i = 0; i < videoUrlList.size(); i++) {
            final String urlpath = videoUrlList.get(i);
            final String j = String.valueOf(i);
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    List<String> videoUrl = new ArrayList<>();
                    videoUrl.add(urlpath);
                    while (!downLoadIndexFile(prePath, videoUrl, uuid)) {
                        System.out.println("再一次下载" + j);
                    }
                }
            });
        }
        executorService.shutdown();


        return null;
    }

    /**
     * 解析索引文件
     *
     * @param content
     * @return
     */
    public static List analysisIndex(String content) {
        List<String> list = new ArrayList<String>();

        String[] split = content.split("\n");
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            Pattern pattern = Pattern.compile("[^/]+ts");
            Matcher ma = pattern.matcher(s);
            while (ma.find()) {
                String s1 = ma.group();
                list.add(s1);
            }
        }


        return list;
    }

    /**
     * 第三步，根据第二步的解析结果下载视频片段
     *
     * @param preUrlPath
     * @param urlList
     * @return
     */
    public static Boolean downLoadIndexFile(String preUrlPath, List<String> urlList, String uuid) {
        try {
            for (String urlpath : urlList) {
                HttpsURLConnection conn = UrlRequest.getHttpsURLConnection(preUrlPath + urlpath);
                String fileOutPath = rootPath + File.separator + uuid + File.separator + urlpath;
                File file = new File(rootPath + File.separator + uuid);
                if (!file.exists()) {
                    file.mkdirs();
                }
                //下在资源
                DataInputStream dataInputStream = new DataInputStream(conn.getInputStream());

                FileOutputStream fileOutputStream = new FileOutputStream(new File(fileOutPath));
                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = dataInputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, length);
                }
//                System.out.println(Thread.currentThread().getName() + "下载完成..." + fileOutPath);
                dataInputStream.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 将.ts 的视频片段合成MP4格式
     *
     * @param fileOutPath
     * @param name
     */
    public static void composeFile(String fileOutPath, String name) {
        List<String> files = JFileUtils.getFiles(fileOutPath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("F:\\m3u8dir\\" + name + ".mp4"));
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
//                System.out.println(s);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downUrl(String indexPath, String name) {
        System.out.println("开始下载->" + name);
        getIndexFile(indexPath, name);
        String puthFile = rootPath + File.separator + name;
        Boolean is = true;
        while (is) {
            List<String> files = JFileUtils.getFiles(puthFile);
            System.out.println("未完成" + (size - files.size()));
            if (files.size() == size && files.size() != 0) is = false;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("开始合并");
        composeFile(puthFile, name);

    }
}
