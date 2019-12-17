package com.down;

import com.utils.UrlRequest;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class DownM3U8Test {

    @Test
    public void down() {
        String indexPath = "https://youku.com-iqiyi.net/20191216/22687_ac28360f/1000k/hls/index.m3u8";
        String name = "庆余年-29";
        DownM3U8.downUrl(indexPath, name);
    }

}