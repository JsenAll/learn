package com.futils;

import java.io.*;

public class TextUtils {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\must-建新\\AppleMusic_Usage_2019Q1_TWN_v1.tsv"));
            InputStreamReader isr = new InputStreamReader(fileInputStream);// InputStreamReader 是字节流通向字符流的桥梁,

            BufferedReader br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
            String line;
            int a = 0;
            while ((line = br.readLine()) != null) {
                System.out.println(a++);
                System.out.println(line);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
