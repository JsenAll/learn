package com.parse.video;

import com.parse.AnalyInterface;
import com.utils.UrlRequest;

import javax.net.ssl.HttpsURLConnection;
import java.util.List;
import java.util.Map;

/**
 * 全视频tv
 * https://www.qsptv.com/
 * 2019-11-18
 */
public class ParseAnalyQsptvPC implements AnalyInterface {
    @Override
    public List<Object> parse(String url, Map<String, Object> param) throws Exception {
        String com = UrlRequest.httpsRequest(url, "GET", null);



        return null;
    }
}
