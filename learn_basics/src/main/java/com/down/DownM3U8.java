package com.down;



import com.utils.UrlRequest;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownM3U8 {
    private static String rootPath = "F:\\m3u8dir";

    public static String getIndexFile(String urlpath) {
        try {
            URL url = new URL(urlpath);
            SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
            //下在资源
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String content = "";
            String line;
            while ((line = in.readLine()) != null) {
                content += line + "\n";
            }
            in.close();
            System.out.println(content);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析索引文件
     *
     * @param content
     * @return
     */
    public static List analysisIndex(String content) {
        Pattern pattern = Pattern.compile(".*ts");
        Matcher ma = pattern.matcher(content);

        List<String> list = new ArrayList<String>();

        while (ma.find()) {
            String s = ma.group();
            list.add(s);
            System.out.println(s);
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
                HttpsURLConnection conn= UrlRequest.getHttpsURLConnection(preUrlPath + urlpath);
                try {
                    conn.connect();
                } catch (SocketTimeoutException e) {
                    conn.connect();
                }

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
                dataInputStream.close();
                filePathList.add(fileOutPath);
            }

            return filePathList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String indexPath = "https://cn5.tabaocss.com/hls/20180827/d877e17e0adceedfd37c6f83b511adad/1535324437/index.m3u8";
//        indexPath = "https://s1.jxtvsb.com/20191101/0NlAZQLuGpKiJcBt/index.m3u8";
        final String prePath = indexPath.substring(0, indexPath.lastIndexOf("/") + 1);
        String get = UrlRequest.httpsRequest(indexPath, "GET", null);

        final List<String> videoUrlList = analysisIndex(get);

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        for (final String urlpath : videoUrlList) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    List<String> videoUrl = new ArrayList<>();
                    videoUrl.add(urlpath);
                    downLoadIndexFile(prePath, videoUrl, uuid);
                }
            });

        }
        executorService.shutdown();
// https://cn5.tabaocss.com/hls/20180827/d877e17e0adceedfd37c6f83b511adad/1535324437/film_00000.ts
    }
}
