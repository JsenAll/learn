package com;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ReplaceTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String u = "%u7b2c01";
        u = new String(u.getBytes(), "Utf-8");
        String s = u.replaceAll("\\u0025u", "\\\\u");
        u = new String(s.getBytes(), "Utf-8");
        System.out.println(
                "\u7b2c01"
        );
        System.out.println(new String(s.getBytes("utf-8"), "utf-8"));
        String decode = URLDecoder.decode(s, "utf-8");
        System.out.println(decode);
        String s1 = decodeUnicode(s);
        System.out.println(s1);
    }
    public static String decodeUnicode(String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }
}
