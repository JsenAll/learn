package com.utils;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * https是对链接加了安全证书SSL的，
 * 如果服务器中没有相关链接的SSL证书，它就不能够信任那个链接，也就不会访问到了。所以我们第一步是自定义一个信任管理器。
 * 自要实现自带的X509TrustManager接口就可以了
 */
public class MyX509TrustManager implements X509TrustManager {


    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return null;
    }

}