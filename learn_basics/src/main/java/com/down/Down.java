package com.down;

import com.utils.UrlRequest;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Down {
    public static void main(String[] args) throws IOException {
        HttpsURLConnection conn = UrlRequest.getHttpsURLConnection("https://p1.pstatp.com/large/tos-cn-p-0015/7fe206125d254a6297bac6b4a112a1fe_1572777419.jpg");
        File file = new File("D:\\Filetest\\fileOK");
        if (!file.exists()) {
            file.mkdirs();
        }
        DataInputStream dataInputStream = new DataInputStream(conn.getInputStream());

        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\Filetest\\fileOK\\"+"jhs"+".jpg"));
        byte[] bytes = new byte[1024];
        int length = 0;
        while ((length = dataInputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, length);
        }
        dataInputStream.close();
    }
}
