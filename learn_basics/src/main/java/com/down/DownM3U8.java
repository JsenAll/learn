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
    private static String rootPath = "F:\\m3u8dir";
    static HashMap keyFileMap = new HashMap();
    static int size;

    public static String getIndexFile(String indexPath, String title) {

        final String prePath = indexPath.substring(0, indexPath.lastIndexOf("/") + 1);

        String get = UrlRequest.httpsRequest(indexPath, "GET", null);

        final List<String> videoUrlList = analysisIndex(get);
        size = videoUrlList.size();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final String uuid = title;
//                UUID.randomUUID().toString().replaceAll("-", "");

        for (int i = 0; i < videoUrlList.size(); i++) {
            final String urlpath = videoUrlList.get(i);
            final String j = String.valueOf(i);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    List<String> videoUrl = new ArrayList<>();
                    videoUrl.add(j);
                    videoUrl.add(urlpath);
                    downLoadIndexFile(prePath, videoUrl, uuid);
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
        Pattern pattern = Pattern.compile("[^/]+ts");
        Matcher ma = pattern.matcher(content);

        List<String> list = new ArrayList<String>();

        while (ma.find()) {
            String s = ma.group();
            list.add(s);
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
    public static List<String> downLoadIndexFile(String preUrlPath, List<String> urlList, String uuid) {
        try {
            List<String> filePathList = new ArrayList<String>();
            String key = urlList.get(0);
            urlList.remove(0);
            for (String urlpath : urlList) {
                HttpsURLConnection conn = UrlRequest.getHttpsURLConnection(preUrlPath + urlpath);
                conn.connect();
                String fileOutPath = rootPath + File.separator + uuid + File.separator + urlpath;
                File file = new File(rootPath + File.separator + uuid);
                //下在资源
                DataInputStream dataInputStream = new DataInputStream(conn.getInputStream());
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(new File(fileOutPath));
                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = dataInputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, length);

                }
                System.out.println(Thread.currentThread().getName() + "下载完成..." + fileOutPath);
                keyFileMap.put(key, fileOutPath);
                dataInputStream.close();
                filePathList.add(fileOutPath);
            }

            return filePathList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void composeFile(String fileOutPath) {
        List<String> files = JFileUtils.getFiles(fileOutPath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("F:\\m3u8dir\\jhs一拳超人.mp4"));
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

    public static void main(String[] args) throws InterruptedException {
        String indexPath = "https://cn5.tabaocss.com/hls/20180827/d877e17e0adceedfd37c6f83b511adad/1535324437/index.m3u8";
        String name = "一拳超人1";
        getIndexFile(indexPath, name);
        Boolean is = true;
        String puthFile = rootPath + File.separator + name;
        Thread.sleep(10000);
        while (is) {
            List<String> files = JFileUtils.getFiles(puthFile);
            System.out.println("下载->" + files.size() + "一共->" + size);
            if (files.size() == size) {
                System.out.println("开始合并");
                is = false;

            }
            Thread.sleep(1000);
        }

        composeFile(puthFile);

    }
}
