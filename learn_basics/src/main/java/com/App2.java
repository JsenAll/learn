package com;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App2 {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://www.youzhidy.com/gqdy/om/dzd6.html");
        CloseableHttpResponse response = httpclient.execute(httpget);
        System.out.println(response.getProtocolVersion());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().toString());
        HttpEntity entity = response.getEntity();
        Header[] allHeaders = response.getAllHeaders();

        String fileName = response.getHeaders("Content-Disposition")[0].getValue().split("filename=")[1];
        if (entity != null) {
            InputStream instream = entity.getContent();
            try {
                byte b[] = new byte[1024] ;        // 所有的内容都读到此数组之中

                instream.read(b);
                System.out.println(new String (b));
                // do something useful
            } finally {
                instream.close();
            }
        }
        response.close();
    }



}
