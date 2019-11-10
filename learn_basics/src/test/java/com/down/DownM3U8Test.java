package com.down;

import com.utils.UrlRequest;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class DownM3U8Test {

    @Test
    public void down() {
        DownM3U8.down("https://sp.new131.com/20190623/7kXhtTKn/900kb/hls/index.m3u8");
    }

    public static void main(String[] args) {
        DownM3U8 downM3U8=new DownM3U8();
        String indexPath = "https://s1.jxtvsb.com/20191106/3ZWB9g1uFx2lT6pA/index.m3u8";
//        indexPath = "https://youku.cdn2-youku.com/20180710/12991_efbabf56/1000k/hls/index.m3u8";
        String prePath = indexPath.substring(0, indexPath.lastIndexOf("/") + 1);
        String get = UrlRequest.httpsRequest(indexPath, "GET", null);
        List videoUrlList =downM3U8. analysisIndex(get);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        List<String> fileList = downM3U8.downLoadIndexFile(prePath, videoUrlList,uuid);
    }
}