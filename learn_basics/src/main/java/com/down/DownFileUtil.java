package com.down;

import com.utils.UrlRequest;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownFileUtil {

    private static String rootPath = "F:\\m3u8dir";

    public static void main(String[] args) {
        String indexPath = "https://cn5.tabaocss.com/hls/20180827/d877e17e0adceedfd37c6f83b511adad/1535324437/index.m3u8";
        String prePath = indexPath.substring(0,indexPath.lastIndexOf("/")+1);
        System.out.println(prePath);
        //下载索引文件
        String indexStr = getIndexFile(indexPath);
        //解析索引文件
        List videoUrlList = analysisIndex(indexStr);

        //生成文件下载目录
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String fileRootPath = rootPath+File.separator+uuid;
        File fileDir = new File(fileRootPath);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        //下载视频片段，分成50个线程切片下载
        HashMap keyFileMap = new HashMap();
        int downForThreadCount = videoUrlList.size()/50;
        for(int i=0;i<videoUrlList.size();i+=downForThreadCount){
            int end = i+downForThreadCount-1;
            if(end>videoUrlList.size()){
                end = videoUrlList.size()-1;
            }
            new DownFileUtil().new downLoadNode(videoUrlList,i,end,keyFileMap,prePath,fileRootPath).start();
        }
        //等待下载
        while (keyFileMap.size()<videoUrlList.size()){
            System.out.println("当前下载数量"+keyFileMap.size());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //合成视频片段
        composeFile(fileRootPath+File.separator+uuid+".mp4",keyFileMap);
    }


    /**
     * 视频片段合成
     * @param fileOutPath
     * @param keyFileMap
     */
    public static void composeFile(String fileOutPath,HashMap<Integer,String> keyFileMap){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileOutPath));
            byte[] bytes = new byte[1024];
            int length = 0;
            for(int i=0;i<keyFileMap.size();i++){
                String nodePath = keyFileMap.get(i);
                File file = new File(nodePath);
                if(!file.exists())
                    continue;
                FileInputStream fis = new FileInputStream(file);
                while ((length = fis.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, length);
                }
            }
        }catch (Exception e){

        }finally {

        }
    }




    public static List analysisIndex(String content){
        Pattern pattern = Pattern.compile("[^/]+ts");

        Matcher ma = pattern.matcher(content);

        List<String> list = new ArrayList<String>();

        while(ma.find()){
            String s = ma.group();
            list.add(s);
            System.out.println(s);
        }
        return list;
    }


    class downLoadNode extends  Thread{
        private List<String> list ;
        private  int start;
        private  int end;
        public  HashMap keyFileMap ;
        private  String preUrlPath ;
        private  String fileRootPath ;

        public  downLoadNode(List list,int start,int end,HashMap keyFileMap,String preUrlPath,String fileRootPath){
            this.list = list;
            this.end = end;
            this.start = start;
            this.keyFileMap = keyFileMap;
            this.preUrlPath = preUrlPath;
            this.fileRootPath = fileRootPath;
        }
        @Override
        public void run(){
            try{
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                for( int i = start;i<=end;i++){
                    String urlpath = list.get(i);
                    HttpsURLConnection conn = UrlRequest.getHttpsURLConnection(preUrlPath + urlpath);
                    conn.connect();


                    //下在资源
                    DataInputStream dataInputStream = new DataInputStream(conn.getInputStream());
                    String fileOutPath = fileRootPath+File.separator+urlpath;
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(fileOutPath));
                    byte[] bytes = new byte[1024];
                    int length = 0;
                    while ((length = dataInputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, length);
                    }
                    dataInputStream.close();
                    keyFileMap.put(i,fileOutPath);
                }
                System.out.println("第"+start/(end-start)+"组完成，"+"开始位置"+start+",结束位置"+end);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static String getIndexFile(String urlpath){

        String get = UrlRequest.httpsRequest(urlpath, "GET", null);
        return get;
    }
    /**
     * @Author：
     * @Description：获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件，所以不用递归获取
     * @Date：
     */

}