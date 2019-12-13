package com.down;

import com.utils.UrlRequest;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class DownM3U8Test {

    @Test
    public void down() {
        String indexPath = "https://cn5.tabaocss.com/hls/20180827/738ab3aa192b020f690a4a339fb6820c/1535320810/index.m3u8";
        String name = "一拳超人-test";
        DownM3U8.downUrl(indexPath, name);
    }

}