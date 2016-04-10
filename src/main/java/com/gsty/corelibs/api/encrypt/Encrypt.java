package com.gsty.corelibs.api.encrypt;

import android.util.Log;

/**
 * Created by zhangjie on 2015/10/30.
 */
public class Encrypt {
    private static final boolean DBG = true;
    private static final String TAG = "EncryptToServer";

    private static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQ"
            +"C01/54zPLCNxmSUnKhlahLTDaHOgc1I6LWS6nVRXHv79MqxmUbpweyTx2t4KjeseJr4Cl0MKP"
            +"nm8cxAcv0NP2OK0+wniUZGl8bxh6axFHSPYmQX35ULjTGlayMKgLhGIUX50FFx2CWCOwAQNjR"
            +"0hVUVi3uOd5FRkhSd1U1QzFKXQIDAQAB";
    private static final String MD5_SUFFIX = "yuexi";

    public static String getRSA(String ming){
//        String message = null;
//        try {
//            message = RSAUtils.encryptByPublicKey(ming, RSA_PUBLIC_KEY);
//        } catch (Exception e) {
//            Log.e(TAG,"getRSA()"+ e.toString());
//            e.printStackTrace();
//        }
//        return message;
        return ming;
    }

    public static String getMD5(String message){
        return MD5Util.MD5(message + MD5_SUFFIX);
    }

    public static String getRsaPublicKey(){
        return RSA_PUBLIC_KEY;
    }
}
