package com.parse.video;

import com.down.DownM3U8;
import com.fasterxml.jackson.databind.JsonNode;
import com.parse.AnalyInterface;
import com.utils.UrlRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 全视频tv
 * https://www.qsptv.com/
 * 2019-11-18
 */
public class ParseAnalyQsptvPC implements AnalyInterface {
    public static String prt = "var zanpiancms_player =(.*);</script>";

    @Override
    public List<Object> parse(String url, Map<String, Object> param) throws Exception {
        String com = UrlRequest.httpsRequest(url, "GET", null);
        Document parse = Jsoup.parse(com);
        String title = parse.select("body > div.container > div:nth-child(1) > div.layout-box.clearfix.p-0.m-0 > div.col-md-9.col-sm-12.col-xs-12.player_left > div.player_title > h1").text();
        String urlm3u = getpat(com, prt);
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(urlm3u);
        String urldown = jsonNode.path("url").asText();
        System.out.println(title + urldown);
        DownM3U8.downUrl(urldown, title);
        return null;
    }

    public static void main(String[] args) {
        try {
            for (int i = 1; i < 12; i++) {
                new ParseAnalyQsptvPC().parse("https://www.qsptv.com/play/46764-1-" + i + ".html", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
