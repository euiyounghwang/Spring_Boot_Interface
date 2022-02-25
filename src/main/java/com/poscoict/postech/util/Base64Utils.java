package com.poscoict.postech.util;

//import org.apache.log4j.Logger;
//import org.apache.log4j.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.*;

import com.poscoict.postech.service.ServermanageService;


public class Base64Utils {
//	private static Logger logger = Logger.getLogger(Base64Utils.class);
	private static Logger logger = (Logger) LoggerFactory.getLogger(Base64Utils.class);
	/**
	 * base64 인코딩
	 * @param value
	 * @return
	 */
	public String base64Encoding(String value) {
		String retVal = "";
		try {
			byte[] plainText = null; // 평문
			plainText = value.getBytes();
			retVal = Base64.encodeBase64String(plainText);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return retVal;
	}
	
	/**
	 * base64 디코딩
	 * @param encodedString
	 * @return
	 */
	public String base64decoding(String encodedString) {
		String retVal = "";
		try {
			byte[] plainText = null; // 해쉬 값
			plainText = Base64.decodeBase64(encodedString);
			retVal = new String(plainText);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return retVal;
	}
	
	/**
	 * Base64 + Seed 암호화
	 * @param str
	 * @param key
	 * @return
	 */
	public String encrypt(String str, String key) {
		//if (key.length() != 24) { return ""; }
		
		StringBuffer strResult = new StringBuffer();
		try {
			String strTemp = "";
			SeedAlgUtil seed = new SeedAlgUtil(key.getBytes());
			strTemp = Base64.encodeBase64String(seed.encrypt(str.getBytes()));
			int forSize = strTemp.length();
			for (int i=0; i<forSize; i++) {
				if (strTemp.charAt(i) != '\n' && strTemp.charAt(i) != '\r') {
					strResult.append(strTemp.charAt(i));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return strResult.toString();
	}
	
	/**
	 * Base64 + Seed 복호화
	 * @param str
	 * @param key
	 * @return
	 */
	public String decrypt(String str, String key) {
		//if (key.length() != 24) { return ""; }
		
		StringBuffer strResult = new StringBuffer();
		try {
			String strTemp = "";
			SeedAlgUtil seedAlg = new SeedAlgUtil(key.getBytes());
			strTemp = new String(seedAlg.decrypt(Base64.decodeBase64(str)));
			int forSize = strTemp.length();
			for (int i=0; i<forSize && strTemp.charAt(i) != 0; i++) {
				if (strTemp.charAt(i) != '\n' && strTemp.charAt(i) != '\r') {
					strResult.append(strTemp.charAt(i));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return strResult.toString();
	}

}