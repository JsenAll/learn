package com.down;


import com.utils.UrlRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownM3U8 {
    private static String rootPath = "F:\\m3u8dir";

    public static void down(String urlpath){
        DownM3U8 downM3U8=new DownM3U8();
        String indexFile = downM3U8.getIndexFile(urlpath);
        List<String> stringList = downM3U8.analysisIndex(indexFile);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final String prePath = urlpath.substring(0, urlpath.lastIndexOf("/") + 1);
        final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        for (final String urll : stringList) {
                     executorService.execute(() -> {
                    List<String> videoUrl = new ArrayList<>();
                    videoUrl.add(urll);
                    downM3U8.downLoadIndexFile(prePath, videoUrl, uuid);

            });
            System.out.println(urll);
        }
        executorService.shutdown();

    }




    public  String getIndexFile(String urlpath) {
        String content = UrlRequest.httpsRequest(urlpath, "GET", null);
        return content;
    }

    /**
     * 解析索引文件
     *
     * @param content
     * @return
     */
    public  List<String> analysisIndex(String content) {
        Pattern pattern = Pattern.compile(".*ts");
        Matcher ma = pattern.matcher(content);

        List<String> list = new ArrayList<String>();

        while (ma.find()) {
            String s = ma.group();
            list.add(s);
//            System.out.println(s);
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
            for (String urlpath : urlList) {
//

                String fileOutPath = rootPath + File.separator + uuid + File.separator + urlpath;
                File file = new File(rootPath + File.separator + uuid);
                //下在资源
                DataInputStream dataInputStream = new DataInputStream(UrlRequest.getHttpsURLConnection(preUrlPath + urlpath).getInputStream());
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
                dataInputStream.close();
                filePathList.add(fileOutPath);
            }

            return filePathList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
