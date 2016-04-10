package com.gsty.corelibs.api.encrypt;

import android.util.Base64;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

import javax.crypto.Cipher;

public class RSAUtils {
	private final static String RSA = "RSA";

	private RSAUtils(){}

	/**
	 * @throws NoSuchAlgorithmException
	 */
	public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
	}

	/**
	 * /None/NoPadding
	 * @param modulus
	 * @param exponent
	 * @return
	 */
	public static RSAPublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * /None/NoPadding
	 * @param modulus
	 * @param exponent
	 * @return
	 */
	public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 模长
		int key_len = publicKey.getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		byte[][] arrays = splitArray(data.getBytes("utf-8"), key_len - 11);
		//如果明文长度大于模长-11则要分组加密
		byte[] result = new byte[arrays.length * key_len];
		int pos = 0;
		for(byte[] arr : arrays){
			byte[] tmp = cipher.doFinal(arr);
			System.arraycopy(tmp, 0, result, pos, tmp.length);
			pos += tmp.length;
		}
		return bcd2Str(result);
	}

	 /**
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data, String publicKey)
			throws Exception {		
		RSAPublicKey pubKey=(RSAPublicKey) loadPublicKey(publicKey);
		String modulus = pubKey.getModulus().toString();
		String public_exponent = pubKey.getPublicExponent().toString();
		pubKey = getPublicKey(modulus, public_exponent);	
		return encryptByPublicKey(data,pubKey);
	}

	/**
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		//模长
		int key_len = privateKey.getModulus().bitLength() / 8;
		byte[] bytes = data.getBytes("utf-8");
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		//如果密文长度大于模长则要分组解密
		byte[][] arrays = splitArray(bcd, key_len);
		byte[] result = new byte[arrays.length * key_len];
		int pos = 0;
		for(byte[] arr : arrays){
			byte[] tmp = cipher.doFinal(arr);
			System.arraycopy(tmp, 0, result, pos, tmp.length);
			pos += tmp.length;
		}
		return new String(result, "utf-8");
	}

	/**
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String data, String privateKey)
			throws Exception {
		RSAPrivateKey priKey=(RSAPrivateKey)loadPrivateKey(privateKey);
		String modulus = priKey.getModulus().toString();
		String private_exponent = priKey.getPrivateExponent().toString();
		priKey = getPrivateKey(modulus, private_exponent);
		return decryptByPrivateKey(data,priKey);
	}

	/**
	 * ASCII-BCD
	 */
	private static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}
	
	private static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	/**
	 * BCD
	 */
	private static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	private static byte[][] splitArray(byte[] data,int len){
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if(y!=0){
			z = 1;
		}
		byte[][] arrays = new byte[x+z][];
		byte[] arr;
		for(int i=0; i<x+z; i++){
		
			if(i==x+z-1 && y!=0){
				arr = new byte[y];
				System.arraycopy(data, i*len, arr, 0, y);
			}else{
				arr = new byte[len];
				System.arraycopy(data, i*len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}
	
	public static boolean isIllegal(String message, String password, String sign) {
//		if(StringUtils.isBlank(message) || StringUtils.isBlank(password) || StringUtils.isBlank(sign)) return true;
		if((message == null) || (password == null) || (sign == null)) return true;
		if (MD5Util.MD5(message.concat(password)).equalsIgnoreCase(sign)) return false;
		return true;
	}
	
	/**
	 * @param privateKeyStr
	 * @throws Exception
	 */
    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception{
        try {
//            BASE64Decoder base64Decoder= new BASE64Decoder();
//            byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
			byte[] buffer = Base64.decode(privateKeyStr.getBytes("UTF-8"),Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA/ECB/PKCS1Padding");
           return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("privateKey can not Algorithm");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Exception("privateKey is illegal");
//        } catch (IOException e) {
//            throw new Exception("PublicKey read error");
        } catch (NullPointerException e) {
            throw new Exception("privateKey is null");
        }
    }

    /**
     * @param publicKeyStr
     * @throws Exception
     */
    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception{
        try {
//            BASE64Decoder base64Decoder= new BASE64Decoder();
//            byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);
			byte[] buffer = Base64.decode(publicKeyStr.getBytes("UTF-8"), Base64.DEFAULT);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("PublicKey can not Algorithm");
        } catch (InvalidKeySpecException e) {
            throw new Exception("PublicKey is illegal");
//        } catch (IOException e) {
//            throw new Exception("PublicKey read error");
        } catch (NullPointerException e) {
            throw new Exception("PublicKey is null");
        }
    }

	public static String decryptByPublicKey(String data, RSAPublicKey publicKey)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		//模长
		int key_len = publicKey.getModulus().bitLength() / 8;
		byte[] bytes = data.getBytes("utf-8");
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		//如果密文长度大于模长则要分组解密
		byte[][] arrays = splitArray(bcd, key_len);
		byte[] result = new byte[arrays.length * key_len];
		int pos = 0;
		for(byte[] arr : arrays){
			byte[] tmp = cipher.doFinal(arr);
			System.arraycopy(tmp, 0, result, pos, tmp.length);
			pos += tmp.length;
		}
		return new String(result, "utf-8");
	}
	public static String decryptByPublicKey(String data) throws Exception {
		RSAPublicKey pubKey=(RSAPublicKey) loadPublicKey(Encrypt.getRsaPublicKey());
		//模
		String modulus = pubKey.getModulus().toString();
		//公钥指数
		String public_exponent = pubKey.getPublicExponent().toString();
		//使用模和指数生成公钥和私钥
		pubKey = getPublicKey(modulus, public_exponent);
		return decryptByPublicKey(data,pubKey);
	}

//  // only for test
//	public static void main(String[] args) throws Exception {
//		HashMap<String, Object> map = getKeys();
//		String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC01/54zPLCNxmSUnKhlahLTDaHOgc1I6LWS6nVRXHv79MqxmUbpweyTx2t4KjeseJr4Cl0MKPnm8cxAcv0NP2OK0+wniUZGl8bxh6axFHSPYmQX35ULjTGlayMKgLhGIUX50FFx2CWCOwAQNjR0hVUVi3uOd5FRkhSd1U1QzFKXQIDAQAB";
//		String password = "YueXi@15";
//		String ming="{'device':'wristRing','datas':[{'tick':'1444368600','steps': '200','distance': '300', 'calorie': '400', 'runTime': '30.6', 'sleepTime':'35.8','seriaNo':'343985AD43D3'},{'tick':'2015-02-03 02:03:04','steps': '200','distance': '300', 'calorie': '400', 'runTime': '30.6', 'sleepTime':'35.8','seriaNo':'343985AD43D3'}]}";
//		//RSAPublicKey publicKey = (RSAPublicKey) loadPublicKey(PropertyUtil.get("rsa_public_key"));
//		RSAPublicKey publicKey = (RSAPublicKey)loadPublicKey(RSA_PUBLIC_KEY);
//		String message = encryptByPublicKey(ming, publicKey);
//		String sign = MD5Util.MD5(message + password);
//	}
}
