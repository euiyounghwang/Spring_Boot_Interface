package com.poscoict.postech.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//import org.apache.log4j.Logger;
import org.slf4j.*;

import net.sf.json.JSONObject;

/**
 * @fileName : EngineConnectionHandler.java
 * @author : Mr.frodo
 * @date : 2017.01.11
 * @version : 1.0
 * @description : Elastic Server HTTP 통신으로 검색 데이터를 받아옴
 */
public class EngineConnectionHandler {
//	private static Logger logger = Logger.getLogger(EngineConnectionHandler.class);
	private static Logger logger = (Logger) LoggerFactory.getLogger(EngineConnectionHandler.class);
	private EngineConnectionHandler() {}
	
	/**
	 * ElasticSearch Engine 검색 데이터를 요청한다.
	 * @param url
	 * @param query
	 * @return String
	 * @throws Exception
	 * @throws 
	 * @throws Exception
	 */
	public static JSONObject search(String url, String query, String usrid) throws Exception {

        StringBuilder     temp_sb = new StringBuilder();
        StringBuffer sb = new StringBuffer();
//        temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        temp_sb.append("[query]**>>["+usrid+"] >> "+url+" >> [DataProvider] >> Query >>>>>>>>>>>>>>>  " + query.replaceAll("\\s{2,}", " ").trim());  
//        temp_sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(temp_sb.toString());
        
		OutputStream os = null;
		BufferedReader br = null;
		HttpURLConnection httpConn = null;
		JSONObject obj = null;
		
		// request write
		try {
		
			long startTime = System.currentTimeMillis();
			URL engineUrl = new URL(url);
			httpConn = (HttpURLConnection) engineUrl.openConnection();
			// elastic search 5.x 헤더에 인증키 추가 elastic:gsaadmin
			httpConn.setRequestProperty("Authorization", "Basic ZWxhc3RpYzpnc2FhZG1pbg==");
			httpConn.setRequestProperty("Content-Type", "application/json");//검색엔진 버전 업그레이드 대응(20190102)
			httpConn.setConnectTimeout(20000);
			httpConn.setReadTimeout(20000);
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			
			
			os = httpConn.getOutputStream();
			
			os.write(query.getBytes("UTF-8"));
			os.flush();
			os.close();
			os = null;
			
			if(httpConn.getResponseCode() == HttpURLConnection.HTTP_OK){				
				br = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			}else{
				br = new BufferedReader(new InputStreamReader(httpConn.getErrorStream(), "UTF-8"));
				String read = "";
				while ((read = br.readLine()) != null) {
					sb.append(read);			       
				}
				logger.error(sb.toString());
				throw new Exception(sb.toString().replaceAll("\"", ""));
			}
			long endTime = System.currentTimeMillis();
			logger.info("##### ENGINE SEARCH TIME : " + (endTime - startTime));
			
			obj = JSONObject.fromObject(br.readLine());
			
			httpConn.disconnect();
			httpConn = null;
		
		} catch (java.net.SocketTimeoutException se) {
			throw new Exception("검색 서버 연결 시간을 초과 하였습니다.");
		}catch (Exception e) {
			//logger.error(e.getMessage(), e.getCause());
			throw new Exception("CONN_TIME_OUT|"+e.getMessage());
		}
		finally
	    {
			if (os != null) {
                try {
                	os.close();
                	os = null;
                } catch (IOException e) {
                    logger.error(e.getMessage(), e.getCause());
                }
            }          

			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					logger.error(e.getMessage(), e.getCause());
				}
			}

			if (httpConn != null) {
				try {
					httpConn.disconnect();
					httpConn = null;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			
	    }
		return obj;
	}

}
