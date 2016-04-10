package com.gsty.corelibs.api.encrypt;


import com.gsty.corelibs.utils.GzipUtils;

/**
 * Created by zhangjie on 2015/11/4.
 */
public class Decrypt {
    private static final boolean DBG = true;
    private static final String TAG = "DecryptFromServer";

    private static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQ"
            + "C01/54zPLCNxmSUnKhlahLTDaHOgc1I6LWS6nVRXHv79MqxmUbpweyTx2t4KjeseJr4Cl0MKP"
            + "nm8cxAcv0NP2OK0+wniUZGl8bxh6axFHSPYmQX35ULjTGlayMKgLhGIUX50FFx2CWCOwAQNjR"
            + "0hVUVi3uOd5FRkhSd1U1QzFKXQIDAQAB";

    public static String getFromRsa(String response) {
        String message = GzipUtils.gunzip(response);
        return message;
    }
}
